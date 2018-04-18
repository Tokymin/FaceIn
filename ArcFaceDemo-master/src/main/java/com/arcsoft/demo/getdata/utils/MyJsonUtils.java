package com.arcsoft.demo.getdata.utils;

import com.arcsoft.demo.View.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyJsonUtils{
	
	public List<Course> parseJson(String jsonStr){
		List<Course> list=new ArrayList<Course>();
		
		try {
			JSONObject obj1=new JSONObject(jsonStr);
			JSONArray array=obj1.getJSONArray("list");
			
			for(int i=0;i<array.length();i++){
				Course course=new Course();
				
				JSONObject obj2=array.optJSONObject(i);
				
				int jieci=obj2.optInt("jieci");//
				String coursename=obj2.optString("coursename");
				String classroomname=obj2.optString("courseaddress");
				String teacher=obj2.optString("teacher");

				course.setClassRoomName(classroomname);
				course.setJieci(jieci);
				course.setCoursename(coursename);
				course.setClassteacher(teacher);
				list.add(course);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
