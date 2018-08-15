package com.intkhabahmed.quicknote.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.intkhabahmed.quicknote.services.ClipboardMonitorService;

public class ClipBoardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, ClipboardMonitorService.class));
        }
    }
}
