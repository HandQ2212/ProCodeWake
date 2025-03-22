package com.example.procodewake2.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.procodewake2.model.TimeAlarm;
import com.example.procodewake2.controller.AlarmUtils;
import com.example.procodewake2.view.WakeUpActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver"; // Tag cho Log
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Báo thức đã kích hoạt");

        String alarmId = intent.getStringExtra("alarmId");
        Log.d(TAG, "Alarm ID: " + alarmId);

        TimeAlarm alarm = AlarmUtils.getAlarmById(context, alarmId);
        if (alarm != null) {
            Log.d(TAG, "Đang mở WakeUpActivity...");

            Intent wakeUpIntent = new Intent(context, WakeUpActivity.class);
            wakeUpIntent.putExtra("alarmId", alarmId);
            wakeUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(wakeUpIntent);
        } else {
            Log.e(TAG, "Không tìm thấy báo thức.");
        }
    }

}