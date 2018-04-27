package com.arcsoft.demo.View;


import java.io.Serializable;

public class Course implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9121734039844677432L;
    private String studentSum;//总人数
    private String SumPeriod;////总课时
    private String time;//详细时间
    private String title;//教师职称
    private String courseid;//课程id
    private String coursename;//课程名
    private int jieci;//节次
    private int day;//那一天
    private String des;//详情信息
    private String remark;//备注
    private int spanNum = 2;
    private String ClassRoomName;//教室
    private String coursemark;//学分
    private String Classteacher;//老师
    private String xuankeID;//选课ID
    private String fullTime;
    public Course(int jieci, int day, String des) {
        this.jieci = jieci;
        this.day = day;
        this.des = des;
    }
    public Course(int jieci, int day, String des,String studentSum, String SumPeriod, String time, String title, String courseid, String coursename, String Classteacher, String coursemark, String remark,String classRoomName) {
        this(jieci,day,des);
        this.studentSum=studentSum;
        this.SumPeriod=SumPeriod;
        this.time=time;
        this.title=title;
        this.courseid=courseid;
        this.coursename=coursename;
        this.remark = remark;
        this.Classteacher=Classteacher;
        this.ClassRoomName= classRoomName;
        this.coursemark=coursemark;

    }
    public Course(int jieci, int day, String des,String studentSum, String SumPeriod, String time, String title, String courseid, String coursename, String Classteacher, String coursemark, String remark,String classRoomName,String xuankeID,String fullTime) {
        this(jieci,day,des);
        this.studentSum=studentSum;
        this.SumPeriod=SumPeriod;
        this.time=time;
        this.title=title;
        this.courseid=courseid;
        this.coursename=coursename;
        this.remark = remark;
        this.Classteacher=Classteacher;
        this.ClassRoomName= classRoomName;
        this.coursemark=coursemark;
        this.xuankeID=xuankeID;
        this.fullTime=fullTime;
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

    public String getCoursemark() {
        return coursemark;
    }

    public void setCoursemark(String coursemark) {
        this.coursemark = coursemark;
    }

    public String getStudentSum() {
        return studentSum;
    }

    public void setStudentSum(String studentSum) {
        this.studentSum = studentSum;
    }

    public String getSumPeriod() {
        return SumPeriod;
    }

    public void setSumPeriod(String sumPeriod) {
        SumPeriod = sumPeriod;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getXuankeID() {
        return xuankeID;
    }

    public void setXuankeID(String xuankeID) {
        this.xuankeID = xuankeID;
    }

    public String getFullTime() {
        return fullTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }
}
