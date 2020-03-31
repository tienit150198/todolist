package com.example.todolist.Models;

import android.graphics.Color;
import android.location.Location;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject{
    @PrimaryKey
    private int id;
    private String content;
    private int color;
    private boolean isChecked;
    private int index;
    private String nameSublist;
    private Date dateCreate;
    public Note(){}

    public Note(int id, String content, int index) {
        this.id = id;
        this.content = content;
        this.index = index;
        this.color = 0;
    }

    public Note(int id, String content, int index, String nameSublist) {
        this.id = id;
        this.content = content;
        this.index = index;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNameSublist() {
        return nameSublist;
    }

    public void setNameSublist(String nameSublist) {
        this.nameSublist = nameSublist;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", color=" + color +
                ", isChecked=" + isChecked +
                ", index=" + index +
                ", nameSublist='" + nameSublist + '\'' +
                ", dateCreate=" + dateCreate +
                '}';
    }
}
