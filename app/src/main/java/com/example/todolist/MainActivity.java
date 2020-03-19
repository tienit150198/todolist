package com.example.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public EditText edtAdd;
    private RecyclerView Recyclerview;
    private NoteAdapter noteAdapter;
    private ImageButton imgbtnAdd;
    private NoteModify instance;
    private ArrayList<Note> noteArrayList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        configRecyclerView();
        setDragAndSwipeRecyclerView();
        setClickListener();
    }

    private void init() {
        mapp();
        initNote();
    }

    private void initNote() {
        instance = NoteModify.getInstance(this);
    }

    private void mapp() {
        Recyclerview = findViewById(R.id.recyclerview);
        edtAdd = findViewById(R.id.edt_add);
        imgbtnAdd = findViewById(R.id.imgbtn_add);
    }

    private void configRecyclerView() {
        noteAdapter = new NoteAdapter(noteArrayList, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        Recyclerview.setAdapter(noteAdapter);
        Recyclerview.addItemDecoration(dividerItemDecoration);
        Recyclerview.setLayoutManager(linearLayoutManager);

        noteArrayList.addAll(instance.queryAllData());
        noteAdapter.notifyDataSetChanged();
    }

    private void setDragAndSwipeRecyclerView() {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelper(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                Note noteFrom = noteArrayList.get(fromPosition);
                Note noteTo = noteArrayList.get(toPosition);

                instance.changeIndexWhenMove(fromPosition, toPosition, noteFrom.getId(), noteTo.getId(), toPosition > fromPosition);
                noteArrayList.add(toPosition, noteArrayList.remove(fromPosition));
                noteAdapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemDissmiss(int position, int direction) {
                // swipe start ( right -> left ) is delete, swipe end ( left -> right ) change color
                Note note = noteArrayList.get(position);
                if (direction == ItemTouchHelper.START) {
                    // update Sqlite
                    noteArrayList.remove(position);
                    noteAdapter.notifyItemRemoved(position);
                    instance.deleteNote(note.getId());

                } else {
                    note.setColor(note.getColor() + 1);// Update Color
                    noteArrayList.set(position, note);
                    instance.updateNote(note.getId(), note);
                    noteAdapter.notifyItemChanged(position);
                }
            }
        });
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(Recyclerview); // set touch in recyclerview
    }

    private void setClickListener() {
        imgbtnAdd.setOnClickListener(v -> {
            String text = edtAdd.getText().toString();
            if (!text.equals("")) {

                int findIndexEdited = getPositionEdited(noteArrayList);
                // check state edit or add?
                if (findIndexEdited != -1) {
                    Note note = noteArrayList.get(findIndexEdited);
                    note.setContent(text);

                    noteArrayList.set(findIndexEdited, note);
                    instance.updateNote(note.getId(), note);// update Sqlite
                    noteAdapter.notifyItemChanged(findIndexEdited);
                } else {
                    Note note = makeNote(text);
                    noteArrayList.add(note);
                    instance.insertNote(note, noteArrayList.size() - 1);     // add Sqlite
                    noteAdapter.notifyItemInserted(noteArrayList.size() - 1);
                }
                edtAdd.setText("");
            }
        });

        noteAdapter.setOnItemClickListener((view, position) -> {
            Note noteClicked = noteArrayList.get(position);
            noteArrayList = setEditedInArrayList(noteArrayList, noteClicked, position);
            setTextEditext(noteClicked.getContent());
        });
    }

    public ArrayList<Note> setEditedInArrayList(ArrayList<Note> noteArrayList, Note note, int position) {
        for (Note element : noteArrayList) {
            element.setEdited(false);
        }
        note.setEdited(true);
        noteArrayList.set(position, note);
        return noteArrayList;
    }

    public int getPositionEdited(ArrayList<Note> noteArrayList) {
        int index = -1;
        for (int i = 0; i < noteArrayList.size(); i++) {
            if (noteArrayList.get(i).isEdited() == true) {
                index = i;
                break;
            }
        }
        return index;
    }

    private Note makeNote(String content) {
        return new Note(content);
    }

    public void setTextEditext(String text) {
        edtAdd.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_delete:
                DialogHandler dialogHandler = new DialogHandler();
                dialogHandler.Confirm(this, "Confirm delelte", "You want delete all note?", "Cancel", "OK",
                        cancelProc(), okProc());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public Runnable okProc() {
        return () -> {
            Log.d("LOG_DIALOG_CLICKED", "cancelProc: clicked OK");
            instance.deleteAll();
            noteArrayList.clear();
            noteAdapter.notifyDataSetChanged();
        };
    }

    public Runnable cancelProc() {
        return () -> {
            Log.d("LOG_DIALOG_CLICKED", "cancelProc: clicked Cancel");
        };
    }
}
