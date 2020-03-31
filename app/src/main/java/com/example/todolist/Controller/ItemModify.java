package com.example.todolist.Controller;

import com.example.todolist.Constants.Constants;
import com.example.todolist.Models.Item;

import io.realm.Realm;
import io.realm.RealmResults;

public class ItemModify {
    private static ItemModify instance;
    private Realm mRealm;

    private ItemModify(Realm realm) {
        mRealm = realm;
    }

    public static ItemModify getInstance(Realm realm) {
        if (instance == null) {
            instance = new ItemModify(realm);
        }
        return instance;
    }

    public void deleteItem(String name) {
        mRealm.executeTransaction(realm -> {
            Item item = mRealm.where(Item.class).equalTo(Constants.KEY_NAME, name).findFirst();
            if (item != null) {
                item.deleteFromRealm();
            }
        });
    }

    public void insertItem(Item item) {    // name is unique
        mRealm.executeTransaction(realm -> {
            mRealm.insertOrUpdate(item);
        });
    }

    public void updateItem(String oldName, String newName) {
        mRealm.executeTransaction(realm -> {
            Item item = mRealm.where(Item.class).equalTo(Constants.KEY_NAME, oldName).findFirst();
            if (item != null) {
                item.setName(newName);
            }
        });
    }

    public RealmResults<Item> queryAllItem() {
        return mRealm.where(Item.class).findAll();
    }

}
