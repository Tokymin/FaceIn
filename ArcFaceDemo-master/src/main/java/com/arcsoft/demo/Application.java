package com.arcsoft.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.arcsoft.demo.Activity.WelcomeActivity;
import com.arcsoft.demo.chatroom.ChatRoomSessionHelper;
import com.arcsoft.demo.common.util.LogHelper;
import com.arcsoft.demo.common.util.crash.AppCrashHandler;
import com.arcsoft.demo.config.preference.Preferences;
import com.arcsoft.demo.config.preference.UserPreferences;
import com.arcsoft.demo.contact.ContactHelper;
import com.arcsoft.demo.event.DemoOnlineStateContentProvider;
import com.arcsoft.demo.main.activity.MainActivity;
import com.arcsoft.demo.mixpush.DemoPushContentProvider;
import com.arcsoft.demo.session.NimDemoLocationProvider;
import com.arcsoft.demo.session.SessionHelper;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.config.AVChatOptions;
import com.netease.nim.avchatkit.model.ITeamDataProvider;
import com.netease.nim.avchatkit.model.IUserInfoProvider;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import io.fabric.sdk.android.Fabric;


public class Application extends android.app.Application {
	public static String path;
	private final String TAG = this.getClass().toString();
	public FaceDB mFaceDB;
	Uri mImage;
	String user;


	@Override
	public void onCreate() {
		super.onCreate();
		mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
		path=this.getExternalCacheDir().getPath();
		mImage = null;

		//云信sdk
		DemoCache.setContext(this);

		// 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
		NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

		// crash handler
		AppCrashHandler.getInstance(this);

		// 以下逻辑只在主进程初始化时执行
		if (NIMUtil.isMainProcess(this)) {

//            // 注册自定义推送消息处理，这个是可选项
//            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
//
//            // 初始化红包模块，在初始化UIKit模块之前执行
//            NIMRedPacketClient.init(this);
			// init pinyin
			PinYin.init(this);
			PinYin.validate();
			// 初始化UIKit模块
			initUIKit();
			// 初始化消息提醒
			NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
			// 云信sdk相关业务初始化
			NIMInitManager.getInstance().init(true);
			// 初始化音视频模块
			initAVChatKit();
		}

		Crashlytics crashlyticsKit = new Crashlytics.Builder()
				.core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
				.build();

		// Initialize Fabric with the debug-disabled crashlytics.
		Fabric.with(this, crashlyticsKit);
	}

	private LoginInfo getLoginInfo() {
		String account = Preferences.getUserAccount();
		String token = Preferences.getUserToken();

		if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
			DemoCache.setAccount(account.toLowerCase());
			return new LoginInfo(account, token);
		} else {
			return null;
		}
	}

	private void initUIKit() {
		// 初始化
		NimUIKit.init(this, buildUIKitOptions());

		// 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
		NimUIKit.setLocationProvider(new NimDemoLocationProvider());

		// IM 会话窗口的定制初始化。
		SessionHelper.init();

		// 聊天室聊天窗口的定制初始化。
		ChatRoomSessionHelper.init();

		// 通讯录列表定制初始化
		ContactHelper.init();

		// 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
		NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

		NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
	}

	private UIKitOptions buildUIKitOptions() {
		UIKitOptions options = new UIKitOptions();
		// 设置app图片/音频/日志等缓存目录
		options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
		return options;
	}

	private void initAVChatKit() {
		AVChatOptions avChatOptions = new AVChatOptions(){
			@Override
			public void logout(Context context) {
				MainActivity.logout(context, true);
			}
		};
		avChatOptions.entranceActivity = WelcomeActivity.class;
		avChatOptions.notificationIconRes = R.drawable.ic_stat_notify_msg;
		AVChatKit.init(avChatOptions);

		// 初始化日志系统
		LogHelper.init();
		// 设置用户相关资料提供者
		AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
			@Override
			public UserInfo getUserInfo(String account) {
				return NimUIKit.getUserInfoProvider().getUserInfo(account);
			}

			@Override
			public String getUserDisplayName(String account) {
				return UserInfoHelper.getUserDisplayName(account);
			}
		});
		// 设置群组数据提供者
		AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
			@Override
			public String getDisplayNameWithoutMe(String teamId, String account) {
				return TeamHelper.getDisplayNameWithoutMe(teamId, account);
			}

			@Override
			public String getTeamMemberDisplayName(String teamId, String account) {
				return TeamHelper.getTeamMemberDisplayName(teamId, account);
			}
		});
	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(newBase);
		MultiDex.install(this);
	}
	public void setCaptureImage(Uri uri) {
		mImage = uri;
	}

	public Uri getCaptureImage() {
		return mImage;
	}

	/**
	 * @param path
	 * @return
	 */
	public static Bitmap decodeImage(String path) {
		Log.e("::::签到APP:::", "调用了Application中的decodeImage方法");
		Bitmap res;
		try {
			ExifInterface exif = new ExifInterface(path);//Android开发中，在对图片进行展示、编辑
			// 、发送等操作时经常会涉及Exif的操作，Android中操作Exif主要是
			// 通过ExifInterface，ExifInterface看上去是一个接口，其实是
			// 一个类，位于Android.media.ExifInterface的位置。进入Exif
			// Interface类，发现方法很少，主要就是三个方面：读取、写入、缩略图。
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//写入
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = 3;
			op.inJustDecodeBounds = false;
			//op.inMutable = true;
			res = BitmapFactory.decodeFile(path, op);
			//rotate and scale.
			Matrix matrix = new Matrix();

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				matrix.postRotate(90);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				matrix.postRotate(180);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				matrix.postRotate(270);
			}

			Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
			Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			temp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			int options = 100;
			while (bos.toByteArray().length/1024 > 100) {
				bos.reset();
				options -= 10;
				temp.compress(Bitmap.CompressFormat.JPEG, options, bos);
			}
			ByteArrayInputStream in = new ByteArrayInputStream(bos.toByteArray());
			temp = BitmapFactory.decodeStream(in, null, null);

			if (!temp.equals(res)) {
				res.recycle();
			}
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
