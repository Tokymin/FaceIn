package com.arcsoft.demo.getdata.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Toky on 2018/4/17.
 */

public class Common {
    public static String postGetJson(String url, String content) {
        try {
            URL mUrl = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            //设置链接超时时间
            mHttpURLConnection.setConnectTimeout(15000);
            //设置读取超时时间
            mHttpURLConnection.setReadTimeout(15000);
            //设置请求参数
            mHttpURLConnection.setRequestMethod("POST");
            //添加Header
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            //接收输入流
            mHttpURLConnection.setDoInput(true);
            //传递参数时需要开启
            mHttpURLConnection.setDoOutput(true);
            //Post方式不能缓存,需手动设置为false
            mHttpURLConnection.setUseCaches(false);

            mHttpURLConnection.connect();

            DataOutputStream dos = new DataOutputStream(mHttpURLConnection.getOutputStream());

            String postContent = content;

            dos.write(postContent.getBytes());
            dos.flush();
            // 执行完dos.close()后，POST请求结束
            dos.close();
            // 获取代码返回值
            int respondCode = mHttpURLConnection.getResponseCode();
            Log.d("respondCode","respondCode="+respondCode );
            // 获取返回内容类型
            String type = mHttpURLConnection.getContentType();
            Log.d("type", "type="+type);
            // 获取返回内容的字符编码
            String encoding = mHttpURLConnection.getContentEncoding();
            Log.d("encoding", "encoding="+encoding);
            // 获取返回内容长度，单位字节
            int length = mHttpURLConnection.getContentLength();
            Log.d("length", "length=" + length);
//            // 获取头信息的Key
//            String key = mHttpURLConnection.getHeaderField(idx);
//            Log.d("key", "key="+key);
            // 获取完整的头信息Map
            Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
            if (respondCode == 200) {
                // 获取响应的输入流对象
                InputStream is = mHttpURLConnection.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String msg=br.readLine();
                is.close();
                br.close();
                Log.d("Common", msg);
                return msg;
            }
            return "fail";
        }catch(Exception e){
            return "error";
        }
    }
}
