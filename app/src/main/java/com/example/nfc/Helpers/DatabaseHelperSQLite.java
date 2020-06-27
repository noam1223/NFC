package com.example.nfc.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelperSQLite extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelperSQLite";

    private static final String TABLE_NAME = "Temperature_table";
    private static final String DATABASE_NAME = "Temperature.db";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "temp";
    private static final String COL_3 = "time";


    public DatabaseHelperSQLite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, temp TEXT, time TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addData(String temp, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, temp);
        contentValues.put(COL_3, time);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == - 1){
            return false;
        } else return true;
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);

    }


    public static String getCol2() {
        return COL_2;
    }

    public static String getCol3() {
        return COL_3;
    }
}
