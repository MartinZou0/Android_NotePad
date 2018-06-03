package com.sueword.notepad.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DB_NAME="MYNOTE_DB";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes(id INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(20),body varchar(150),date VARCHAR(50),tagcolor varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
