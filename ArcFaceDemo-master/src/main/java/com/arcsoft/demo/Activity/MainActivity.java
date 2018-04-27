package com.arcsoft.demo.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.demo.Bean.User;
import com.arcsoft.demo.Database.MyDatabaseHelper;
import com.arcsoft.demo.DemoCache;
import com.arcsoft.demo.R;
import com.arcsoft.demo.RegisterActivity1;
import com.arcsoft.demo.config.preference.Preferences;
import com.arcsoft.demo.config.preference.UserPreferences;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int FINISH = 1;
    private final String TAG = this.getClass().toString();
    private CheckBox check;
    private EditText mUser;
    private EditText mPassword;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String isFirstLogin = "true";//是否第一次登录，默认是第一次
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView imgVGuidePictures;
    private String username;
    private String userpassword;
    private TextView txtV_GuideContent;
    private Drawable[] pictures;
    private Animation[] animations;
    private int position = 0;
    private UIhandler3 uIhandler3;
    private Button btn_login;
    private AbortableFuture<LoginInfo> loginRequest;
    private static final String KICK_OUT = "KICK_OUT";
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KICK_OUT, kickOut);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); /*set it to be no title*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,/*set it to be full screen*/
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setTheme(android.R.style.Theme_Translucent_NoTitleBar);
        check = (CheckBox) findViewById(R.id.check);
        mUser = (EditText) findViewById(R.id.login_user_edit);
        mPassword = (EditText) findViewById(R.id.login_passwd_edit);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        Button button2 = (Button) findViewById(R.id.register);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity1.class);
                startActivity(i);
            }
        });
        //从注册界面拿到数据
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String userpassword = intent.getStringExtra("userpassword");
        mUser.setText(username);
        mPassword.setText(userpassword);


        //新建数据库管理对象
        dbHelper = new MyDatabaseHelper(this, "user.db", null, 1);
        //记住密码功能实现
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rememberPassword = sharedPreferences.getBoolean("rememberPassword", false);
        if (rememberPassword) { //默认值为""，保持键的一致性
            String account = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("userpassword", "");
            mUser.setText(account);
            mPassword.setText(password);
            check.setChecked(true);
        }
        initData();
        imgVGuidePictures = (ImageView) findViewById(R.id.imgV_guide_picture);
        txtV_GuideContent = (TextView) findViewById(R.id.txtV_guide_content);
        imgVGuidePictures.setImageDrawable(pictures[position]);
        imgVGuidePictures.startAnimation(animations[0]);
        uIhandler3 = new UIhandler3();
        requestBasicPermission();

    }



    protected void initData() {
        //图片资源初始化
        pictures = new Drawable[]{getResources().
                getDrawable(R.drawable.v5_3_0_guide_pic2), getResources().getDrawable(R.drawable.v5_3_0_guide_pic2), getResources().getDrawable(R.drawable.v5_3_0_guide_pic2),};
        animations = new Animation[]{AnimationUtils.loadAnimation(this, R.anim.v5_0_1_guide_welcome_fade_in), AnimationUtils.loadAnimation(this, R.anim.v5_0_1_guide_welcome_fade_in_scale), AnimationUtils.loadAnimation(this, R.anim.v5_0_1_guide_welcome_fade_out)};
        animations[0].setDuration(1500);
        animations[1].setDuration(3000);
        animations[2].setDuration(1500);
        animations[0].setAnimationListener(new GuideAnimationListener(0));
        animations[1].setAnimationListener(new GuideAnimationListener(1));
        animations[2].setAnimationListener(new GuideAnimationListener(2));
    }

    private class GuideAnimationListener implements Animation.AnimationListener { //内部类---引导动画的监听类
        private int index;//索引序列

        public GuideAnimationListener(int index) {
            this.index = index;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (index < (animations.length - 1)) {
                imgVGuidePictures.startAnimation(animations[index + 1]);
            } else {
                position++;
                if (position > (pictures.length - 1)) {
                    position = 0;
                }
                imgVGuidePictures.setImageDrawable(pictures[position]);
                imgVGuidePictures.startAnimation(animations[0]);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    //点击登录按钮

    class UIhandler3 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINISH:
                    String result = "";
                    if ("true".equals(msg.obj.toString())) {
                        result = "登录成功！";
                    } else if ("false".equals(msg.obj.toString())) {
                        result = "登录失败！";
                    } else {
                        result = msg.obj.toString();
                    }
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    if (msg.obj.toString().equals("true")) {
                        SlindingActivity.start(MainActivity.this, DemoCache.getAccount());
                        MainActivity.this.finish();
                    } else {
                        new AlertDialog.Builder(MainActivity.this).setIcon(getResources().getDrawable(R.drawable.cancel)).setTitle("登陆错误").setMessage("账号或密码不正确\n请检查后重试").create().show();
                    }
                    break;
            }
        }
    }

    /**
     * 基本权限管理
     */

    private final String[] BASIC_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private void requestBasicPermission() {
        MPermission.with(MainActivity.this).setRequestCode(BASIC_PERMISSION_REQUEST_CODE).permissions(BASIC_PERMISSIONS).request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    private void login() {
        Log.e("MainA","???1");
        username = mUser.getText().toString().trim();
        userpassword = mPassword.getText().toString().trim();
        User user = new User(username, userpassword); //封装输入的数据
        List<User> userlist = new ArrayList<User>();
//        saveNum();//保存学号
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String usersname = cursor.getString(cursor.getColumnIndex("user"));
                String userspassword = cursor.getString(cursor.getColumnIndex("password"));
                String isFirst = cursor.getColumnName(cursor.getColumnIndex("isFirstLogin"));
                if (usersname.equals(usersname)) { //查找到对应的用户名，赋值
                    isFirstLogin = isFirst;
                }
                User users = new User(usersname, userspassword); //封装数据
                userlist.add(users);
            } while (cursor.moveToNext());
        }
        editor = sharedPreferences.edit();//保存密码
        if (check.isChecked()) {
            editor.putBoolean("rememberPassword", true);
            editor.putString("username", username);
            editor.putString("userpassword", userpassword);
        } else {
            editor.clear();
        }
        editor.commit();
        if (userlist.contains(user)) {  //判断条件
            if (isFirstLogin.equals("true")) { //如果是第一次登录
                isFirstLogin = "false";//修改不是第一次登录
                db.execSQL("update user set isFirstLogin=?where user=?", new String[]{"false", mUser.getText().toString()});
                MainActivity.this.finish();
            }
        }
        DialogMaker.showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (loginRequest != null) {
                    loginRequest.abort();
                    onLoginDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。

        final String account =mUser.getEditableText().toString()+RegisterActivity1.secret;
        final String token = tokenFromPassword(mPassword.getEditableText().toString());
        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");

                onLoginDone();

                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 初始化消息提醒配置
                initNotificationConfig();

                // 进入主界面
//                MainActivity.start(MainActivity.this, null);
//                finish();
                Message ms2 = Message.obtain();
                ms2.what = FINISH;
                ms2.obj = "true";
                uIhandler3.sendMessage(ms2);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(MainActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(MainActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey) || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
