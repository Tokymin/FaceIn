package com.arcsoft.demo.getdata.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUtils {

	public static byte[] getBytesByUrl(String urlStr) {
		URL url = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			Log.e("HomeFr","????4");
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.connect();
			Log.e("HomeFr","????5"+conn.getResponseCode());
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				Log.e("HomeFr","????5");
				byte[] buffer = new byte[1024];
				int len = 0;
				baos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				is.close();
				baos.close();
				return baos.toByteArray();
			}else{
				Log.e("HomeFr","????6");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[10];

	}
}
