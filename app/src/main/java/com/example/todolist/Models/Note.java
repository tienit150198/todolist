package com.example.todolist.Models;

import android.os.Build;

import com.example.todolist.MainActivity;
import com.example.todolist.Utils.DateUtility;

import java.time.LocalDate;

public class Note {
    private int id;
    private String content;
    private int color;
    private boolean isChecked;
    private boolean isEdited;
    private LocalDate dateCreate;

    public Note() {

    }

    public Note(String content) {
        this.content = content;

        this.isChecked = false;
        this.isEdited = false;
        this.color = 0;
    }

    public Note(int id, String content, int color, boolean isChecked) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.isChecked = isChecked;
        this.isEdited = false;
    }

    public Note(int id, String content, int color, boolean isChecked, LocalDate dateCreate) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.isChecked = isChecked;
        this.dateCreate = dateCreate;
        this.isEdited = false;
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

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public int getId() {
        return id;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
