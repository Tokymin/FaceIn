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

import com.arcsoft.demo.Activity.CoursedetailActivity;
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
            public void onCourseItemClick(TextView tv, int jieci, int day, String des,String studentSum, String SumPeriod, String time, String title, String courseid, String coursename, String Classteacher, String coursemark, String remark,String classroom,String xuankeID,String fullTime) {
                openMore(tv, des,day,jieci,studentSum,SumPeriod,time,title,courseid,coursename,Classteacher, coursemark,remark,classroom,xuankeID,fullTime);
            }
        });
        new CommitCourseAsyntask(getContext(), SlindingActivity.userID);
        list=new ArrayList<Course>();
        list=HomeNewsFragment.courseLists;
        Log.e("?????520","::=="+list);
        courseTableView.updateCourseViews(list);
        return view;
    }
    //处理对话框的点击事件
    private void openMore(TextView tv, final String des, final int day, final int jieci, final String studentSum, final String SumPeriod, final String time, final String title, final String courseid, final String coursename, final String Classteacher, final String coursemark, final String remark, final String classroom, final String xuankeID,final String fullTime) {
        new AlertDialog.Builder(getContext()).setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info).setSingleChoiceItems(new String[]{"人脸签到","查看详情","删除该课"}, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        startDetector(1,fullTime,xuankeID);
                        break;
                    case 1:
                        dialog.dismiss();
                        lookmore(des,studentSum,SumPeriod,time,title,courseid,coursename,Classteacher, coursemark,remark,classroom);
                        break;
                    case 2:
                        dialog.dismiss();
                        courseTableView.clearViewsIfNeeded();
                        break;
                }
            }
        }).setNegativeButton("取消", null).show();
    }
    private void startDetector(int camera,String time,String xuankeID) {
        Intent it = new Intent(getActivity(), DetecterActivity.class);
        it.putExtra("Camera", camera);
        it.putExtra("userID", userID);
        it.putExtra("time", time);
        it.putExtra("xuankeID", xuankeID);

        startActivityForResult(it, REQUEST_CODE_OP);
    }
    private void startReview(){
        Intent intent=new Intent(getActivity(), ListView1.class);
        startActivity(intent);

    }
    public void lookmore(final String des, String studentSum, String SumPeriod, String time, String title, String courseid, String coursename, String Classteacher, String coursemark,String remark,String Classroom) {
        Intent it=new Intent(getActivity(), CoursedetailActivity.class);

        it.putExtra("studentSum" ,studentSum);//把课程信息传给另一个Activity
        it.putExtra("SumPeriod", SumPeriod);
        it.putExtra("time", time);
        it.putExtra("title", title);
        it.putExtra("courseid", courseid);
        it.putExtra("coursename", coursename);
        it.putExtra("Classteacher", Classteacher);
        it.putExtra("coursemark", coursemark);
        it.putExtra("remark", remark);
        it.putExtra("Classroom", Classroom);
        startActivity(it);
    }
}




