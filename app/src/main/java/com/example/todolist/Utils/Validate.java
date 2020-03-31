package com.example.todolist.Utils;

import android.content.Context;

import com.example.todolist.Models.Item;
import com.example.todolist.Models.Note;

import java.util.ArrayList;

import io.realm.RealmResults;

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

    public int findIndexWithId(RealmResults<Item> realmResults, String name){
        int index = -1;
        for(int i = 0 ; i < realmResults.size() ; i++){
            if(realmResults.get(i).getName().equals(name)){
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean checkContainsInArrayList(RealmResults<Item> realmResults, String name){
        for(int i = 0 ; i < realmResults.size() ; i++){
            if(realmResults.get(i).getName().equals(name)){
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
}
