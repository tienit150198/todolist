package com.example.todolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Interface.OnItemListenerHelper;
import com.example.todolist.Models.Item;
import com.example.todolist.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private OnItemListenerHelper listener;
    private ArrayList<Item> mItems;

    public ItemAdapter(ArrayList<Item> items, OnItemListenerHelper listener) {
        this.listener = listener;
        this.mItems = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_recycler_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = mItems.get(position);

        holder.imgIcon.setImageResource(item.getIcon());
        holder.tvName.setText(item.getName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvName;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }

    }
}
