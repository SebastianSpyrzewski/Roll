package com.example.roll;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class databaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HighScores.db";
    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table adventure (id integer primary key autoincrement, level integer, miliseconds integer)");
        db.execSQL("create table random (id integer primary key autoincrement, level integer, miliseconds integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists adventure");
        db.execSQL("drop table if exists random");
        onCreate(db);
    }
}
