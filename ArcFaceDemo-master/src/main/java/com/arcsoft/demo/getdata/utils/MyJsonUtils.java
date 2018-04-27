package com.arcsoft.demo.getdata.utils;

import android.util.Log;

import com.arcsoft.demo.View.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//解析json的工具类
public class MyJsonUtils{
	public List<Course> parseJson(String jsonStr){
		List<Course> list=new ArrayList<Course>();
		try {
			JSONArray array=new JSONArray(jsonStr);
			for(int i=0;i<array.length();i++){
				JSONObject obj2=array.optJSONObject(i);
				String courseId=obj2.optString("cId");//课程的ID
				String sum = obj2.optString("time");//
				int jieci = Integer.valueOf(sum.substring(3,4));//节次
				int tianshu = Integer.valueOf(sum.substring(2,3));//天数
				String coursename = obj2.optString("cName");//课程名
				String studentSum = obj2.optString("cSum");//选课人数
				String SumPeriod = obj2.optString("cSumPeriod");//课时
				String time=obj2.optString("cTime");//上课的周数
				String title = obj2.optString("t_title");//教师职称
				String teacher = obj2.optString("t_name");//老师
				String coursemark = obj2.optString("cCredit");//学分
				String classroomname = obj2.optString("cAddress");//教室
				String remark = obj2.optString("cRemark");//备注
				String xuankeID=obj2.optString("cChooseId");//选课ID
				Log.e("TABLE","我真的不知道该::"+coursename+":"+classroomname+teacher+coursemark);

				Course course=new Course(jieci,tianshu,coursename+'\n'+classroomname+'\n'+teacher,studentSum,SumPeriod,time,title,courseId,coursename,teacher,coursemark,remark,classroomname,xuankeID,sum);

				course.setCourseid(courseId);
				course.setJieci(jieci);
				course.setDes(coursename+'\n'+classroomname+'\n'+teacher);
				course.setDay(tianshu);
				list.add(course);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
