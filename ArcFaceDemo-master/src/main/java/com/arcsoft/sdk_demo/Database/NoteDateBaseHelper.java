package com.arcsoft.sdk_demo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PC on 2018/2/3.
 */

public class NoteDateBaseHelper extends SQLiteOpenHelper
{

    public NoteDateBaseHelper(Context context)
    {
        super(context,"note",null,1);
    }
    public static final String CreateNote="create table note("+"id integer primary key autoincrement,"
            +"content text ,"+"date text)";


    public NoteDateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateNote);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
