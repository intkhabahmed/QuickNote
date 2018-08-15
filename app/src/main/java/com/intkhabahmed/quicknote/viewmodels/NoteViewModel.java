package com.intkhabahmed.quicknote.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.intkhabahmed.quicknote.database.NoteRepository;
import com.intkhabahmed.quicknote.models.Note;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private LiveData<List<Note>> notes;

    public NoteViewModel() {
        if (notes == null) {
            notes = NoteRepository.getInstance().getNotes();
        }
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}
