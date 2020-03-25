package com.example.todolist.Controller;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {
    ItemTouchHelperAdapter mListener;

    private int fromDrag = -1;
    private int toDrag = -1;

    public SimpleItemTouchHelper(ItemTouchHelperAdapter itemTouchHelper) {
        this.mListener = itemTouchHelper;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    // clieaView is only called once
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (fromDrag != -1 && toDrag != -1 && fromDrag != toDrag) {
            mListener.onItemMove(fromDrag, toDrag);
        }
        fromDrag = -1;
        toDrag = -1;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swapFlags = ItemTouchHelper.START | ItemTouchHelper.END;    // start: left - right | end: right - left

        return makeMovementFlags(dragFlags, swapFlags);
    }

    // onMove is called multiple times
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // just drag and drop only once, instead of dragging multiple times
        if (fromDrag == -1) {
            fromDrag = viewHolder.getAdapterPosition();
        }
        toDrag = target.getAdapterPosition();
//        mListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onItemDissmiss(viewHolder.getLayoutPosition(), direction);
    }
}
