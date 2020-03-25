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

    public int findIndexWithId(ArrayList<Note> arrayListNote, int id){
        int index = -1;
        for(int i = 0 ; i < arrayListNote.size() ; i++){
            if(arrayListNote.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    public int findIndexWithId(ArrayList<Item> arrayListItem, String name){
        int index = -1;
        for(int i = 0 ; i < arrayListItem.size() ; i++){
            if(arrayListItem.get(i).getName().equals(name)){
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean checkContainsInArrayList(ArrayList<Item> arrayListItem, String name){
        for(int i = 0 ; i < arrayListItem.size() ; i++){
            if(arrayListItem.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}
