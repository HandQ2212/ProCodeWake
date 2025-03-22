package com.example.procodewake2.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.procodewake2.model.TimeAlarm;

import java.util.Calendar;
import java.util.List;

public class AlarmScheduler {
    private static final String TAG = "AlarmScheduler";

    // Lập lịch cho một báo thức theo ID 
    public static void scheduleAlarm(Context context, String alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Lấy báo thức từ JSON
        TimeAlarm alarm = AlarmUtils.getAlarmById(context, alarmId);
        if (alarm == null) {
            Log.e(TAG, "❌ Không tìm thấy báo thức với ID: " + alarmId);
            return;
        }

        if (!alarm.isDuocBat()) {
            Log.w(TAG, "⚠️ Báo thức đã bị tắt, không lập lịch: " + alarmId);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // Nếu thời gian đã qua, lên lịch cho ngày mai
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.example.procodewake2.ALARM_TRIGGERED");
        intent.putExtra("alarmId", alarmId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, alarmId.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent
        );

        Log.d(TAG, "✅ Đã lên lịch báo thức: " + alarmId + " lúc " + alarm.getHour() + ":" + alarm.getMinute());
    }

    // Hủy một báo thức theo ID 
    public static void cancelAlarm(Context context, String alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, alarmId.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "🛑 Đã hủy báo thức với ID: " + alarmId);
    }

    // Lập lịch tất cả báo thức từ danh sách ID 
    public static void scheduleAllAlarms(Context context) {
        List<String> alarmIds = JsonHelper.getAllAlarmIds(context);
        if (alarmIds.isEmpty()) {
            Log.w(TAG, "⚠️ Không có báo thức nào để lập lịch!");
            return;
        }
        for (String alarmId : alarmIds) {
            scheduleAlarm(context, alarmId);
        }
    }
}
