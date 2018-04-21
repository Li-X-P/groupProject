package com.example.groupproject;

/**
 * Created by baijing on 19/4/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CreateUser.db";
    public static final String TABLE_NAME = "CreateUser_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "QUESTION";
    public static final String COL_5 = "ANSWER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // super(context, name, factor, version)
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, QUESTION TEXT, ANSWER TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //insert account to the database 只保存帳戶、密碼、安全問題和答案
    public boolean insertData(String account, String password, String question, String answer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,account);
        contentValues.put(COL_3,password);
        contentValues.put(COL_4,question);
        contentValues.put(COL_5,answer);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
    public Cursor getData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from CreateUser_table where USERNAME = ?",new String[]{name});
        return res;
    }
    // Method to update a record
    public boolean updateData(String account, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,password);
        db.update(TABLE_NAME, contentValues, "USERNAME = ?", new String[] {account});
        return true;
    }
    // Method to delete a record
    public Integer deleteData (String account) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "USERNAME = ?", new String[] {account});
    }

}

