package com.example.todolist.Models;

import java.io.Serializable;

public class Item implements Serializable {
    int icon;
    String name;

    public Item(String name) {
        this.name = name;
    }

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
}
