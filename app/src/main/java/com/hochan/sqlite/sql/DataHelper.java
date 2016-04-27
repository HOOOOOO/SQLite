package com.hochan.sqlite.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.fragment.SearchDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/12.
 */
public class DataHelper {

    //数据库名称
    private static String DB_NAME = "power_tower.db";
    //数据库版本
    private static int DB_VERSION = 3;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context){
        // context.deleteDatabase(DB_NAME);//删除数据库，版本从1开始
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public List<Worker> getWorkersInfo(){
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null, null, null, null, null,SearchDialogFragment.SEARCH_ORDERBY, null);
        cursor.moveToFirst();
        List<Worker> workers = new ArrayList<>();
        while (!cursor.isAfterLast()){
            Worker worker = new Worker(String.valueOf(cursor.getInt(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
                    workers.add(worker);
            cursor.moveToNext();
        }
        return workers;
    }

    public List<Worker> getWorkerByID(int id1, int id2) {
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null,
                SqliteHelper.ID + ">=" + id1 + " and " + SqliteHelper.ID + "<=" + id2,
                null, null, null, SearchDialogFragment.SEARCH_ORDERBY,null);
        cursor.moveToFirst();
        List<Worker> workers = new ArrayList<>();
        while (!cursor.isAfterLast()){
            Worker worker = new Worker(String.valueOf(cursor.getInt(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            workers.add(worker);
            cursor.moveToNext();
        }
        return workers;
    }

    public List<Worker> getWorkersByName(String name){
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null,
                SqliteHelper.NAME + " = \"" + name + "\"", null, null, null, SearchDialogFragment.SEARCH_ORDERBY,null);
        cursor.moveToFirst();
        List<Worker> workers = new ArrayList<>();
        while (!cursor.isAfterLast()){
            Worker worker = new Worker(String.valueOf(cursor.getInt(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            workers.add(worker);
            cursor.moveToNext();
        }
        return workers;
    }

    public List<Worker> getWorkersByTowerNumber(String towerNum){
        String[] search_tower_range = new String[]{towerNum,towerNum};
        if (towerNum.contains("-")){
            search_tower_range = towerNum.split("-");
        }
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null,
                SqliteHelper.TOWER_NUMBER + ">=" + search_tower_range[0] + " and " + SqliteHelper.TOWER_NUMBER + "<=" + search_tower_range[1],
                null, null, null,SearchDialogFragment.SEARCH_ORDERBY, null);
        cursor.moveToFirst();
        List<Worker> workers = new ArrayList<>();
        while (!cursor.isAfterLast()){
            Worker worker = new Worker(String.valueOf(cursor.getInt(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            workers.add(worker);
            cursor.moveToNext();
        }
        return workers;
    }

    public List<Worker> getWorkersByWorkState(String workstate){
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null, SqliteHelper.WORK_STATE + "=" + workstate,
                null, null, null,SearchDialogFragment.SEARCH_ORDERBY, null);
        cursor.moveToFirst();
        List<Worker> workers = new ArrayList<>();
        while (!cursor.isAfterLast()){
            Worker worker = new Worker(String.valueOf(cursor.getInt(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            workers.add(worker);
            cursor.moveToNext();
        }
        return workers;
    }

    //判断worker表中的是否包含某个ID的记录
    public Boolean HaveUserInfo(int id)
    {
        Boolean b=false;
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null, SqliteHelper.ID + "=" + id, null, null, null, null);
        b=cursor.moveToFirst();
        cursor.close();
        return b;
    }

    //判断worker表中的是否包含某个NAME的记录
    public Boolean HaveUserInfo(String name)
    {
        Boolean b=false;
        Cursor cursor=db.query(SqliteHelper.TB_NAME, null, SqliteHelper.NAME + "=" + name, null, null, null, null);
        b=cursor.moveToFirst();
        cursor.close();
        return b;
    }

    public Long addData(Worker worker){
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.NAME, worker.getmName());
        values.put(SqliteHelper.PHONE_NUMBER, worker.getmPhoneNumber());
        values.put(SqliteHelper.TOWER_NUMBER, worker.getmTowerNumber());
        values.put(SqliteHelper.WORK_STATE, worker.getmWorkState());
        //set time stamp;
        values.put(SqliteHelper.TIME_STAMP, getDateTime());
        Long result = db.insert(SqliteHelper.TB_NAME, "null", values);

        return result;
    }

    public int deleteByID(int id){
        int result = db.delete(SqliteHelper.TB_NAME, SqliteHelper.ID + "=" + id, null);

        return result;
    }

    public int deleteByName(String name){
        int result = db.delete(SqliteHelper.TB_NAME, SqliteHelper.NAME+ "=" + name, null);
        return result;
    }

    public int updateByID(int id, Worker worker){
        ContentValues values = new ContentValues();
        if(!TextUtils.isEmpty(worker.getmName())){
            values.put(SqliteHelper.NAME, worker.getmName());
        }
        if(!TextUtils.isEmpty(worker.getmPhoneNumber())){
            values.put(SqliteHelper.PHONE_NUMBER, worker.getmPhoneNumber());
        }
        if(!TextUtils.isEmpty(worker.getmTowerNumber())){
            values.put(SqliteHelper.TOWER_NUMBER, worker.getmTowerNumber());
        }
        if(!TextUtils.isEmpty(worker.getmWorkState())){
            values.put(SqliteHelper.WORK_STATE, worker.getmWorkState());
        }
        //set time stamp;
        values.put(SqliteHelper.TIME_STAMP,getDateTime());
        int result = db.update(SqliteHelper.TB_NAME, values, SqliteHelper.ID + "=" + id, null);

        return result;
    }

    public void clearTable(Context context){
        dbHelper.onUpgrade(db, DB_VERSION, DB_VERSION);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
