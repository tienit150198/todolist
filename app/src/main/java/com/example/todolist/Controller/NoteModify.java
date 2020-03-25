package com.example.todolist.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.example.todolist.Database.DatabaseHandler;
import com.example.todolist.Models.Note;
import com.example.todolist.Utils.DateUtility;

import java.util.ArrayList;

public class NoteModify {
    private static NoteModify instance;
    private DateUtility instanceDate;
    private SQLiteDatabase db;
    private DatabaseHandler dbHelper;

    private NoteModify(Context context) {
        this.dbHelper = new DatabaseHandler(context);
        this.db = dbHelper.getWritableDatabase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instanceDate = DateUtility.getInstance(context);
        }
    }

    public static NoteModify getInstance(Context context) {
        if (instance == null) {
            instance = new NoteModify(context);
        }
        return instance;
    }

    public int insertNote(Note note, int index, String sublist) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_CONTENT, note.getContent());
        values.put(DatabaseHandler.KEY_COLOR, note.getColor());
        values.put(DatabaseHandler.KEY_COMPLETED, note.isChecked());
        values.put(DatabaseHandler.KEY_INDEX, index);
        values.put(DatabaseHandler.KEY_NAME_SUB_LIST, sublist);

        String dateString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = instanceDate.convertLocalDateToString(note.getDateCreate());
        }
        values.put(DatabaseHandler.KEY_DATE_CREATE, dateString);
        return (int) db.insert(DatabaseHandler.TABLE_NAME, null, values);
    }

    public int updateNote(int id, Note note) {
        ContentValues values = new ContentValues();
        String selection = DatabaseHandler.KEY_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id)};

        values.put(DatabaseHandler.KEY_CONTENT, note.getContent());
        values.put(DatabaseHandler.KEY_COLOR, note.getColor());
        values.put(DatabaseHandler.KEY_COMPLETED, note.isChecked());

        String dateString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = instanceDate.convertLocalDateToString(note.getDateCreate());
        }
        values.put(DatabaseHandler.KEY_DATE_CREATE, dateString);

        return db.update(DatabaseHandler.TABLE_NAME, values, selection, selectionArgs);
    }

    public int updateNote(int id, Note note, String sublist) {
        ContentValues values = new ContentValues();
        String selection = DatabaseHandler.KEY_ID + "= ? AND " + DatabaseHandler.KEY_NAME_SUB_LIST + " = ?";
        String[] selectionArgs = {String.valueOf(id), sublist};

        values.put(DatabaseHandler.KEY_CONTENT, note.getContent());
        values.put(DatabaseHandler.KEY_COLOR, note.getColor());
        values.put(DatabaseHandler.KEY_COMPLETED, note.isChecked());

        String dateString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = instanceDate.convertLocalDateToString(note.getDateCreate());
        }
        values.put(DatabaseHandler.KEY_DATE_CREATE, dateString);

        return db.update(DatabaseHandler.TABLE_NAME, values, selection, selectionArgs);
    }

    public boolean deleteAll() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.delete(DatabaseHandler.TABLE_NAME, null, null) > 0;
    }

    public boolean deleteAll(String sublist) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = DatabaseHandler.KEY_NAME_SUB_LIST + " = ?";
        String[] selectionArgs = {sublist};
        return database.delete(DatabaseHandler.TABLE_NAME, selection, selectionArgs) > 0;
    }

    public boolean deleteNote(int id, String sublist) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = DatabaseHandler.KEY_ID + " = ? AND " + DatabaseHandler.KEY_NAME_SUB_LIST + " = ?";
        String[] selectionArgs = {String.valueOf(id), sublist};

        return database.delete(DatabaseHandler.TABLE_NAME, selection, selectionArgs) > 0;
    }

    public ArrayList<Note> queryAllData() {
        ArrayList<Note> result = new ArrayList<>();
        Cursor mCursor = db.query(DatabaseHandler.TABLE_NAME, null, null, null, null, null, DatabaseHandler.KEY_INDEX + " ASC");


        // convert cursor to arraylist
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            int id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_ID));
            String content = mCursor.getString(mCursor.getColumnIndex(DatabaseHandler.KEY_CONTENT));
            int color = mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_COLOR));
            boolean isChecked = mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_COMPLETED)) > 0;

            Note note = makeNote(id, content, color, isChecked);
            result.add(note);
        }
        mCursor.close();
        return result;
    }

    public ArrayList<Note> queryAllData(String nameSublist) {
        ArrayList<Note> result = new ArrayList<>();
        Cursor mCursor = db.query(DatabaseHandler.TABLE_NAME, null, DatabaseHandler.KEY_NAME_SUB_LIST + " = ?", new String[]{nameSublist}, null, null, DatabaseHandler.KEY_INDEX + " ASC");

        // convert cursor to arraylist
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            int id = mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_ID));
            String content = mCursor.getString(mCursor.getColumnIndex(DatabaseHandler.KEY_CONTENT));
            int color = mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_COLOR));
            boolean isChecked = mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_COMPLETED)) > 0;

            Note note = makeNote(id, content, color, isChecked);
            result.add(note);
        }
        mCursor.close();
        return result;
    }

    public ArrayList<String> queryAllSublist() {
        ArrayList<String> result = new ArrayList<>();
        Cursor mCursor = db.query(DatabaseHandler.TABLE_NAME, new String[]{DatabaseHandler.KEY_NAME_SUB_LIST}, null, null, null, null, DatabaseHandler.KEY_DATE_CREATE + " ASC");

        // convert cursor to arraylist
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            String subList = mCursor.getString(mCursor.getColumnIndex(DatabaseHandler.KEY_NAME_SUB_LIST));
            result.add(subList);
        }
        mCursor.close();
        return result;
    }

    public boolean changeIndexWhenMove(int fromIndex, int toIndex, int fromIndex_id, int toIndex_id, boolean isDownward, String sublist) {
        String sqlUpdate;
        String sqlIncrease;

        //
        if (isDownward) {
            int indexTemp = toIndex;

            int tempSwap = toIndex;
            toIndex = fromIndex;
            fromIndex = tempSwap;

            sqlUpdate = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + indexTemp +
                    " WHERE " + DatabaseHandler.KEY_ID + " = " + fromIndex_id + " AND " + DatabaseHandler.KEY_NAME_SUB_LIST + " = '" + sublist + "'";
            sqlIncrease = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + DatabaseHandler.KEY_INDEX +
                    " - 1 WHERE " + DatabaseHandler.KEY_INDEX + " BETWEEN " + toIndex + " AND " + fromIndex + " AND " + DatabaseHandler.KEY_NAME_SUB_LIST + " = '" + sublist + "'";
        } else {
            sqlUpdate = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + toIndex +
                    " WHERE " + DatabaseHandler.KEY_ID + " = " + fromIndex_id + " AND " + DatabaseHandler.KEY_NAME_SUB_LIST + " = '" + sublist + "'";
            sqlIncrease = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + DatabaseHandler.KEY_INDEX + " + 1 WHERE "
                    + DatabaseHandler.KEY_INDEX + " BETWEEN " + toIndex + " AND " + fromIndex + " AND " + DatabaseHandler.KEY_NAME_SUB_LIST + " = '" + sublist + "'";
        }

        db.execSQL(sqlIncrease);
        db.execSQL(sqlUpdate);
        return true;
    }

    private Note makeNote(int id, String content, int color, boolean isChecked) {
        return new Note(id, content, color, isChecked);
    }
}
