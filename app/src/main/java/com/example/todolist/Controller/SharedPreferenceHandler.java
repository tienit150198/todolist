package com.example.todolist.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.todolist.Models.Item;
import com.example.todolist.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceHandler {
    private Context mContext;
    private static SharedPreferenceHandler instance;
    private SharedPreferenceHandler(Context context){
        mContext = context;
    }

    public static SharedPreferenceHandler getInstance(Context context){
        if(instance == null){
            instance = new SharedPreferenceHandler(context);
        }
        return instance;
    }

    public void saveData(Context context, String key, ArrayList<Item> itemArrayList) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_preference_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // convert Arraylist to Json
        Gson gson = new Gson();
        String jsonText = gson.toJson(itemArrayList);

        editor.putString(key, jsonText);
        editor.commit(); // commit is synchronized. Apply is not
    }
    public ArrayList<Item> getData(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.share_preference_name), MODE_PRIVATE);

        String textArray = preferences.getString(key, "");

        Gson gson = new Gson();
        Type typeArrayList = new TypeToken<ArrayList<Item>>(){}.getType();  // get type of Arraylist
        ArrayList<Item> itemArrayList = gson.fromJson(textArray, typeArrayList);

        return itemArrayList;
    }
}
