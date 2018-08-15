package com.intkhabahmed.quicknote.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.intkhabahmed.quicknote.R;
import com.intkhabahmed.quicknote.database.NoteRepository;
import com.intkhabahmed.quicknote.databinding.ActivityAddNoteBinding;
import com.intkhabahmed.quicknote.models.Note;
import com.intkhabahmed.quicknote.utils.AppExecutors;
import com.intkhabahmed.quicknote.utils.ViewUtils;

import java.util.Arrays;

public class AddNoteActivity extends AppCompatActivity {
    private ActivityAddNoteBinding mAddNoteBinding;
    private boolean mIsEditing;
    private boolean mIsChanged;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);
        setupUI();
    }

    private void setupUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mIsChanged = !TextUtils.isEmpty(mAddNoteBinding.noteTitleInput.getText().toString().trim())
                        || !TextUtils.isEmpty(mAddNoteBinding.noteDescriptionInput.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mAddNoteBinding.noteTitleInput.addTextChangedListener(textWatcher);
        mAddNoteBinding.noteDescriptionInput.addTextChangedListener(textWatcher);
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.note))) {
            mIsEditing = true;
            mNote = intent.getParcelableExtra(getString(R.string.note));
            populateViews();
        } else {
            mIsEditing = false;
        }
    }

    private void populateViews() {
        mAddNoteBinding.noteTitleInput.setText(mNote.getTitle());
        mAddNoteBinding.noteDescriptionInput.setText(mNote.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save_action:
                insertSimpleNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertSimpleNote() {
        String noteTitle = mAddNoteBinding.noteTitleInput.getText().toString().trim();
        String noteDescription = mAddNoteBinding.noteDescriptionInput.getText().toString().trim();
        String[] hashTags = mAddNoteBinding.hashTagInput.getText().toString().split(" ");
        if (TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
            Toast.makeText(this, getString(R.string.mandatory_fields), Toast.LENGTH_LONG).show();
            return;
        }
        if (!mIsEditing) {
            final Note note = new Note();
            note.setTitle(noteTitle);
            note.setDescription(noteDescription);
            note.setHashTags(Arrays.asList(hashTags));
            note.setDateCreated(System.currentTimeMillis());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    long id = NoteRepository.getInstance().insertNote(note);
                    if (id > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddNoteActivity.this, getString(R.string.save_success), Toast.LENGTH_LONG).show();
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        });
                    }
                }
            });
        } else {
            mNote.setTitle(noteTitle);
            mNote.setDescription(noteDescription);
            mNote.setHashTags(Arrays.asList(hashTags));
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    int rowsUpdated = NoteRepository.getInstance().updateNote(mNote);
                    if (rowsUpdated > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddNoteActivity.this, getString(R.string.update_success), Toast.LENGTH_LONG).show();
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsChanged) {
            ViewUtils.showUnsavedChangesDialog(this);
        } else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
