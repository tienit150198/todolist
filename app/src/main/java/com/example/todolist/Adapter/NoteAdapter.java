package com.example.todolist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Interface.OnItemListenerHelper;
import com.example.todolist.MainActivity;
import com.example.todolist.Models.Note;
import com.example.todolist.R;
import com.example.todolist.Utils.ColorUtility;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class NoteAdapter extends RealmRecyclerViewAdapter<Note, NoteAdapter.ViewHolder> {
    private OnItemListenerHelper listener;
    private ColorUtility instanceColor;

    public NoteAdapter(@Nullable OrderedRealmCollection<Note> data, boolean autoUpdate, Context context, OnItemListenerHelper listener) {
        super(context, data, autoUpdate);
        this.listener = listener;
        instanceColor = ColorUtility.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Note note = getItem(position);
        holder.tvContent.setText(note.getContent());
        holder.cbContent.setBackgroundColor(instanceColor.getColor(note.getColor()));
        holder.tvContent.setBackgroundColor(instanceColor.getColor(note.getColor()));

        // check checked
        if (note.isChecked()) {
            holder.cbContent.setChecked(true);
        } else {
            holder.cbContent.setChecked(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbContent;
        private TextView tvContent;
        private View tvDelete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cbContent = itemView.findViewById(R.id.cbContent);
            tvContent = itemView.findViewById(R.id.tvContent);
            //   this.setIsRecyclable(false);    // recycler not reused view

            itemView.setOnClickListener(v -> {
                Intent it = new Intent(itemView.getContext(), MainActivity.class);
                itemView.getContext().startActivity(it);

                if (listener != null) {
                    listener.onItemClick(itemView, getLayoutPosition());
                }
            });

            cbContent.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    if (isChecked) {
                        tvContent.setPaintFlags(tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        tvContent.setPaintFlags(0);
                    }
                    listener.onChangeCheckedListener(buttonView, isChecked, getLayoutPosition());
                }
            });
        }
    }
}
