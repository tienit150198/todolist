package com.example.todolist.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "note";
    public static final String KEY_ID = "id";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_COLOR = "color";
    public static final String KEY_INDEX = "_index";
    public static final String KEY_DATE_CREATE = "date_create";
    public static final String KEY_NAME_SUB_LIST = "name_sub_list";
    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTENT + " TEXT," + KEY_NAME_SUB_LIST + " TEXT,"
                    + KEY_COMPLETED + " BIT DEFAULT 0, " + KEY_COLOR + " INTEGER DEFAULT 0, " + KEY_INDEX + " INTEGER, " +
                     KEY_DATE_CREATE + " TEXT" + ")";
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }
}
