package com.hochan.sqlite.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public final static String TB_NAME = "worker";
    public final static String ID = "ID";
    public final static String NAME = "姓名";
    public final static String PHONE_NUMBER = "电话";
    public final static String TOWER_NUMBER = "电塔编号";
    public final static String WORK_STATE = "工作状况";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" +
                ID + " integer primary key autoincrement," +   //0
                NAME + " varchar," +                           //1
                PHONE_NUMBER + " varchar," +                   //2
                TOWER_NUMBER + " varchar," +                   //3
                WORK_STATE + " varchar" +                      //4
                ")");
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.TB_NAME);
        onCreate(db);
    }
}
