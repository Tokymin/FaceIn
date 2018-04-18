package com.arcsoft.demo.View;


import java.io.Serializable;

public class Course implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9121734039844677432L;
    private String courseid;//课程id
    private String coursename;//课程名
    private int jieci;//节次
    private int day;//那一天
    private String des;//详情信息
    private int spanNum = 2;
    private String ClassRoomName;//教室
    private String Classteacher;//老师

    public Course(int jieci, int day, String des) {
        this.jieci = jieci;
        this.day = day;
        this.des = des;
    }

    public Course() {
    }

    public int getJieci() {
        return jieci;
    }

    public void setJieci(int jieci) {
        this.jieci = jieci;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getSpanNum() {
        return spanNum;
    }

    public void setSpanNum(int spanNum) {
        this.spanNum = spanNum;
    }

    @Override
    public String toString() {
        return "Course [jieci=" + jieci + ", day=" + day + ", des=" + des
                + ", spanNun=" + spanNum + "]";
    }

    public String getClassRoomName() {
        return ClassRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        ClassRoomName = classRoomName;
    }

    public String getClassteacher() {
        return Classteacher;
    }

    public void setClassteacher(String classteacher) {
        Classteacher = classteacher;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        Classteacher = coursename;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String Courseid) {
        courseid = Courseid;
    }
}
