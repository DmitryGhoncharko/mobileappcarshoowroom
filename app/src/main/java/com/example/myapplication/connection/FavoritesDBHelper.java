package com.example.myapplication.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavoritesDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "carshowroom";
    public static final String TABLE_NAME = "favorite_car";
    public static final String COLUMN_ID_NAME = "_favorite_car_id";
    public static final String COLUMN_USER_ID_NAME = "user_id";
    public static final String COLUMN_CAR_ID_NAME = "car_id";

    public FavoritesDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" + COLUMN_ID_NAME + " integer primary key," +
                COLUMN_USER_ID_NAME + " integer," + COLUMN_CAR_ID_NAME + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
