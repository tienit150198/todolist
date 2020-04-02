package com.example.todolist.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todolist.Constants.Constants;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = Constants.KEY_TABLE_NAME_NOTE)
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private int color;
    private boolean isChecked;
    private String nameSublist;
    @Ignore
    private Date createAt;
    public Note() {
    }

    @Ignore
    public Note(String content, String nameSublist) {
        this.content = content;
        this.nameSublist = nameSublist;
        this.color = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getNameSublist() {
        return nameSublist;
    }

    public void setNameSublist(String nameSublist) {
        this.nameSublist = nameSublist;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", color=" + color +
                ", isChecked=" + isChecked +
                ", nameSublist='" + nameSublist + '\'' +
                '}';
    }
}
