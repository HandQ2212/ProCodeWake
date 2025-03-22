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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.procodewake2.R;
import com.example.procodewake2.model.TimeAlarm;
import com.example.procodewake2.controller.AlarmUtils;

import java.util.Calendar;
import java.util.List;

public class AlarmService extends Service {

    private static final String TAG = "AlarmService"; // Tag cho Log
    private static final String CHANNEL_ID = "AlarmServiceChannel";
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d(TAG, "AlarmService đã khởi động!");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm Service")
                .setContentText("Alarm service is running")
                .build();
        startForeground(1, notification);

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Đang kiểm tra báo thức...");
                checkAlarms();
                handler.postDelayed(this, 60000); // Kiểm tra mỗi phút
            }
        };
        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "AlarmService đã nhận onStartCommand.");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkAlarms() {
        List<String> alarmIds = AlarmUtils.getAllAlarmIds(this);
        if (alarmIds == null) {
            Log.e(TAG, "Không tìm thấy báo thức nào trong hệ thống!");
            return;
        }

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        Log.d(TAG, "Hiện tại: " + currentHour + ":" + currentMinute);
        Log.d(TAG, "Tổng số báo thức: " + alarmIds.size());

        for (String alarmId : alarmIds) {
            TimeAlarm alarm = AlarmUtils.getAlarmById(this, alarmId);
            if (alarm == null) {
                Log.e(TAG, "Báo thức với ID " + alarmId + " không tồn tại!");
                continue;
            }

            if (!alarm.isDuocBat()) {
                Log.d(TAG, "Báo thức " + alarmId + " đang tắt, bỏ qua.");
                continue;
            }

            Log.d(TAG, "Đang kiểm tra báo thức: " + alarmId + " -> " + alarm.getHour() + ":" + alarm.getMinute());

            if (alarm.getHour() == currentHour && alarm.getMinute() == currentMinute) {
                Log.d(TAG, "BÁO THỨC KÍCH HOẠT! ID: " + alarmId);

                Intent broadcastIntent = new Intent(this, AlarmReceiver.class);
                broadcastIntent.putExtra("alarmId", alarmId);
                sendBroadcast(broadcastIntent);
                Log.d(TAG, "Đã gửi broadcast đến AlarmReceiver với alarmId: " + alarmId);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.d(TAG, "AlarmService đã bị hủy!");
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
