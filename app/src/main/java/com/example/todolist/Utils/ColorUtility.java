package com.example.todolist.Utils;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;

public class ColorUtility {
    private Context mContext;
    private static ColorUtility instance;
    private ColorUtility(Context context){
        mContext = context;
    }
    public static ColorUtility getInstance(Context context){
        if(instance == null){
            instance = new ColorUtility(context);
        }
        return instance;
    }

    public ArrayList<Integer> createColor(){
        ArrayList<Integer> colorArraylist = new ArrayList<>();
        colorArraylist.add(Color.WHITE);
        colorArraylist.add(Color.LTGRAY);
        colorArraylist.add(Color.CYAN);
        colorArraylist.add(Color.GREEN);
        colorArraylist.add(Color.MAGENTA);
        colorArraylist.add(Color.YELLOW);

        return colorArraylist;
    }

    public int getColor(int number){
        ArrayList<Integer> listColor = createColor();
        if(number < listColor.size())
            return listColor.get(number);
        return (listColor.get(number%(listColor.size())));
    }
}
