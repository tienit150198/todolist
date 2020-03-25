package com.example.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.NoteAdapter;
import com.example.todolist.Controller.ItemTouchHelperAdapter;
import com.example.todolist.Controller.NoteModify;
import com.example.todolist.Controller.SimpleItemTouchHelper;
import com.example.todolist.Models.Note;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
    private EditText edtAdd;
    private RecyclerView mRecyclerview;
    private NoteAdapter mNoteAdapter;
    private ImageButton imgbtnAdd;
    private NoteModify instance;
    private ArrayList<Note> mListNote;
    private View mRootView;
    private String mSublist;

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);

        Bundle bundle = getArguments();
        mSublist = bundle.getString(getString(R.string.bundle_send_fragment));
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(mRootView);
        configRecyclerView(mRootView);
        setDragAndSwipeRecyclerView();
        setClickListener();
    }

    private void init(View rootView) {
        mapp(rootView);
        initNote();
    }

    private void mapp(View rootView) {
        mRecyclerview = rootView.findViewById(R.id.recyclerview);
        edtAdd = rootView.findViewById(R.id.edt_add);
        imgbtnAdd = rootView.findViewById(R.id.imgbtn_add);
    }

    private void initNote() {
        instance = NoteModify.getInstance(getActivity());
        mListNote = new ArrayList<>();
    }

    private void configRecyclerView(View rootView) {
        mNoteAdapter = new NoteAdapter(mListNote, rootView.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());

        mRecyclerview.setAdapter(mNoteAdapter);
        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mRecyclerview.setLayoutManager(linearLayoutManager);

        queryData();
        mNoteAdapter.notifyDataSetChanged();
    }

    private void queryData() {
        if (mSublist.equals(getString(R.string.sub_list_default))) {
            mListNote.addAll(instance.queryAllData());
        } else {
            mListNote.addAll(instance.queryAllData(mSublist));
        }
    }

    private void setDragAndSwipeRecyclerView() {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelper(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                Note noteFrom = mListNote.get(fromPosition);
                Note noteTo = mListNote.get(toPosition);

                instance.changeIndexWhenMove(fromPosition, toPosition, noteFrom.getId(), noteTo.getId(), toPosition > fromPosition, mSublist);
                mListNote.add(toPosition, mListNote.remove(fromPosition));
                mNoteAdapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemDissmiss(int position, int direction) {
                // swipe start ( right -> left ) is delete, swipe end ( left -> right ) change color
                Note note = mListNote.get(position);
                if (direction == ItemTouchHelper.START) {
                    // update Sqlite
                    deleteItem(position);

                } else {
                    note.setColor(note.getColor() + 1);// Update Color
                    mListNote.set(position, note);
                    instance.updateNote(note.getId(), note, mSublist);
                    mNoteAdapter.notifyItemChanged(position);
                }
            }
        });
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerview); // set touch in recyclerview
    }

    private void setClickListener() {
        imgbtnAdd.setOnClickListener(v -> {
            String text = edtAdd.getText().toString();
            if (!text.equals("")) {
                int findIndexEdited = getPositionEdited(mListNote);
                // check state edit or add?
                if (findIndexEdited != -1) {
                    Note note = mListNote.get(findIndexEdited);
                    note.setContent(text);

                    mListNote.set(findIndexEdited, note);
                    instance.updateNote(note.getId(), note, mSublist);// update Sqlite
                    mNoteAdapter.notifyItemChanged(findIndexEdited);
                } else {
                    addItem(text);
                }
                edtAdd.setText("");
            }
        });

        mNoteAdapter.setOnItemClickListener((view, position) -> {
            Note noteClicked = mListNote.get(position);
            mListNote = setEditedInArrayList(mListNote, noteClicked, position);
            setTextEditext(noteClicked.getContent());
        });
    }

    private ArrayList<Note> setEditedInArrayList(ArrayList<Note> noteArrayList, Note note, int position) {
        for (Note element : noteArrayList) {
            element.setEdited(false);
        }
        note.setEdited(true);
        noteArrayList.set(position, note);
        return noteArrayList;
    }

    private int getPositionEdited(ArrayList<Note> noteArrayList) {
        int index = -1;
        for (int i = 0; i < noteArrayList.size(); i++) {
            if (noteArrayList.get(i).isEdited()) {
                index = i;
                break;
            }
        }
        return index;
    }

    private Note makeNote(String content) {
        return new Note(content);
    }

    private void setTextEditext(String text) {
        edtAdd.setText(text);
    }

    private void addItem(String text) {
        Note note = makeNote(text);
        mListNote.add(note);
        instance.insertNote(note, mListNote.size() - 1, mSublist);     // add Sqlite
        mNoteAdapter.notifyItemInserted(mListNote.size() - 1);
    }

    private boolean deleteItem(int position) {
        Note note = mListNote.get(position);
        mListNote.remove(position);
        mNoteAdapter.notifyItemRemoved(position);
        boolean deleted = instance.deleteNote(note.getId(), mSublist);

        return deleted;
    }

    public boolean deleteSublist(String sublist) {
        boolean deleted = false;
        if (mSublist.equals(getString(R.string.sub_list_default))) {
            deleted = instance.deleteAll();
        } else {
            deleted = instance.deleteAll(sublist);
        }

        mNoteAdapter.notifyDataSetChanged();
        return deleted;
    }
}
