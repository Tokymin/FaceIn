package com.arcsoft.sdk_demo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.arcsoft.sdk_demo.Adapter.MyAdapter;
import com.arcsoft.sdk_demo.R;

import java.util.ArrayList;
import java.util.List;

public class ListView1 extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private MyAdapter myAdapter;
    private List<String> groupList;
    private List<List<String>> childList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        initView();
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expendablelistview);
        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        addData("行列式",new String[]{"1 二阶与三阶行列式","2 全排列及其逆序数","3 n阶行列式的定义","4 对换","5 行列式的性质","6 行列式按行（列）展开","7 克拉默法则",});
        addData("矩阵及其运算",new String[]{"1 矩阵","2 矩阵的运算","3 逆矩阵","4 矩阵分块法"});
        addData("矩阵的初等运算及线性方程组",new String[]{"1 矩阵的初等变换","2 矩阵的秩","3 线性方程组的解"});
        addData("向量组的线性相关性",new String[]{"1 向量组及其线性组合","2 向量组的线性相关性","3 向量组的秩","4 线性方程组的解的结构","5 向量空间"});
        addData("相似矩阵及其二次型",new String[]{"1 向量的内积、长度及正交性","2 方阵的特征值与特征向量","3 相似矩阵","4 对称矩阵的对角化","5 二次型及其标准型","6 用配方法化二次型成标准型","7 正定二次型",});
        addData("线性空间与线性变换",new String[]{"1 线性空间的定义与性质","2 维数、基与坐标","3 基变换与坐标变换","4 线性变换","5 线性变换的矩阵表示式"});
        myAdapter = new MyAdapter(this,groupList,childList);
        expandableListView.setAdapter(myAdapter);

    }

    /**
     * 用来添加数据的方法
     */
    private void addData(String group, String[] friend) {
        groupList.add(group);
        //每一个item打开又是一个不同的list集合
        List<String> childitem = new ArrayList<>();
        for (int i = 0; i < friend.length; i++) {
            childitem.add(friend[i]);
        }
        childList.add(childitem);
    }
}