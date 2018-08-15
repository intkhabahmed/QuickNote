package com.intkhabahmed.quicknote.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.intkhabahmed.quicknote.models.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;
    private static final String DATABASE_NAME = "notes.db";

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract NoteDao notesDao();
}
