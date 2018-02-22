package com.arcsoft.sdk_demo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.arcsoft.sdk_demo.Database.NoteDateBaseHelper;
import com.arcsoft.sdk_demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    private Button addNote;
    private TextView tv_content;
    private NoteDateBaseHelper DbHelper;
    private SQLiteDatabase DB;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        InitView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        RefreshNoteList();
    }

    public void InitView() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        listView = (ListView) findViewById(R.id.listview);
        dataList = new ArrayList<>();
        addNote = (Button) findViewById(R.id.btn_editnote);
        DbHelper = new NoteDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NoteActivity.this,NoteEdit.class);
                //愈发感觉这个就是捆绑起来，然后传递！
                Bundle bundle=new Bundle();
                bundle.putString("info","");
                bundle.putInt("enter_state",0);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }

    //刷新listview
    public void RefreshNoteList()
    {
        //如果已经有内容。全部删掉
        //并且更新
        int size=dataList.size();
        if(size>0)
        {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }
        //从数据库读取信息
        Cursor cursor=DB.query("note",null,null,null,null,null,null);
        startManagingCursor(cursor);
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex("content"));
            String date=cursor.getString(cursor.getColumnIndex("date"));
            Map<String ,Object>map=new HashMap<>();
            map.put("tv_content",name);
            map.put("tv_date",date);
            dataList.add(map);
        }
        simple_adapter=new SimpleAdapter(this,dataList, R.layout.item1,new String[]{"tv_content","tv_date"},new int[]{R.id.tv_content, R.id.tv_date});
        listView.setAdapter(simple_adapter);
    }

    //点击某项监听事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        //获取listview中此个item中的内容
        String content=listView.getItemAtPosition(position)+"";
        String content1=content.substring(content.indexOf("=")+1,content.indexOf(","));
        Intent myIntent=new Intent(NoteActivity.this,NoteEdit.class);
        Bundle bundle=new Bundle();
        bundle.putString("info",content1);
        bundle.putInt("enter_state",1);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    //点击listview中某一项长时间的点击事件

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("删除");
        builder.setMessage("确定");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //获取listv中此个item的内容
                //删除该行后刷新
                String content=listView.getItemAtPosition(position)+"";
                String content1=content.substring(content.indexOf("=")+1,content.indexOf(","));
                DB.delete("note","content=?",new String[]{content1});
                RefreshNoteList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
        return true;

    }
}
