package com.arcsoft.demo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.demo.Activity.ListView1;
import com.arcsoft.demo.Activity.NewsActivity;
import com.arcsoft.demo.Activity.NoteActivity;
import com.arcsoft.demo.Activity.SlindingActivity;
import com.arcsoft.demo.R;
import com.arcsoft.demo.View.Course;
import com.arcsoft.demo.View.XListView;
import com.arcsoft.demo.getdata.asynctask.DownloadAsyncTask;
import com.arcsoft.demo.getdata.utils.MyJsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.arcsoft.demo.Activity.SlindingActivity.userID;

/**
 * Created by Toky on 2018/1/13.
 */

public class HomeNewsFragment extends BaseFragment implements View.OnClickListener {
    private Context ctx;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextClock textClock;
    private DownloadAsyncTask gettoaday;
    private View view;
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    private int oldPosition = 0;
    private int[] imageIds = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };
    private String[]  titles = new String[]{
            "FaceIn",
            "人脸识别",
            "签到app",
            "一款基于人脸识别的签到app",
            "FaceIn"
    };
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;
    public static XListView mListView;
    public static SimpleAdapter mAdapter;//？？？
    public static Handler handler;
    public static List<Course> courseLists;

    public HomeNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);

        ctx = getContext();
        mViewPaper = (ViewPager) view.findViewById(R.id.vp);
        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.dot_0));
        dots.add(view.findViewById(R.id.dot_1));
        dots.add(view.findViewById(R.id.dot_2));
        dots.add(view.findViewById(R.id.dot_3));
        dots.add(view.findViewById(R.id.dot_4));

        title = (TextView) view.findViewById(R.id.title);
        findview();
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        Log.e("HomeFr","????2");
        /** 下拉刷新，上拉加载 */

        mListView = (XListView)view.findViewById(R.id.ListView_today);//
        mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mHandler = new Handler();
        courseLists=new ArrayList<Course>();

        new DownloadAsyncTask(getActivity(),userID,mHandler,courseLists,new MyJsonUtils()).execute();
        return view;
    }
//    public List<Course> getCourseLists(){
//        return this.courseLists;
//    }
    private void findview() {

        img1=view.findViewById(R.id.iv_chat);
        img2=view.findViewById(R.id.iv_addbj);
        img3=view.findViewById(R.id.iv_searchmark);
        img4=view.findViewById(R.id.iv_sign);//签到图片
        text1=view.findViewById(R.id.lookmore);//查看更多
        text2=view.findViewById(R.id.goreview);//复习
        text3=view.findViewById(R.id.more);//查更多

        textClock= view.findViewById(R.id.textClock);
        textClock.setFormat24Hour("yyyy-MM-dd");

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_chat:
                Intent intent = new Intent(getActivity(), com.arcsoft.demo.main.activity.MainActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_addbj:
                Intent intent1 = new Intent(getActivity(), NoteActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_searchmark:
                Toast.makeText(getActivity(), "sorry,此功能还在开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_sign:
                SlindingActivity slindingActivity=(SlindingActivity) getActivity();
                slindingActivity.setFramet(1);
                break;
            case R.id.lookmore:
                SlindingActivity slindingActivity2=(SlindingActivity) getActivity();
                slindingActivity2.setFramet(1);
                break;
            case R.id.goreview:
                Intent intent2 = new Intent(getActivity(), ListView1.class);
                startActivity(intent2);
                break;
            case R.id.more:
                Intent intent3 = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent3);
                break;
//            case R.id.today:
//                SlindingActivity slindingActivity3=(SlindingActivity) getActivity();
//                slindingActivity3.setFramet(1);
//                break;

        }

    }
    /**
     * 自定义Adapter
     *
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {

            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    public void onStart() {
        super.onStart();
        if(scheduledExecutorService!=null){
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ViewPageTask(), 2, 2, TimeUnit.SECONDS);
    }


    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    public  Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };
    @Override
    public void onStop() {
        super.onStop();
    }




}


