package com.example.todolist.Controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolist.Constants.Constants;
import com.example.todolist.DAO.ItemDao;
import com.example.todolist.DAO.NoteDao;
import com.example.todolist.Models.Item;
import com.example.todolist.Models.Note;

@Database(entities = {Note.class, Item.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // do something when update version
        }
    };
    private static final Object sLock = new Object();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, Constants.KEY_TABLE_NAME_APPDATA)
                        .allowMainThreadQueries()   // allow query in main, default false
//                    .addMigrations(MIGRATION_0_1)
//                    .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract ItemDao itemDao();

    public abstract NoteDao noteDao();
}
