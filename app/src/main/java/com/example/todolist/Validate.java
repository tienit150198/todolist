package com.example.todolist;

import java.util.ArrayList;

public class Validate {
    public static int findIndexWithId(ArrayList<Note> arrayListNote, int id){
        int index = -1;
        for(int i = 0 ; i < arrayListNote.size() ; i++){
            if(arrayListNote.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }
}
