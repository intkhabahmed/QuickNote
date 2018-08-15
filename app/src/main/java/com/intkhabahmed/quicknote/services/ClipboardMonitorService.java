package com.intkhabahmed.quicknote.services;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.intkhabahmed.quicknote.R;

public class ClipboardMonitorService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {
    private ClipboardManager mClipboardManager;
    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams layoutParams;

    public ClipboardMonitorService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_bubble, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowManager.addView(floatingView, layoutParams);
        floatingView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                startService(new Intent(ClipboardMonitorService.this, ClipboardMonitorService.class));
            }
        });
        floatingView.setVisibility(View.INVISIBLE);
        mClipboardManager = (ClipboardManager) getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
        if (mClipboardManager != null) {
            mClipboardManager.addPrimaryClipChangedListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipboardManager.removePrimaryClipChangedListener(this);
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }

    @Override
    public void onPrimaryClipChanged() {
        floatingView.setVisibility(View.VISIBLE);
        floatingView.findViewById(R.id.bubble_civ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClipboardMonitorService.this, FloatingWindowService.class);
                startService(intent);
                stopSelf();
            }
        });
    }
}
