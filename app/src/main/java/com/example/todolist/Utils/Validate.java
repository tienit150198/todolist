package com.example.todolist.Utils;

import android.content.Context;

import com.example.todolist.Models.Item;
import com.example.todolist.Models.Note;

import java.util.ArrayList;


public class Validate {
    private Context mContext;
    private static Validate instance;

    private Validate(Context context){
        mContext = context;
    }

    public static Validate getInstance(Context context){
        if(instance == null){
            instance = new Validate(context);
        }
        return instance;
    }

    public boolean checkContainsInArrayList(ArrayList<Item> items, String name){
        for(int i = 0 ; i < items.size() ; i++){
            if(items.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public int findIndexWithId(ArrayList<String> listName, String name){
        int index = -1;
        for(int i = 0 ; i < listName.size() ; i++){
            if(listName.get(i).equals(name)){
                index = i;
                break;
            }
        }
        return index;
    }

    public int findIndexWithId(ArrayList<Note> notes, int id){ // spinner gì
        int index = -1;
        for(int i = 0 ; i < notes.size() ; i++){
            if(notes.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }
}
