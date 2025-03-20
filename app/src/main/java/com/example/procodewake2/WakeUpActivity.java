package com.example.procodewake2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class WakeUpActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private String correctAnswer = "42"; // Câu trả lời đúng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_hinh_bao_thuc);

        TextView questionText = findViewById(R.id.questionText);
        EditText answerInput = findViewById(R.id.answerInput);
        Button submitAnswer = findViewById(R.id.submitAnswer);
        TextView resultText = findViewById(R.id.resultText);

        // Nhận dữ liệu từ Intent
        String topic = getIntent().getStringExtra("topic");
        String soundPath = getIntent().getStringExtra("alarmSoundPath");
        questionText.setText("Câu hỏi: " + topic);

        // Phát âm thanh báo thức
        if (soundPath != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(soundPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        submitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = answerInput.getText().toString().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    resultText.setText("Chính xác! Báo thức sẽ tắt.");
                    resultText.setVisibility(View.VISIBLE);
                    stopAlarm();
                    finish();
                } else {
                    resultText.setText("Sai rồi! Hãy thử lại.");
                    resultText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }
}
