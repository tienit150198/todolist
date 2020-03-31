package com.example.todolist.Controller;

import com.example.todolist.Constants.Constants;
import com.example.todolist.Models.Note;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmResults;

public class NoteModify {
    private static NoteModify instance;
    private Realm mRealm;

    private NoteModify(Realm realm) {
        mRealm = realm;
    }

    public static NoteModify getInstance(Realm realm) {
        if (instance == null) {
            instance = new NoteModify(realm);
        }
        return instance;
    }

    public void insertNote(Note note) {
        mRealm.executeTransaction(realm -> mRealm.insert(note));
    }

    public void updateNote(int id, @Nonnull Note note) {
        mRealm.executeTransaction(realm -> {
            Note noteUpdate = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            noteUpdate.setContent(note.getContent());
            noteUpdate.setChecked(note.isChecked());
            noteUpdate.setColor(note.getColor());
            noteUpdate.setDateCreate(note.getDateCreate());
            noteUpdate.setNameSublist(note.getNameSublist());
        });
    }

    public void updateContent(int id, @Nonnull String content) {
        mRealm.executeTransaction(realm -> {
            Note noteUpdate = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            noteUpdate.setContent(content);
        });
    }

    public void updateColor(int id, int color) {
        mRealm.executeTransaction(realm -> {
            Note noteUpdate = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            noteUpdate.setColor(color);
        });
    }

    public void updateChecked(int id, boolean checked) {
        mRealm.executeTransaction(realm -> {
            Note noteUpdate = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            noteUpdate.setChecked(checked);
        });
    }

    public void updateIndex(int id, int index) {
        mRealm.executeTransaction(realm -> {
            Note noteUpdate = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            noteUpdate.setIndex(index);
        });
    }

    public void updateSublist(int id, String sublist) {
        mRealm.executeTransaction(realm -> {
            Note noteUpdate = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            noteUpdate.setNameSublist(sublist);
        });
    }

    public void deleteAll() {
        mRealm.executeTransaction(realm -> mRealm.where(Note.class).findAll().deleteAllFromRealm());
    }

    public void deleteAll(String sublist) {
        mRealm.executeTransaction(realm -> mRealm.where(Note.class).equalTo(Constants.KEY_SUBLIST, sublist).findAll().deleteAllFromRealm());
    }

    public void deleteNote(int id) {
        mRealm.executeTransaction(realm -> {
            Note note = mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
            if (note != null) {
                note.deleteFromRealm();
            }
        });
    }

    public RealmResults<Note> queryAllData() {
        return mRealm.where(Note.class).findAll().sort(Constants.KEY_SUBLIST);
    }

    public RealmResults<Note> queryAllData(String nameSublist) {
        return mRealm.where(Note.class).equalTo(Constants.KEY_SUBLIST, nameSublist).findAll().sort(Constants.KEY_INDEX);
    }

    public Note queryNote(int id){
        return mRealm.where(Note.class).equalTo(Constants.KEY_ID, id).findFirst();
    }

    public int getMaxId() {
        Number number = mRealm.where(Note.class).max(Constants.KEY_ID);
        int id = 0;
        if (number != null)
            id = number.intValue();
        return id;
    }
}
