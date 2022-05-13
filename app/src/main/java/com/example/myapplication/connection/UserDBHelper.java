package com.example.myapplication.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "carshowroom";
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID_NAME = "_user_id";
    public static final String COLUMN_LOGIN_NAME = "user_login";
    public static final String COLUMN_PASSWORD_NAME = "user_password";

    public UserDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" + COLUMN_ID_NAME + " integer primary key," +
                COLUMN_LOGIN_NAME + " text," + COLUMN_PASSWORD_NAME + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
