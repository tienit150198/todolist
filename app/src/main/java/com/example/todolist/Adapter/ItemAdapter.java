package com.example.todolist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Models.Item;
import com.example.todolist.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<Item> mListItem;
    Context mContext;

    public ItemAdapter(ArrayList<Item> mListItem, Context mContext) {
        this.mListItem = mListItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.activity_recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = mListItem.get(position);

        holder.imgIcon.setImageResource(item.getIcon());
        holder.tvName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvName;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(v -> {
                if(listener != null){
                    listener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }

    }
}
