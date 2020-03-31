package com.example.todolist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Interface.OnItemListenerHelper;
import com.example.todolist.Models.Item;
import com.example.todolist.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ItemAdapter extends RealmRecyclerViewAdapter<Item, ItemAdapter.ItemViewHolder> {
    public OnItemListenerHelper listener;

    public ItemAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Item> data, boolean autoUpdate, OnItemListenerHelper listener) {
        super(context, data, autoUpdate);
        this.listener = listener;
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
        Item item = getItem(position);

        holder.imgIcon.setImageResource(item.getIcon());
        holder.tvName.setText(item.getName());
    }

    @Override
    public long getItemId(int position) {
        return position;
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
