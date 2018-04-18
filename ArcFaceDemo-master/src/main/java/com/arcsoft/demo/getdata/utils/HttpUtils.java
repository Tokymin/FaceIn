package com.arcsoft.demo.getdata.utils;

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
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				baos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
			}else{
				//Toast.makeText(, text, duration)
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();

	}
}
