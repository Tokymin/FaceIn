package com.arcsoft.demo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcsoft.demo.Activity.ListView1;
import com.arcsoft.demo.Activity.SlindingActivity;
import com.arcsoft.demo.DetecterActivity;
import com.arcsoft.demo.R;
import com.arcsoft.demo.View.Course;
import com.arcsoft.demo.View.CourseTableView;
import com.arcsoft.demo.getdata.asynctask.CommitCourseAsyntask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Toky on 2018/1/13.
 */

public class TableFragment extends BaseFragment {
    private final String TAG = this.getClass().getSimpleName();
    private View view;
    private static final int REQUEST_CODE_OP = 3;
    private List<Course> list;
    private CourseTableView courseTableView;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table, container, false);
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
        new CommitCourseAsyntask(getContext(), SlindingActivity.userID);
        list = new ArrayList<>();//course的数组



        Course c1 = new Course();
        c1.setDay(1);
        c1.setDes("英语(四)\n\n二教101\n\n陈老师");
        c1.setJieci(1);
        list.add(c1);


        Course c2 = new Course();
        c2.setDay(1);
        c2.setDes("马克思主义基本原理概论\n\n三教E01\n\n熊老师");
        c2.setJieci(3);
        list.add(c2);

        Course c3 = new Course();
        c3.setDay(1);
        c3.setDes("概论统计\n\n二教501\n\n王老师");
        c3.setJieci(5);
        list.add(c3);

        Course c4 = new Course();
        c4.setDay(2);
        c4.setDes("Linux系统编程\n\n二教510\n\n欧阳老师");
        c4.setJieci(3);
        list.add(c4);

        Course c15 = new Course();
        c15.setDay(2);
        c15.setDes("Linux系统编程\n\n二教510\n\n欧阳老师");
        c15.setJieci(9);
        list.add(c15);

        Course c5 = new Course();
        c5.setDay(3);
        c5.setDes("概论统计\n\n主教110\n\n吴老师");
        c5.setJieci(1);
        list.add(c5);

        Course c6 = new Course();
        c6.setDay(3);
        c6.setDes("体育（四）\n\n老体育馆\n\n杨老师");
        c6.setJieci(3);
        list.add(c6);

        Course c7 = new Course();
        c7.setDay(3);
        c7.setDes("大学物理实验\n\n物理实验室601\n\n熊老师");
        c7.setJieci(7);
        list.add(c7);

        Course c8 = new Course();
        c8.setDay(4);
        c8.setDes("英语四\n\n二教506\n\n陈老师");
        c8.setJieci(1);
        list.add(c8);

        Course c9 = new Course();
        c9.setDay(4);
        c9.setDes("马克思主义基本原理概论\n\n二教501\n\n王老师");
        c9.setJieci(3);
        list.add(c9);

        Course c0 = new Course();
        c0.setDay(4);
        c0.setDes("电子电工技术\n\n主教110\n\n郭老师");
        c0.setJieci(5);
        list.add(c0);

        Course c10 = new Course();
        c10.setDay(5);
        c10.setDes("Linux系统编程\n\n二教510\n\n欧阳老师");
        c10.setJieci(1);
        list.add(c10);

        Course c11 = new Course();
        c11.setDay(6);
        c11.setDes("Linux系统编程\n\n二教510\n\n欧阳老师");
        c11.setJieci(3);
        list.add(c11);

        Course c12 = new Course();
        c12.setDay(5);
        c12.setDes("Web技术与应用\n\n信息学院M601\n\n杨老师");
        c12.setJieci(5);
        list.add(c12);

        Course c13 = new Course();
        c13.setDay(7);
        c13.setDes("形式与政策\n\n三教515\n\n黄老师");
        c13.setJieci(7);
        list.add(c13);
        courseTableView.updateCourseViews(list);
        return view;
    }


    //处理对话框的点击事件
    private void openMore(TextView tv, String des) {
        final String[] datas = des.split("\n\n");
        new AlertDialog.Builder(getContext()).setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info).setSingleChoiceItems(new String[]{"人脸签到","查看详情","删除该课"}, 0, new DialogInterface.OnClickListener() {
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
                    case 2:
                        dialog.dismiss();
                        courseTableView.clearViewsIfNeeded();
                        break;
                }
            }
        }).setNegativeButton("取消", null).show();
    }
    private void startDetector(int camera, String[] datas, String userID) {
        Intent it = new Intent(getActivity(), DetecterActivity.class);
        for (int i = 0; i < datas.length; i++) {
            Log.d(TAG, "chuanchuanaa" + datas[i]);
            it.putExtra("info" + i, datas[i]);//把课程信息传给另一个Activity
        }
        it.putExtra("Camera", camera);
        it.putExtra("userID", userID);
        startActivityForResult(it, REQUEST_CODE_OP);

    }
    private void startReview(){
        Intent intent=new Intent(getActivity(), ListView1.class);
        startActivity(intent);

    }
}




