package com.example.todolist.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todolist.Constants.Constants;
import com.example.todolist.R;

@Entity(tableName = Constants.KEY_TABLE_NAME_ITEM)
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int icon;
    private String name;
    public Item() {
    }
    @Ignore
    public Item(String name) {
        this.name = name;
        this.icon = R.drawable.sublist;
    }
    @Ignore
    public Item(int image, String name) {
        this.icon = image;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
