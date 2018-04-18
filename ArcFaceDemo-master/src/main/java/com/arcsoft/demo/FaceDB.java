package com.arcsoft.demo;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.guo.android_extend.java.ExtInputStream;
import com.guo.android_extend.java.ExtOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
public class FaceDB {
	private final String TAG = "::::签到APP::::FaceDB::::"+this.getClass().toString();
	public static String appid = "Ek2rZ6pz6n4qZS5eKLtfwsJi2BfGYRDdspKyLjdctDvC";
	public static String ft_key = "Ht6TRzS2wuPRkXiEZ2P1j9KmyW1QSnfSdkQXebm7jKL7";
	public static String fd_key = "Ht6TRzS2wuPRkXiEZ2P1j9Ku8uGdT8ACo8tssCEniPEk";
	public static String fr_key ="Ht6TRzS2wuPRkXiEZ2P1j9L2JJXnmJnvRojpWiuYkvbt";

	public static String data;

	String mDBPath;
	List<FaceRegist> mRegister;
	AFR_FSDKEngine mFREngine;
	AFR_FSDKVersion mFRVersion;
	boolean mUpgrade;

	class FaceRegist {
		String mName;
		List<AFR_FSDKFace> mFaceList;

		public FaceRegist(String name)
		{
			mName = name;
			mFaceList = new ArrayList<>();
		}
	}

	public FaceDB(String path)
	{
		mDBPath = path;
		mRegister = new ArrayList<>();
		mFRVersion = new AFR_FSDKVersion();
		mUpgrade = false;
		mFREngine = new AFR_FSDKEngine();
		AFR_FSDKError error = mFREngine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
		if (error.getCode() != AFR_FSDKError.MOK) {
			Log.e(TAG, "AFR_FSDK_InitialEngine fail! error code :" + error.getCode());
		} else {
			mFREngine.AFR_FSDK_GetVersion(mFRVersion);
			Log.d(TAG, "AFR_FSDK_GetVersion=" + mFRVersion.toString());
		}
		Log.e(TAG, "初始化完成");
	}

	public void destroy() {
		if (mFREngine != null) {
			mFREngine.AFR_FSDK_UninitialEngine();
		}
		Log.e(TAG, "destroy");
	}

	private boolean saveInfo() {
		try {
			FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt");
			ExtOutputStream bos = new ExtOutputStream(fs);
			bos.writeString(mFRVersion.toString() + "," + mFRVersion.getFeatureLevel());
			bos.close();
			fs.close();
			Log.e(TAG, "saveInfo::::true");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e(TAG, "saveInfo::::false");
		return false;
	}

	private boolean loadInfo() {
		if (!mRegister.isEmpty()) {
			Log.e(TAG, "loadInfo::::mRegister is not Empty::::false");
			return false;
		}
		Log.e(TAG, "loadInfo::::mRegister is Empty::::");
		try {
			FileInputStream fs = new FileInputStream(mDBPath + "/face.txt");
			ExtInputStream bos = new ExtInputStream(fs);
			//load version
			String version_saved = bos.readString();
			if (version_saved.equals(mFRVersion.toString() + "," + mFRVersion.getFeatureLevel())) {
				mUpgrade = true;
				Log.e(TAG, "loadInfo::::mRegister is Empty::::mUpgrade=true");
			}
			//load all regist name.
			if (version_saved != null) {
				for (String name = bos.readString(); name != null; name = bos.readString()){
					if (new File(mDBPath + "/" + name + ".data").exists()) {
						mRegister.add(new FaceRegist(new String(name)));
						Log.e(TAG, "loadInfo::::向mRegister中添加本地data");
					}
				}
			}
			bos.close();
			fs.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean loadFaces()
	{
		if (loadInfo()) {
			try {
				for (FaceRegist face : mRegister)
				{//mRegister存放的是所有的特征信息。
					Log.e(TAG, "loadFaces:::load name:" + face.mName + "'s face feature data.");
					new Teset().execute();
					Teset teset=new Teset();
					String y=teset.doInBackground();
					Log.e(TAG,"12345"+y);
					FileInputStream fs = new FileInputStream(mDBPath + "/" + face.mName + ".data");//从本地得到！
					ExtInputStream bos = new ExtInputStream(fs);
					AFR_FSDKFace afr = null;
					do {
						if (afr != null) {
							if (mUpgrade) {
							}
							face.mFaceList.add(afr);
						}
						afr = new AFR_FSDKFace();
					} while (bos.readBytes(afr.getFeatureData()));
					bos.close();
					Log.e(TAG, "loadFaces:::load name: size = " + face.mFaceList.size());
				}
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public	void addFace(String name, final AFR_FSDKFace face) {
		try {
			//check if already registered.
			boolean add = true;
			for (FaceRegist frface : mRegister) {
				if (frface.mName.equals(name)) {
					frface.mFaceList.add(face);
					Log.d(TAG,"mRegister.mFaceList.add(face)");
					add = false;
					break;
				}
				Log.d(TAG,"别吓我"+mRegister.size());
			}
			if (add) { // not registered.
				FaceRegist frface = new FaceRegist(name);
				frface.mFaceList.add(face);
				mRegister.add(frface);
				Log.d(TAG,"我来看看你"+mRegister.size());
			}
			if (saveInfo()) {
				//update all names
				FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt", true);
				ExtOutputStream bos = new ExtOutputStream(fs);
				for (FaceRegist frface : mRegister) {
					bos.writeString(frface.mName);
				}
				bos.close();
				fs.close();
				//save new feature
				//fs = new FileOutputStream(mDBPath + "/" + name + ".data", true);//特征信息保存的最终路径。
				//bos = new ExtOutputStream(fs);
				//bos.writeBytes(face.getFeatureData());

//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						PrintWriter out = null;
//						BufferedReader in = null;
//						try {
//
//							URL realUrl = new URL("");
//							File file =new File("/Android/data/com.arcsoft.sdk_demo/cache");
//							URLConnection conn = realUrl.openConnection();
//							conn.setRequestProperty("accept", "*/*");
//							conn.setRequestProperty("connection", "Keep-Alive");
//							conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//							conn.setDoOutput(true);
//							conn.setDoInput(true);
//							out = new PrintWriter(conn.getOutputStream());
//							JSONObject object=new JSONObject();
							 data= Base64.encodeToString(face.getFeatureData(), Base64.DEFAULT);
//							object.put("data",data);
//							out.println(object.toString());
//							out.flush();
//							in = new BufferedReader(
//									new InputStreamReader(conn.getInputStream()));
//							String line=in.readLine();
//							JSONObject object1=new JSONObject(line);
//							String result =object1.getString("result");
//						} catch (Exception e) {
//							System.out.println("     POST           "+e);
//							e.printStackTrace();
//						}
//						//finally
//						finally{
//							try{
//								if(out!=null){
//									out.close();
//								}
//								if(in!=null){
//									in.close();
//								}
//							}
//							catch(IOException ex){
//								ex.printStackTrace();
//							}
//						}
//
//
//					}
//				}).start();
				Log.d(TAG,"我已经吓傻了"+mRegister.size());
				bos.close();
				fs.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean delete(String name) {
		try {
			//check if already registered.
			boolean find = false;
			for (FaceRegist frface : mRegister)
			{
				if (frface.mName.equals(name)) {
					File delfile = new File(mDBPath + "/" + name + ".data");
					if (delfile.exists()) {
						delfile.delete();
					}
					mRegister.remove(frface);
					find = true;
					break;
				}
			}
			if (find) {
				if (saveInfo()) {
					//update all names
					FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt", true);
					ExtOutputStream bos = new ExtOutputStream(fs);
					for (FaceRegist frface : mRegister) {
						bos.writeString(frface.mName);
					}
					bos.close();
					fs.close();
				}
			}
			return find;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean upgrade() {
		return false;
	}

	class Teset extends AsyncTask<Void,Integer,String> {
		String result = "";

		@Override
		protected String doInBackground(Void... voids) {
			final String TAG = "uploadFile";
			PrintWriter out = null;
			BufferedReader in = null;

			try {
				URL url = new URL("http://geek-team.xin/FaceIn/SignStudent");
				URLConnection conn = url.openConnection();
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				conn.setDoOutput(true);
				conn.setDoInput(true);

				out = new PrintWriter(conn.getOutputStream());
				JSONObject object = new JSONObject();
				object.put("studentid", "11");
				out.println(object.toString());
				out.flush();
				Log.d(TAG,"下一句 in");

				InputStream inputStream1=conn.getInputStream();
				InputStreamReader reader=new InputStreamReader(inputStream1);
				in = new BufferedReader(reader);

				Log.d(TAG,"证明上一句正常执行");
				String line = in.readLine();
				Log.d(TAG,"&*&*&*&"+line);
				JSONObject object1 = new JSONObject(line);
				result=object1.getString("data");
				Log.d(TAG,"*(*(*(*(*"+result);
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			return result;

		}

	}



}
