package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.NoteAdapter;
import com.example.todolist.Constants.Constants;
import com.example.todolist.Controller.NoteModify;
import com.example.todolist.Controller.SimpleItemTouchHelper;
import com.example.todolist.Interface.ItemTouchHelperAdapter;
import com.example.todolist.Interface.OnItemListenerHelper;
import com.example.todolist.Models.Note;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment implements OnItemListenerHelper {
    private static AtomicInteger index;
    private EditText edtAdd;
    private RecyclerView mRecyclerview;
    private NoteAdapter mNoteAdapter;
    private ImageButton imgbtnAdd;
    private View mRootView;
    private String mSublist;
    private NoteModify instanceNote;
    private Realm mRealmNote;
    private RealmResults<Note> mRealmResultNote;
    private Gson mGson;
    private ArrayList<String> mListItemName;

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
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        config();
        setDragAndSwipeRecyclerView();
        setClickListener();
    }

    private void init() {
        mapp();
        initRealm();
        initNote();

        mGson = new Gson();
    }

    private void mapp() {
        mRecyclerview = mRootView.findViewById(R.id.recyclerview);
        edtAdd = mRootView.findViewById(R.id.edt_add);
        imgbtnAdd = mRootView.findViewById(R.id.imgbtn_add);
    }

    private void initRealm() {
        Realm.init(getActivity());
        RealmConfiguration configNote = new RealmConfiguration.Builder().name(Constants.KEY_TABLE_NAME_NOTE)
                .schemaVersion(1)
                .build();

        mRealmNote = Realm.getInstance(configNote);
    }

    private void initNote() {
        instanceNote = NoteModify.getInstance(mRealmNote);
        index = new AtomicInteger();
    }

    private void config() {
        queryData();
        configRecyclerView(mRootView);
    }

    private void configRecyclerView(View rootView) {
        mNoteAdapter = new NoteAdapter(mRealmResultNote, true, rootView.getContext(), this);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());

        mRecyclerview.setAdapter(mNoteAdapter);
//        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setHasFixedSize(true);
    }

    private void queryData() {
        if (mSublist.equals(Constants.KEY_SUBLIST_DEFAULT)) {
            mRealmResultNote = instanceNote.queryAllData();
        } else {
            mRealmResultNote = instanceNote.queryAllData(mSublist);
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
        if (mRealmResultNote.get(position) != null) {
            instanceNote.updateColor(mRealmResultNote.get(position).getId(), mRealmResultNote.get(position).getColor() + 1);
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
        Note note = makeNote(text, mSublist);
        instanceNote.insertNote(note);     // add Sqlite
    }

    private void deleteItem(int position) {
        if (mRealmResultNote.get(position) != null) {
            instanceNote.deleteNote(mRealmResultNote.get(position).getId());
        }
    }

    public void deleteSublist(String sublist) {
        if (mSublist.equals(Constants.KEY_SUBLIST_DEFAULT)) {
            instanceNote.deleteAll();
        } else {
            instanceNote.deleteAll(sublist);
        }
    }

    public void closeRealm() {
        if (!mRealmNote.isClosed()) {
            mRealmNote.close();
        }
    }

    private Note makeNote(String content, String sublist) {
        return new Note(instanceNote.getMaxId() + 1, content, index.getAndIncrement(), sublist);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mRealmResultNote.get(position) != null) {
            sendData(position);
        }
    }

    private void sendData(int position) {
        Bundle bundle = getBundleSend(position);
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.KEY_REQUEST_CODE);
    }

    private Bundle getBundleSend(int position) {
        Bundle bundle = new Bundle();
        Note note = mRealmResultNote.get(position);
        note = mRealmNote.copyFromRealm(note);  //detach from Realm, copy values to fields. If not used, the program is always waiting
        String textNote = mGson.toJson(note);

        bundle.putString(Constants.KEY_INTENT_SEND_ACTIVITY, textNote);
        bundle.putStringArrayList(Constants.KEY_BUNDLE_SEND_FRAGMENT_LIST_NAME, mListItemName);
        return bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.KEY_REQUEST_CODE && resultCode == Constants.KEY_RESULT && data != null) {
            Note note = mGson.fromJson(data.getStringExtra(Constants.KEY_INTENT_SEND_ACTIVITY), Note.class);
            if (note != null) {
                instanceNote.updateNote(note.getId(), note);
            }
        }

    }

    @Override
    public void onChangeCheckedListener(View view, boolean isChecked, int position) {
        if (!mRecyclerview.isComputingLayout()) {
            instanceNote.updateChecked(mRealmResultNote.get(position).getId(), isChecked);
        }
    }

}
