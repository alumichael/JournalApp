package com.example.mike4christ.journalapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by delaroy on 10/25/17.
 */

public class DiaryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "diary.db";

    private static final int DATABASE_VERSION = 3;

    public DiaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the reminder table
        String SQL_CREATE_DIARY_TABLE =  "CREATE TABLE " + DiaryContract.DiaryEntry.TABLE_NAME + " ("
                + DiaryContract.DiaryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DiaryContract.DiaryEntry.KEY_TITLE + " TEXT, "
                + DiaryContract.DiaryEntry.KEY_DESCRIPTION + " TEXT" + " );";



        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_DIARY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
