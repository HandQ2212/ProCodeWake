package com.example.procodewake2.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.procodewake2.model.TimeAlarm;
import com.example.procodewake2.controller.AlarmUtils;
import com.example.procodewake2.view.WakeUpActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmId = intent.getStringExtra("alarmId");
        TimeAlarm alarm = AlarmUtils.getAlarmById(context, alarmId);

        if (alarm != null) {
            Intent wakeUpIntent = new Intent(context, WakeUpActivity.class);
            wakeUpIntent.putExtra("alarmId", alarmId);
            wakeUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(wakeUpIntent);
        }
    }
}