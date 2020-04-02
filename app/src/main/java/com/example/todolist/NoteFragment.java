package com.example.todolist;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.todolist.Constants.Constants;
import com.example.todolist.Controller.AppDatabase;
import com.example.todolist.Controller.SimpleItemTouchHelper;
import com.example.todolist.Interface.ItemTouchHelperAdapter;
import com.example.todolist.Interface.OnItemListenerHelper;
import com.example.todolist.Models.Note;
import com.example.todolist.Utils.Validate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment implements OnItemListenerHelper {
    private EditText edtAdd;
    private RecyclerView mRecyclerview;
    private NoteAdapter mNoteAdapter;
    private ImageButton imgbtnAdd;
    private View mRootView;
    private String mSublist;
    private ArrayList<String> mListItemName;
    private ArrayList<Note> mNotes;
    private AppDatabase instanceData;

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        Bundle bundle = getArguments();
        mSublist = bundle.getString(Constants.KEY_BUNDLE_SEND_FRAGMENT_SUBLISTNAME);
        mListItemName = bundle.getStringArrayList(Constants.KEY_BUNDLE_SEND_FRAGMENT_LIST_NAME);
        Log.d("LOG_FRAGMENT", "onCreateView: " + mSublist);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        config();
        setDragAndSwipeRecyclerView();
        setClickListener();
        Log.d("LOG_FRAGMENT", "onActivityCreated: " + mNotes.toString());
    }

    private void init() {
        mapp();
        initNote();

    }

    private void mapp() {
        mRecyclerview = mRootView.findViewById(R.id.recyclerview);
        edtAdd = mRootView.findViewById(R.id.edt_add);
        imgbtnAdd = mRootView.findViewById(R.id.imgbtn_add);
    }

    private void initNote() {
        instanceData = AppDatabase.getInstance(getActivity());
    }

    private void config() {
        queryData();
        configRecyclerView(mRootView);
    }

    private void configRecyclerView(View rootView) {
        mNoteAdapter = new NoteAdapter(getActivity(), mNotes, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());

        mRecyclerview.setAdapter(mNoteAdapter);
        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setHasFixedSize(true);
    }

    private void queryData() {
        if (mSublist.equals(Constants.KEY_SUBLIST_DEFAULT)) {
            mNotes = (ArrayList) instanceData.noteDao().queryAll();
        } else {
            mNotes = (ArrayList) instanceData.noteDao().querySublist(mSublist);
        }
    }

    private void setDragAndSwipeRecyclerView() {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelper(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
            }

            @Override
            public void onItemDissmiss(int position, int direction) {
                // swipe start ( right -> left ) is delete, swipe end ( left -> right ) change color
                if (direction == ItemTouchHelper.START) {
                    // update Sqlite
                    deleteItem(position);
                } else {
                    updateColor(position);
                }
                mNoteAdapter.notifyDataSetChanged();
            }
        });
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerview); // set touch in recyclerview
    }

    private void updateColor(int position) {
        if (mNotes.get(position) != null) {
            Note note = mNotes.get(position);
            note.setColor(note.getColor() + 1);
            mNotes.set(position, note);
            instanceData.noteDao().updateNote(note);
        }
    }

    private void setClickListener() {
        imgbtnAdd.setOnClickListener(v -> {
            String text = edtAdd.getText().toString();
            if (!text.equals("")) {
                addItem(text);
                edtAdd.setText("");
            }
        });
    }

    private void addItem(String text) {
        Note note = makeNote(text.trim(), mSublist);
        long idNew = instanceData.noteDao().insertNote(note);     // add Sqlite
        note.setId((int) idNew);
        mNotes.add(note);
    }

    private void deleteItem(int position) {
        if (mNotes.get(position) != null) {
            instanceData.noteDao().deleteNote(mNotes.get(position));
            mNotes.remove(position);
        }
    }

    public void deleteSublist(String sublist) {
        mNotes.clear();
        if (mSublist.equals(Constants.KEY_SUBLIST_DEFAULT)) {
            instanceData.noteDao().deleteAll();
        } else {
            instanceData.noteDao().deleteSublist(sublist);
        }
    }

//    public void closeRealm() {
//        if (!mRealmNote.isClosed()) {
//            mRealmNote.close();
//        }
//    }

    private Note makeNote(String content, String sublist) {
        return new Note(content, sublist);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mNotes.get(position) != null) {
            sendData(position);
        }
    }

    // đây là truyền dữ liệu từ fragment qua editActivity để chỉnh sửa
    private void sendData(int position) {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        Bundle bundle = getBundleSend(position);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.KEY_REQUEST_CODE);
    }

    private Bundle getBundleSend(int position) {
        Bundle bundle = new Bundle();
        Note note = mNotes.get(position);

        bundle.putSerializable(Constants.KEY_INTENT_SEND_ACTIVITY, note);
        bundle.putStringArrayList(Constants.KEY_BUNDLE_SEND_FRAGMENT_LIST_NAME, mListItemName);
        return bundle;
    }

    // đây ,là nhận dữ liệu sau khi edit xong nó k gọi thằng này luôn, thử log hoặc toast đi là biết
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.KEY_REQUEST_CODE && resultCode == Constants.KEY_RESULT && data != null) {
            Note note = (Note) data.getSerializableExtra(Constants.KEY_INTENT_SEND_ACTIVITY);

            int position = Validate.getInstance(getActivity()).findIndexWithId(mNotes, note.getId());
            mNotes.set(position, note);
            instanceData.noteDao().updateNote(note);
        }
    }

    @Override
    public void onChangeCheckedListener(View view, boolean isChecked, int position) {
        if (!mRecyclerview.isComputingLayout()) {
            Note note = mNotes.get(position);
            note.setChecked(isChecked);
            instanceData.noteDao().updateNote(note);
        }
    }
}
