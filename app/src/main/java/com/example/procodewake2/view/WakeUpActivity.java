package com.example.procodewake2.view;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.procodewake2.controller.AlarmReceiver;
import com.example.procodewake2.controller.AlarmUtils;
import com.example.procodewake2.controller.JsonHelper;
import com.example.procodewake2.controller.QuestionUtils;
import com.example.procodewake2.R;
import com.example.procodewake2.model.TimeAlarm;

import java.util.List;

public class WakeUpActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private String correctAnswer;
    private final String TAG = "WakeUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_hinh_bao_thuc);
        ImageView alarmGif = findViewById(R.id.alarmGif);
        AlarmReceiver.stopAlarmSound();
        Glide.with(this)
                .asGif()
                .load(R.drawable.alarm_gif)
                .into(alarmGif);

        TextView questionView = findViewById(R.id.questionText);
        EditText answerInput = findViewById(R.id.answerInput);
        Button submitButton = findViewById(R.id.submitAnswer);

        // Nhận alarmId từ Intent
        String alarmId = getIntent().getStringExtra("alarmId");
        Log.d(TAG, "Nhận được alarmId: " + alarmId);

        if (alarmId == null) {
            Log.e(TAG, "alarmId bị null! Không thể xử lý báo thức.");
            finish();
            return;
        }

        // Lấy thông tin báo thức từ AlarmUtils
        TimeAlarm alarm = AlarmUtils.getAlarmById(this, alarmId);
        if (alarm == null) {
            Log.e(TAG, "Không tìm thấy báo thức với ID: " + alarmId);
            finish();
            return;
        }

        String topic = alarm.getTopic();
        String soundPath = alarm.getAlarmSoundPath();
        Log.d(TAG, "Topic: " + topic + ", SoundPath: " + soundPath);

        // Lấy câu hỏi theo topic
        QuestionUtils.Question randomQuestion = QuestionUtils.getRandomQuestion(this, topic);
        if (randomQuestion != null) {
            questionView.setText(randomQuestion.getQuestion());
            correctAnswer = randomQuestion.getAnswer();
        } else {
            Log.e(TAG, "Không tìm thấy câu hỏi cho chủ đề: " + topic);
            finish();
            return;
        }

        // Phát âm thanh báo thức
        if (soundPath != null && !soundPath.isEmpty()) {
            @SuppressLint("DiscouragedApi") int soundResId = getResources().getIdentifier(soundPath, "raw", getPackageName());
            if (soundResId != 0) {
                mediaPlayer = MediaPlayer.create(this, soundResId);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } else {
                Log.e(TAG, "Không tìm thấy file âm thanh: " + soundPath);
            }
        } else {
            Log.e(TAG, "soundPath bị null hoặc rỗng.");
        }

        // Xử lý kiểm tra câu trả lời
        submitButton.setOnClickListener(v -> {
            String userAnswer = answerInput.getText().toString().trim();
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                Log.d(TAG, "Câu trả lời đúng! Tắt báo thức.");
                Toast.makeText(this, "Trả lời đúng, chúc bạn 1 ngày tốt lành", Toast.LENGTH_SHORT).show();
                stopAlarm();
                finish();
            } else {
                Log.d(TAG, "Câu trả lời sai! Tiếp tục phát nhạc.");
                Toast.makeText(this, "Trả lời sai, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                answerInput.setText("");
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

        // Bật màn hình khi báo thức kêu
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
