package com.arcsoft.sdk_demo.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.sdk_demo.Database.NoteDateBaseHelper;
import com.arcsoft.sdk_demo.R;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PC on 2018/2/3.
 */

public class NoteEdit extends Activity implements View.OnClickListener
{
    private TextView tv_date;
    private EditText et_content;
    private Button btn_ok;
    private Button btn_cancel;
    private NoteDateBaseHelper DBHelper;
    public int enter_state=0;//用来区分是新建一个note还是更改原来的note
    public String last_content;//用来获取edittext内容

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
       InitView();

    }

    private void InitView()
    {
        tv_date=(TextView)findViewById(R.id.tv_date);
        et_content=(EditText)findViewById(R.id.et_content);
        btn_ok=(Button)findViewById(R.id.btn_ok);
        btn_cancel=(Button)findViewById(R.id.btn_cancle);
        DBHelper=new NoteDateBaseHelper(this);

        //获取该时刻时间
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String dataString=simpleDateFormat.format(date);
        tv_date.setText(dataString);

        //接收内容和ID
        Bundle myBundle=this.getIntent().getExtras();
        last_content=myBundle.getString("info");
        enter_state=myBundle.getInt("enter_state");
        et_content.setText(last_content);

        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_ok:
                SQLiteDatabase db=DBHelper.getReadableDatabase();
                //获取edittext内容
                String content=et_content.getText().toString();

                //添加一个新的日志
                if(enter_state==0)
                {
                    if(!content.equals(""))
                    {
                        //获取此时此刻的时间
                        Date date=new Date();
                        SimpleDateFormat simpleDateForma=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString=simpleDateForma.format(date);

                        //向数据库添加信息
                        ContentValues values=new ContentValues();
                        values.put("content",content);
                        values.put("date",dateString);
                        db.insert("note",null,values);
                        finish();

                    }else {
                        Toast.makeText(this,"请输入您的内容",Toast.LENGTH_SHORT).show();

                    }

                }
                //查看并修改一个已有的日志
                else{
                    ContentValues values=new ContentValues();
                    values.put("content",content);
                    db.update("note",values,"content= ?",new String[]{last_content});
                    finish();
                }
                break;
            case R.id.btn_cancle:
                finish();
                break;

        }
    }
}
