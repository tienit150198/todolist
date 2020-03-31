package com.example.todolist.Models;

import com.example.todolist.R;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Item extends RealmObject {
    int icon;
    @Required
    String name;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
        this.icon = R.drawable.sublist;
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
