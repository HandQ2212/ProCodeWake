package com.example.procodewake2.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.procodewake2.view.WakeUpActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String topic = intent.getStringExtra("topic");
        String soundPath = intent.getStringExtra("soundPath");

        Intent wakeUpIntent = new Intent(context, WakeUpActivity.class);
        wakeUpIntent.putExtra("topic", topic);
        wakeUpIntent.putExtra("soundPath", soundPath);
        wakeUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(wakeUpIntent);
    }
}