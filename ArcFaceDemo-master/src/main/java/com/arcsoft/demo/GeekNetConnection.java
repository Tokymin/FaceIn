package com.arcsoft.demo;

import android.util.Base64;
import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKFace;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by win7 on 2018/1/27.
 */

public class GeekNetConnection {


    public static AFR_FSDKFace getFaceData(String userID) {
        String url_getData = "http://geek-team.xin/signIn/getData?sId="+userID;
        AFR_FSDKFace face = null;
        Log.e(":::aaaaaa:::", "userID:::" + userID);
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        //FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Request request = new Request.Builder()//创建Request 对象。
                .url(url_getData)
                //.post(formBody.build())//传递请求体
                .build();
        Response response = null;
        Log.e(":::aaaaaa:::", "asfffffffffafs:::" + userID);
        try {
            response = client.newCall(request).execute();
            Log.e(":::bbbbb:::", "asfffffffffafs:::" + userID);
            String result = response.body().string();
            Log.e("bbbbb", "????5svdsg"+result);
            byte[] imgBytes= Base64.decode(result, Base64.DEFAULT);
            Log.e(":::GeekNetConnection:::", "imgBSRHdtrshszdsezgredyes:::");
            face = new AFR_FSDKFace(imgBytes);
            Log.e(":::GeekNetConnection:::", "sgrhethrsj:::");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.isSuccessful()) try {
            throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return face;
    }
}
