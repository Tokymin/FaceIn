package com.arcsoft.demo.getdata.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.arcsoft.demo.getdata.utils.Common;

/**
 * 得到当天课程的信息
 * Created by Toky on 2018/4/17.
 */

public class CommitCourseAsyntask extends AsyncTask<Void,Integer,String>{
    Context context;
    String postdatas;//提交信息
    String url="";//url
    public CommitCourseAsyntask(Context context, String string) {
        super();
        this.context = context;
        this.postdatas = string;
    }

    @Override
    protected String doInBackground(Void... params) {

        String result= Common.postGetJson(url,postdatas);

        return result;//这个result是返回的json对象
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
