package com.arcsoft.demo.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.arcsoft.demo.R;

/**
 * Created by Toky on 2018/4/18.
 */

public class CoursedetailActivity extends Activity {
    private String studentSum,SumPeriod,time,coursename,courseroom,teacher,zhoushu,mark,courseid,title,remark;
    private TextView tv_coursename,tv_courseroom,tv_period,tv_teacher,tv_time,tv_mark,tv_remark,tv_sum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetail);
        getdatafromlastAct();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar7);
        toolbar.setNavigationIcon(R.drawable.backlog);
        toolbar.setTitleMarginStart(1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursedetailActivity.this.finish();
            }
        });

        findview();
        setData();

    }


    private void getdatafromlastAct() {

       studentSum=getIntent().getStringExtra("studentSum");
       SumPeriod=getIntent().getStringExtra("SumPeriod");
        time=getIntent().getStringExtra("time");
        title=getIntent().getStringExtra("title");
        courseid=getIntent().getStringExtra("courseid");
        coursename=getIntent().getStringExtra("coursename");
        teacher=title+"  "+getIntent().getStringExtra("Classteacher");
        mark= getIntent().getStringExtra("coursemark");
        remark=getIntent().getStringExtra("remark");
        courseroom=getIntent().getStringExtra("Classroom");

    }

    private void findview() {
        tv_coursename=(TextView) findViewById(R.id.det_coursename);//课程名
        tv_courseroom=(TextView) findViewById(R.id.det_courseroom);
        tv_period=(TextView) findViewById(R.id.det_classperiod);//课时
        tv_teacher=(TextView) findViewById(R.id.det_teacher);
        tv_time=(TextView) findViewById(R.id.det_time);
        tv_mark=(TextView) findViewById(R.id.det_coursemark);//学分
        tv_sum=(TextView) findViewById(R.id.det_sum);
        tv_remark=(TextView) findViewById(R.id.det_remark);
    }
    private void setData() {
        tv_coursename.setText(coursename);//
        tv_courseroom.setText(courseroom);
        tv_period.setText(SumPeriod);//
        tv_teacher.setText(teacher);
        tv_time.setText(time);//
        tv_mark.setText(mark);
        tv_sum.setText(studentSum);
        tv_remark.setText(remark);
    }

}
