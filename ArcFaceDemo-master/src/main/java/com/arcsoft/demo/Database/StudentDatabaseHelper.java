package com.arcsoft.demo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by localhost on 2017/10/10.
 */

public class StudentDatabaseHelper extends SQLiteOpenHelper
{
    public  static final String sql="create table student("+"id integer primary key autoincrement,"+"clas text,"+"name text,"+" sex text,"+"tele text,"+"choice)";
    private Context mContext;
    public StudentDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
