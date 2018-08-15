package com.intkhabahmed.quicknote.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.intkhabahmed.quicknote.R;
import com.intkhabahmed.quicknote.databinding.ActivityDetailBinding;
import com.intkhabahmed.quicknote.models.Note;
import com.intkhabahmed.quicknote.utils.DateUtils;

import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mDetailBinding;
    private Note mNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setupUI();
    }

    private void setupUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.note))) {
            mNote = intent.getParcelableExtra(getString(R.string.note));
        }
        populateViews();
    }

    private void populateViews() {
        mDetailBinding.noteTitleTv.setText(mNote.getTitle());
        mDetailBinding.noteDesciptionTv.setText(mNote.getDescription());
        mDetailBinding.hashTagsTv.setText("");
        for (String tag : mNote.getHashTags()) {
            mDetailBinding.hashTagsTv.append(String.format("%s%s%s", "#", tag.trim(), " "));
        }
        mDetailBinding.dateCreatedTv.setText(DateUtils.getFormattedTime(mNote.getDateCreated()));
        mDetailBinding.editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, AddNoteActivity.class);
                intent.putExtra(getString(R.string.note), mNote);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
