package com.example.procodewake2.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "📌 Thiết bị khởi động lại, khôi phục tất cả báo thức...");
            AlarmScheduler.scheduleAllAlarms(context);
        }
    }
}