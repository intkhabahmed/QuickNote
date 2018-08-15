package com.intkhabahmed.quicknote.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.intkhabahmed.quicknote.models.Note;

import java.util.List;

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes order by date_created desc")
    LiveData<List<Note>> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertNote(Note note);

    @Query("SELECT * FROM notes WHERE _id = :noteId")
    LiveData<Note> getNoteById(int noteId);

    @Query("DELETE FROM notes WHERE _id = :noteId")
    void deleteNote(int noteId);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int updateNote(Note note);
}
