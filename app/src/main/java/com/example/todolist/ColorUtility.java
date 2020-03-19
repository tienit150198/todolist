package com.example.todolist;

import android.graphics.Color;

import java.util.ArrayList;

public class ColorUtility {
    public static ArrayList<Integer> createColor(){
        ArrayList<Integer> colorArraylist = new ArrayList<>();
        colorArraylist.add(Color.WHITE);
        colorArraylist.add(Color.LTGRAY);
        colorArraylist.add(Color.CYAN);
        colorArraylist.add(Color.GREEN);
        colorArraylist.add(Color.MAGENTA);
        colorArraylist.add(Color.YELLOW);

        return colorArraylist;
    }

    public static int getColor(int number){
        ArrayList<Integer> listColor = createColor();
        if(number < listColor.size())
            return listColor.get(number);
        return (listColor.get(number%(listColor.size())));
    }
}
