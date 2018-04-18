package com.arcsoft.demo.getdata.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.arcsoft.demo.getdata.utils.Common;

/**
 * 得到当天课程的信息
 * Created by Toky on 2018/4/17.
 */

public class CommitJsonAsyntask extends AsyncTask<Void,Integer,String>{
    Context context;
    String postdatas;
    String url="";
//	MyAdapter adapter; //自定义Adapter

    public CommitJsonAsyntask(Context context, String string) {
        super();
        this.context = context;
        this.postdatas = string;
    }

    @Override
    protected String doInBackground(Void... params) {

        String result= Common.postGetJson(url,postdatas);

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        //ui

    }
}
