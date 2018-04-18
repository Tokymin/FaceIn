package com.arcsoft.demo.getdata.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.arcsoft.demo.R;
import com.arcsoft.demo.getdata.asynctask.DownloadAsyncTask;
import com.arcsoft.demo.getdata.bean.PictureBean;
import com.arcsoft.demo.getdata.constant.MyUrl;
import com.arcsoft.demo.getdata.utils.MyJsonUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
	ListView mListView;
	List<PictureBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //	1.初始化ListView控件
        //	2.初始化List集合
        list=new ArrayList<>();
        //	3.创建适配器
        //	4.为ListView匹配适配器
        //	5.异步任务为List加载数据
        new DownloadAsyncTask(this, list,new MyJsonUtils())
		.execute(MyUrl.url);
    }

}
