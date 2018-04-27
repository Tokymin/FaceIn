package com.arcsoft.demo.getdata.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.arcsoft.demo.Activity.MainActivity;
import com.arcsoft.demo.R;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PC on 2018/4/18.
 */

public class Task extends AsyncTask<String, Integer, Boolean> {
    String url = "http://geek-team.xin/LAR/sRegister";
    String sID;
    String sPassword;
    String s_data;
    File file;
    String sImage;
    String sImageName;
    Context context;

    public Task(Context context, String sID, String sPassword, String s_data, String sImage, String sImageName) {
        this.sID = sID;
        this.s_data = s_data;
        this.sPassword = sPassword;
        this.sImage = sImage;
        this.sImageName = sImageName;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... voids) {
        Boolean result = false;
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("sId", sID);
        formBody.add("sPassword", sPassword);
        formBody.add("s_data", s_data);
        JSONObject jo=new JSONObject();
        try {
            jo.put("image",sImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        formBody.add("s_image", jo.toString());
        formBody.add("s_image_name", sImageName);

        Request request = new Request.Builder()//创建Request 对象。
                .url(url).post(formBody.build())//传递请求体
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            JSONObject jo1 = new JSONObject(response.body().string());
            if (jo1.getString("result").equals("true")) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (!(response != null && response.isSuccessful())) try {
            throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.e("REG", "setpppp");
            Toast.makeText(context, R.string.register_success, Toast.LENGTH_SHORT).show();
            Log.e("REG", "setpppp1");
            DialogMaker.dismissProgressDialog();
            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
//                    getUsername = register_username.getText().toString().trim();
//                    getUserpassword = register_userpassword.getText().toString().trim();
            intent.putExtra("username", sID);
            intent.putExtra("userpassword", sPassword);
            context.startActivity(intent);
        } else {
            DialogMaker.dismissProgressDialog();

            Toast.makeText(context, "注册失败，此用户不存在", Toast.LENGTH_SHORT).show();
        }
    }


}
