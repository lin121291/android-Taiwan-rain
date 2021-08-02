package com.example.listpop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 繼承sql接口
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String NOTEPAD_DB = "notepad";
    public static final String COST_TITLE = "cost_title";
    public static final String COST_DATE = "cost_date";
    public static final String COST_MONEY = "cost_money";

    public DataBaseHelper(Context context){
        // 創建數據庫
        super(context, NOTEPAD_DB,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 創建表格
        db.execSQL("create table if not exists notepad(" +
                "id integer primary key," +
                COST_TITLE + " varchar," +
                COST_DATE + " varchar," +
                COST_MONEY + " varchar)");
    }

    // 插入數據
    public void insert(CostBean bean){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COST_TITLE,bean.costTitle);
        cv.put(COST_DATE,bean.costDate);
        cv.put(COST_MONEY,bean.time);

        db.insert(NOTEPAD_DB,null,cv);
    }

    // 查詢
    public Cursor getAllCursorDate(){
        SQLiteDatabase db = getWritableDatabase();
        return db.query(NOTEPAD_DB,null,null,null,null,null, COST_DATE + " ASC");
    }

    // 刪除所有數據
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOTEPAD_DB,null,null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
