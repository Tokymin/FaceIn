package com.arcsoft.demo.getdata.asynctask;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.arcsoft.demo.View.Course;
import com.arcsoft.demo.getdata.utils.HttpUtils;
import com.arcsoft.demo.getdata.utils.MyJsonUtils;

import java.util.List;

public class DownloadAsyncTask extends AsyncTask<String, Void, byte[]> {
	Context context;
	List<Course> list1;
	MyJsonUtils utils; //解析json数据的工具类
//	MyAdapter adapter; //自定义Adapter

	public DownloadAsyncTask(Context context, List<Course> list, MyJsonUtils utils) {
		super();
		this.context = context;
		this.list1 = list;
		this.utils = utils;

	}
	@Override
	protected byte[] doInBackground(String... params) {
		return HttpUtils.getBytesByUrl(params[0]);
	}
	@Override
	protected void onPostExecute(byte[] result) {
		super.onPostExecute(result);
		if(result!=null){
			String jsonStr=new String(result,0,result.length);
			System.out.print(jsonStr);
			List<Course> list2= (List<Course>) utils.parseJson(jsonStr);
			list1.addAll(list2);
//			adapter.notifyDataSetChanged();
		}else{
			Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
		}
	}

}
