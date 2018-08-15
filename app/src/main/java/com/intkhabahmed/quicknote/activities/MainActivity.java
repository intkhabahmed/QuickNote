package com.intkhabahmed.quicknote.activities;

import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.intkhabahmed.quicknote.R;
import com.intkhabahmed.quicknote.adapters.NotesAdapter;
import com.intkhabahmed.quicknote.database.NoteRepository;
import com.intkhabahmed.quicknote.databinding.ActivityMainBinding;
import com.intkhabahmed.quicknote.models.Note;
import com.intkhabahmed.quicknote.receivers.ClipBoardReceiver;
import com.intkhabahmed.quicknote.services.ClipboardMonitorService;
import com.intkhabahmed.quicknote.services.FloatingWindowService;
import com.intkhabahmed.quicknote.utils.AppConstants;
import com.intkhabahmed.quicknote.utils.AppExecutors;
import com.intkhabahmed.quicknote.utils.ViewUtils;
import com.intkhabahmed.quicknote.viewmodels.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnItemClickListener {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1000;
    private NotesAdapter mAdapter;
    private ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();
        setupUI();
        setupViewModel();
    }

    private void setupViewModel() {
        NoteViewModel noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                mMainBinding.loadingPb.setVisibility(View.INVISIBLE);
                if (notes != null && notes.size() > 0) {
                    mMainBinding.emptyViewContainer.setVisibility(View.INVISIBLE);
                    mAdapter.setNotes(notes);
                } else {
                    mAdapter.setNotes(null);
                    mMainBinding.emptyViewContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupUI() {
        mMainBinding.loadingPb.setVisibility(View.VISIBLE);
        mAdapter = new NotesAdapter(this);
        RecyclerView recyclerView = mMainBinding.notesRv;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                ViewUtils.showDeleteConfirmationDialog(MainActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteRepository.getInstance().deleteNote(((int) viewHolder.itemView.getTag()));
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
        mMainBinding.addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        startService(new Intent(this, ClipboardMonitorService.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (!Settings.canDrawOverlays(this)) {
            requestPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getApplicationContext().getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != OVERLAY_PERMISSION_REQUEST_CODE || resultCode != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_reason), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(Note note) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.note), note);
        startActivity(intent);
    }
}
