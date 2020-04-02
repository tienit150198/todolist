package com.example.todolist.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.Constants.Constants;
import com.example.todolist.Models.Note;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertNote(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract int updateNote(Note note);

    @Delete
    public abstract void deleteNote(Note note);

    @Query("DELETE FROM " + Constants.KEY_TABLE_NAME_NOTE)
    public abstract void deleteAll();

    @Query("DELETE FROM " + Constants.KEY_TABLE_NAME_NOTE + " WHERE " + Constants.KEY_SUBLIST_NOTE + " = :sublist")
    public abstract void deleteSublist(String sublist);

    @Query("SELECT * FROM " + Constants.KEY_TABLE_NAME_NOTE)
    public abstract List<Note> queryAll();

    @Query("SELECT * FROM " + Constants.KEY_TABLE_NAME_NOTE + " WHERE " + Constants.KEY_SUBLIST_NOTE + " = :sublist")
    public abstract List<Note> querySublist(String sublist);

    @Query("SELECT * FROM " + Constants.KEY_TABLE_NAME_NOTE + " WHERE " + Constants.KEY_ID_NOTE + " = :id")
    public abstract Note queryNote(int id);
}
