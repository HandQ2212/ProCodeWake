package com.example.procodewake2.view;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.procodewake2.controller.QuestionUtils;
import com.example.procodewake2.R;

public class WakeUpActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_hinh_bao_thuc);

        TextView questionView = findViewById(R.id.questionText);
        EditText answerInput = findViewById(R.id.answerInput);
        Button submitButton = findViewById(R.id.submitAnswer);

        String topic = getIntent().getStringExtra("topic");
        String soundPath = getIntent().getStringExtra("soundPath");

        QuestionUtils.Question randomQuestion = QuestionUtils.getRandomQuestion(this, topic);

        //Phát âm thanh
        @SuppressLint("DiscouragedApi") int soundResId = getResources().getIdentifier(soundPath, "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.setLooping(true); // Phát âm thanh lặp lại
        mediaPlayer.start();

        submitButton.setOnClickListener(v -> {
            String userAnswer = answerInput.getText().toString().trim();
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            } else {
                answerInput.setText("");
                mediaPlayer.start();
            }
        });

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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}