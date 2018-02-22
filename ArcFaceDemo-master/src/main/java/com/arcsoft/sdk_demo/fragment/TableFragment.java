package com.arcsoft.sdk_demo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcsoft.sdk_demo.Activity.ListView1;
import com.arcsoft.sdk_demo.DetecterActivity;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.View.Course;
import com.arcsoft.sdk_demo.View.CourseTableView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Toky on 2018/1/13.
 */

public class TableFragment extends Fragment {
    private final int REQUE = 2;
    private final String TAG = this.getClass().getSimpleName();
    private View view;
    private static final int REQUEST_CODE_OP = 3;
    private List<Course> list;
    private CourseTableView courseTableView;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table, container, false);
        //courseTableView = (CourseTableView) courseTableView.findViewById(R.id.ctv);
        if (getArguments()!=null){
            userID = getArguments().getString("userID");
        }
        courseTableView = (CourseTableView) view.findViewById(R.id.ctv);
        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
            @Override
            public void onCourseItemClick(TextView tv, int jieci, int day, String des) {
                openMore(tv, des);
            }
        });
        list = new ArrayList<>();
        Course c1 = new Course();
        c1.setDay(1);
        c1.setDes("英语\n\n主教101\n\n杨老师");
        c1.setJieci(1);
        list.add(c1);

        Course c2 = new Course();
        c2.setDay(2);
        c2.setDes("线性代数\n\n三教E01\n\n熊老师");
        c2.setJieci(6);
        list.add(c2);

        Course c3 = new Course();
        c3.setDay(3);
        c3.setDes("数据库\n\n三教E01\n\n兰老师");
        c3.setJieci(5);
        list.add(c3);

        Course c4 = new Course();
        c4.setDay(4);
        c4.setDes("数据结构\n\n三教E01\n\n毛老师");
        c4.setJieci(7);
        list.add(c4);

        Course c5 = new Course();
        c5.setDay(5);
        c5.setDes("大学物理\n\n三教E01\n\n杨老师");
        c5.setJieci(9);
        list.add(c5);
        courseTableView.updateCourseViews(list);
        return view;
    }


    //处理对话框的点击事件
    private void openMore(TextView tv, String des) {
        final String[] datas = des.split("\n\n");
        new AlertDialog.Builder(getContext()).setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info).setSingleChoiceItems(new String[]{"人脸签到","查看详情"}, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        startDetector(1, datas, userID);
                        break;
                    case 1:
                        dialog.dismiss();
                        startReview();
                        break;
                }
            }
        }).setNegativeButton("取消", null).show();
    }
    private void startDetector(int camera, String[] datas, String userID) {
        Intent it = new Intent(getActivity(), DetecterActivity.class);
        for (int i = 0; i < datas.length; i++) {
            Log.d(TAG, "chuanchuanaa" + datas[i]);
            it.putExtra("info" + i, datas[i]);//把课程信息传给另一个Acti
            it.putExtra("Camera", camera);
            it.putExtra("userID", userID);
            startActivityForResult(it, REQUEST_CODE_OP);
        }
    }
    private void startReview(){
        Intent intent=new Intent(getActivity(), ListView1.class);
        startActivity(intent);

    }
}




