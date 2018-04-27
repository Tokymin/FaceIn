package com.arcsoft.demo;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

public class Post {
    public static String postform(String sID,String sPassword,String s_data) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+sID+".jpeg");
        Log.e("FILE","：："+file.getName()+file.getPath());
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("sId", sID);
        parameters.put("sPassword", sPassword);
        parameters.put("s_data", s_data);

        return  singleFileUploadWithParameters("http://geek-team.xin/LAR/sRegister", "s_image",file, parameters);
    }

    private static String singleFileUploadWithParameters(String actionURL, String fileKey, File uploadFile, HashMap<String, String> parameters) {
        String end = "\r\n";
        String twoHyphens = "--";
        String suiJiMa = "7e0dd540448";
        String boundary = "-------------------------" + suiJiMa;
        String response = "";
        try {
            URL url = new URL(actionURL);
            URLConnection connection = url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置请求参数
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //获取请求内容输出流
            connection.setDoOutput(true);
            connection.setDoInput(true);
            Log.e("POST1","::");
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            String fileName = uploadFile.getName();
            //开始写表单格式内容
            //写参数
            Set<String> keys = parameters.keySet();
            for (String key : keys) {
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; name=\"");
                ds.write(key.getBytes());
                ds.writeBytes("\"" + end);
                ds.writeBytes(end);
                ds.write(parameters.get(key).getBytes());
                ds.writeBytes(end);
            }
            //写文件
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"" + fileKey + "\"; " + "filename=\"");
            //防止中文乱码
            ds.write(fileName.getBytes());
            ds.writeBytes("\"" + end);
            ds.writeBytes("Content-Type: " + fileName.substring(fileName.lastIndexOf('.') + 1) + end);
            ds.writeBytes(end);
            //根据路径读取文件
            FileInputStream fis = new FileInputStream(uploadFile);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            fis.close();
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.writeBytes(end);
            ds.flush();
            try {
                //获取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuilder s = new StringBuilder();
                String temp = "";
                while ((temp = reader.readLine()) != null) {
                    s.append(temp);
                }
                response = s.toString();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("No response get!!!");
            }
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Request failed!");
        }
        return response;
    }
}
