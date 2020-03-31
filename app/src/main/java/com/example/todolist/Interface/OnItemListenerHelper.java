package com.example.todolist.Interface;

import android.view.View;

public interface OnItemListenerHelper {
    void onItemClick(View view, int position);

    void onChangeCheckedListener(View view, boolean isChecked, int position);
}
