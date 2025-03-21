package com.example.procodewake2.controller;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.procodewake2.model.TimeAlarm;

import java.util.Calendar;
import java.util.List;

public class AlarmService {
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleAlarms(Context context) {
        List<TimeAlarm> alarms = AlarmUtils.getAllAlarms(context);
        if (alarms == null) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (TimeAlarm alarm : alarms) {
            if (!alarm.isDuocBat()) continue; // Bỏ qua báo thức chưa bật

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMinute());
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("topic", alarm.getTopic());
            intent.putExtra("soundPath", alarm.getAlarmSoundPath());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getHour() * 100 + alarm.getMinute(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
