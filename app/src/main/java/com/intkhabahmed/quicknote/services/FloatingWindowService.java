package com.intkhabahmed.quicknote.services;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.intkhabahmed.quicknote.R;
import com.intkhabahmed.quicknote.activities.MainActivity;
import com.intkhabahmed.quicknote.database.NoteRepository;
import com.intkhabahmed.quicknote.models.Note;
import com.intkhabahmed.quicknote.utils.AppExecutors;

import java.util.Arrays;

public class FloatingWindowService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText hashTagEditText;

    public FloatingWindowService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_window, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                PixelFormat.TRANSLUCENT);
        windowManager.addView(floatingView, layoutParams);
        titleEditText = floatingView.findViewById(R.id.note_title_input);
        descriptionEditText = floatingView.findViewById(R.id.note_description_input);
        hashTagEditText = floatingView.findViewById(R.id.hash_tag_input);
        floatingView.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSimpleNote();
            }
        });
        floatingView.findViewById(R.id.window_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                startService(new Intent(FloatingWindowService.this, ClipboardMonitorService.class));
            }
        });

        floatingView.findViewById(R.id.app_title_tv).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        return false;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, layoutParams);
                        return true;
                }
                return false;
            }
        });

        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cm != null) {
            String copiedText = cm.getPrimaryClip().getItemAt(0).getText().toString();
            descriptionEditText.setText(copiedText);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }

    public void insertSimpleNote() {
        String noteTitle = titleEditText.getText().toString().trim();
        String noteDescription = descriptionEditText.getText().toString().trim();
        String[] hashTags = hashTagEditText.getText().toString().split(" ");
        if (TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
            Toast.makeText(this, getString(R.string.mandatory_fields), Toast.LENGTH_LONG).show();
            return;
        }
        final Note note = new Note();
        note.setTitle(noteTitle);
        note.setDescription(noteDescription);
        note.setHashTags(Arrays.asList(hashTags));
        note.setDateCreated(System.currentTimeMillis());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                NoteRepository.getInstance().insertNote(note);
            }
        });
        Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_LONG).show();
        stopSelf();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
