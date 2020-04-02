package com.example.todolist.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.Constants.Constants;
import com.example.todolist.Models.Item;

import java.util.List;

@Dao
public abstract class ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertItem(Item item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateItem(Item item);

    @Delete
    public abstract void deleteItem(Item item);

    @Query("DELETE FROM " + Constants.KEY_TABLE_NAME_ITEM)
    public abstract void deleteAll();

    @Query("SELECT * FROM " + Constants.KEY_TABLE_NAME_ITEM)
    public abstract List<Item> queryAll();

    @Query("SELECT " + Constants.KEY_NAME_ITEM + " FROM " + Constants.KEY_TABLE_NAME_ITEM)
    public abstract List<String> queryAllName();

    @Query("SELECT * FROM " + Constants.KEY_TABLE_NAME_ITEM + " WHERE " + Constants.KEY_ID_ITEM + " = :id")
    public abstract Item queryItem(int id);

}
