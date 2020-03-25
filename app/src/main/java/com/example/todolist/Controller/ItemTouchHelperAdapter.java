package com.example.todolist.Controller;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDissmiss(int position, int direction);
}
