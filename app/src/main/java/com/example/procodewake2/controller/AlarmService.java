package com.example.procodewake2.controller;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.procodewake2.R;
import com.example.procodewake2.model.TimeAlarm;
import com.example.procodewake2.controller.AlarmUtils;
import com.example.procodewake2.view.WakeUpActivity;

import java.util.Calendar;
import java.util.List;

public class AlarmService extends Service {

    private static final String CHANNEL_ID = "AlarmServiceChannel";
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm Service")
                .setContentText("Alarm service is running")
                .build();
        startForeground(1, notification);

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                checkAlarms();
                handler.postDelayed(this, 60000); // Kiểm tra mỗi phút
            }
        };
        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkAlarms() {
        List<String> alarmIds = AlarmUtils.getAllAlarmIds(this);
        if (alarmIds == null) return;

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        for (String alarmId : alarmIds) {
            TimeAlarm alarm = AlarmUtils.getAlarmById(this, alarmId);
            if (alarm == null || !alarm.isDuocBat()) continue; // Bỏ qua báo thức chưa bật

            if (alarm.getHour() == currentHour && alarm.getMinute() == currentMinute) {
                Intent wakeUpIntent = new Intent(this, WakeUpActivity.class);
                wakeUpIntent.putExtra("alarmId", alarmId);
                wakeUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(wakeUpIntent);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}