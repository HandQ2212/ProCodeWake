package com.example.procodewake2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    private static final String FILE_NAME = "alarms.json";
    private final Handler handler = new Handler();
    private Runnable checkAlarmsTask;

    @Override
    public void onCreate() {
        super.onCreate();
        startCheckingAlarms();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startCheckingAlarms() {
        checkAlarmsTask = new Runnable() {
            @Override
            public void run() {
                readAlarmsAndSet();
                handler.postDelayed(this, 60000); // Kiểm tra mỗi phút
            }
        };
        handler.post(checkAlarmsTask);
    }

    private void readAlarmsAndSet() {
        try {
            File file = new File(getFilesDir(), FILE_NAME);
            if (!file.exists()) return;

            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONArray alarmsArray = new JSONArray(jsonStr);

            for (int i = 0; i < alarmsArray.length(); i++) {
                JSONObject obj = alarmsArray.getJSONObject(i);
                int hour = obj.getInt("hour");
                int minute = obj.getInt("minute");
                boolean isEnabled = obj.getBoolean("duocBat");
                if (isEnabled) {
                    setAlarm(hour, minute);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading alarms.json", e);
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, hour * 60 + minute, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkAlarmsTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
