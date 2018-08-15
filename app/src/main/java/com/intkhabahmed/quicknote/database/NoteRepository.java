package com.intkhabahmed.quicknote.database;

import android.arch.lifecycle.LiveData;

import com.intkhabahmed.quicknote.models.Note;
import com.intkhabahmed.quicknote.utils.AppExecutors;
import com.intkhabahmed.quicknote.utils.Global;

import java.util.List;

public class NoteRepository {
    private static NoteRepository sInstance;
    private static final Object LOCK = new Object();

    public static NoteRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NoteRepository();
            }
        }
        return sInstance;
    }

    public LiveData<List<Note>> getNotes() {
        return Global.getDbInstance().notesDao().getAllNotes();
    }

    public LiveData<Note> getNoteById(int noteId) {
        return Global.getDbInstance().notesDao().getNoteById(noteId);
    }

    public void deleteNote(final int noteId) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Global.getDbInstance().notesDao().deleteNote(noteId);
            }
        });
    }

    public int updateNote(final Note note) {
        return Global.getDbInstance().notesDao().updateNote(note);

    }

    public long insertNote(final Note note) {
        return Global.getDbInstance().notesDao().insertNote(note);
    }
}
