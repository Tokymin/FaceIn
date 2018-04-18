package com.arcsoft.demo.getdata.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcsoft.demo.getdata.utils.HttpUtils;

import java.util.Map;

//	用图片的连接下载图片
public class ImageAsyncTask extends AsyncTask<String, Void, byte[]> {

	Context context;
	ImageView imageView;
	String picUrl;
	Map<String, Bitmap> catchImage;

	public ImageAsyncTask(Context context, ImageView imageView, String picUrl,
			Map<String, Bitmap> catchImage) {
		super();
		this.context = context;
		this.imageView = imageView;
		this.picUrl = picUrl;
		this.catchImage = catchImage;
	}

	@Override
	protected byte[] doInBackground(String... params) {
		// 通过HttpUtils类下载图片
		return HttpUtils.getBytesByUrl(params[0]);
	}

	@Override
	protected void onPostExecute(byte[] result) {
		super.onPostExecute(result);
		if (result != null) {
			// 将下载到的图片字节转换成Bitmap
			Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
					result.length);
			/*
			 * ****防止图片错位****
			 * imageView.getTag().toString() 当前要现在的图片地址 iconUrl
			 * 之前没有下载完成的图片的地址
			 */
			if (imageView.getTag().toString().equals(picUrl)) {
				// 给ImageView设置图片
				imageView.setImageBitmap(bitmap);
				catchImage.put(imageView.getTag().toString(), bitmap);
			}
		} else {
			Toast.makeText(context, "网络连接错误，请检查网络", Toast.LENGTH_LONG).show();
		}
	}

}
