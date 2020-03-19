package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoteModify {
    private static NoteModify instance;
    private SQLiteDatabase db;
    private DatabaseHandler dbHelper;

    private NoteModify(Context context) {
        this.dbHelper = new DatabaseHandler(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public static NoteModify getInstance(Context context) {
        if (instance == null) {
            instance = new NoteModify(context);
        }
        return instance;
    }

    int insertNote(Note note, int index) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_CONTENT, note.getContent());
        values.put(DatabaseHandler.KEY_COLOR, note.getColor());
        values.put(DatabaseHandler.KEY_COMPLETED, note.isChecked());
        values.put(DatabaseHandler.KEY_INDEX, index);

        String dateString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = DateUtility.convertLocalDateToString(note.getDateCreate());
        }
        values.put(DatabaseHandler.KEY_DATE_CREATE, dateString);
        return (int) db.insert(DatabaseHandler.TABLE_NAME, null, values);
    }

    int updateNote(int id, Note note) {
        ContentValues values = new ContentValues();
        String selection = DatabaseHandler.KEY_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id)};

        values.put(DatabaseHandler.KEY_CONTENT, note.getContent());
        values.put(DatabaseHandler.KEY_COLOR, note.getColor());
        values.put(DatabaseHandler.KEY_COMPLETED, note.isChecked());

        String dateString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = DateUtility.convertLocalDateToString(note.getDateCreate());
        }
        values.put(DatabaseHandler.KEY_DATE_CREATE, dateString);

        return db.update(DatabaseHandler.TABLE_NAME, values, selection, selectionArgs);
    }

    void deleteAll() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.delete(DatabaseHandler.TABLE_NAME, null, null);
    }

    void deleteNote(int id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = DatabaseHandler.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int change = database.delete(DatabaseHandler.TABLE_NAME, selection, selectionArgs);
    }

    ArrayList<Note> queryAllData() {
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

    boolean changeIndexWhenMove(int fromIndex, int toIndex, int fromIndex_id, int toIndex_id, boolean isDownward) {
        String sqlUpdate = "";
        String sqlIncrease = "";

        //
        if (isDownward) {
            int indexTemp = toIndex;

            int tempSwap = indexTemp;
            toIndex = fromIndex;
            fromIndex = tempSwap;

            sqlUpdate = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + indexTemp + " WHERE " + DatabaseHandler.KEY_ID + " = " + fromIndex_id;
            sqlIncrease = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + DatabaseHandler.KEY_INDEX + " - 1 WHERE "
                    + DatabaseHandler.KEY_INDEX + " BETWEEN " + toIndex + " AND " + fromIndex;
        } else {
            sqlUpdate = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + toIndex + " WHERE " + DatabaseHandler.KEY_ID + " = " + fromIndex_id;
            sqlIncrease = "UPDATE " + DatabaseHandler.TABLE_NAME + " SET " + DatabaseHandler.KEY_INDEX + " = " + DatabaseHandler.KEY_INDEX + " + 1 WHERE "
                    + DatabaseHandler.KEY_INDEX + " BETWEEN " + toIndex + " AND " + fromIndex;
        }

        db.execSQL(sqlIncrease);
        db.execSQL(sqlUpdate);
        return true;
    }

    private Note makeNote(int id, String content, int color, boolean isChecked) {
        return new Note(id, content, color, isChecked);
    }
}
