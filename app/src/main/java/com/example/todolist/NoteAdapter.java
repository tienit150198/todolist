package com.example.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private ArrayList<Note> noteArrayList;
    private Context mContext;
    private OnItemClickListener listener;
    private NoteModify instance;

    public NoteAdapter(ArrayList<Note> contentArrayList, Context mContext) {
        this.noteArrayList = contentArrayList;
        this.mContext = mContext;

        instance = NoteModify.getInstance(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_default, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Note content = noteArrayList.get(position);
        holder.tvContent.setText(content.getContent());
        holder.cbContent.setBackgroundColor(ColorUtility.getColor(content.getColor()));
        holder.tvContent.setBackgroundColor(ColorUtility.getColor(content.getColor()));

        // check checked
        if (content.isChecked()) {
            holder.cbContent.setChecked(true);
        } else {
            holder.cbContent.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateArrayList(ArrayList<Note> newArrayList) {
        if (noteArrayList != null) {
            noteArrayList.clear();
        }

        if (newArrayList != null) {
            noteArrayList = newArrayList;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbContent;
        private TextView tvContent;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cbContent = itemView.findViewById(R.id.cbContent);
            tvContent = itemView.findViewById(R.id.tvContent);
            //   this.setIsRecyclable(false);    // recycler not reused view

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });

            cbContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Note note = noteArrayList.get(getLayoutPosition());
                    note.setChecked(isChecked);
                    noteArrayList.set(getLayoutPosition(), note);
                    if (isChecked) {
                        tvContent.setPaintFlags(tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        tvContent.setPaintFlags(0);
                    }
                    Log.d("LOG_CLICKED_CHECKBOX", "onCheckedChanged: " + note);
                    instance.updateNote(note.getId(), note);    // update Sqlite
                }
            });
        }

    }
}
