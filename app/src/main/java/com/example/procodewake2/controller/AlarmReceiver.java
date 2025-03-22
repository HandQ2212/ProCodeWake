package com.example.procodewake2.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.procodewake2.R;
import com.example.procodewake2.model.TimeAlarm;
import com.example.procodewake2.view.WakeUpActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static MediaPlayer mediaPlayer;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "⏰ [onReceive] Báo thức đã kích hoạt!");

        // Kiểm tra quyền thông báo trên Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "⚠️ Chưa có quyền thông báo, không thể gửi thông báo!");
                Toast.makeText(context, "⚠️ Cần cấp quyền thông báo!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Lấy ID báo thức
        String alarmId = intent.getStringExtra("alarmId");
        if (alarmId == null) {
            Log.e(TAG, "Không tìm thấy alarmId!");
            return;
        }
        Log.d(TAG, "alarmId nhận được: " + alarmId);

        // Lấy báo thức từ AlarmUtils
        TimeAlarm alarm = AlarmUtils.getAlarmById(context, alarmId);
        if (alarm == null) {
            Log.e(TAG, "Không tìm thấy báo thức với ID: " + alarmId);
            return;
        }

        // Lấy âm thanh báo thức
        String soundPath = alarm.getAlarmSoundPath();
        Log.d(TAG, "Đường dẫn âm thanh: " + soundPath);
        @SuppressLint("DiscouragedApi")
        int soundResId = context.getResources().getIdentifier(soundPath, "raw", context.getPackageName());
        Uri soundUri = (soundResId != 0) ? Uri.parse("android.resource://" + context.getPackageName() + "/" + soundResId) : null;

        if (soundUri != null) {
            Log.d(TAG, "Âm thanh báo thức hợp lệ: " + soundUri);
            playAlarmSound(context, soundResId); // 🔥 Loop âm thanh
        } else {
            Log.e(TAG, "Không tìm thấy tệp âm thanh: " + soundPath);
        }

        // Tạo kênh thông báo nếu cần
        createNotificationChannel(context);

        // Mở WakeUpActivity ngay khi báo thức kêu
        Intent wakeUpIntent = new Intent(context, WakeUpActivity.class);
        wakeUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        wakeUpIntent.putExtra("alarmId", alarmId);
        context.startActivity(wakeUpIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, alarmId.hashCode(), wakeUpIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Log.d(TAG, "📢 Đang tạo thông báo...");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("⏰ Báo thức!")
                .setContentText("Nhấn vào đây để tắt báo thức.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVibrate(new long[]{0, 500, 500, 500})
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.d(TAG, "✅ Thông báo đã gửi thành công!");

        // Kiểm tra trạng thái kênh thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            if (channel != null) {
                Log.d(TAG, "📢 Kênh thông báo: " + channel.getId() + " - Trạng thái: " + channel.getImportance());
            }
        }
    }

    // Tạo kênh thông báo với IMPORTANCE_HIGH - vừa hiện pop_up vừa có âm thanh
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Báo Thức",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Kênh thông báo cho báo thức");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Log.d(TAG, "📢 Kênh thông báo đã được tạo hoặc cập nhật!");
        }
    }

    // Loop âm thanh báo thức
    private void playAlarmSound(Context context, int soundResId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, soundResId);
            mediaPlayer.setLooping(true); // 🔥 Loop âm thanh liên tục
            mediaPlayer.start();
            Log.d(TAG, "🔊 Đã bắt đầu phát âm thanh báo thức!");
        }
    }

    // Dừng âm thanh khi báo thức bị tắt
    public static void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "🛑 Đã dừng âm thanh báo thức!");
        }
    }
}
