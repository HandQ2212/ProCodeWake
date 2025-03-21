package com.example.procodewake2.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.procodewake2.controller.QuestionUtils;
import com.example.procodewake2.R;

import java.io.IOException;

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
        if (randomQuestion == null) {
            questionView.setText("Không tìm thấy câu hỏi.");
            correctAnswer = "";
        } else {
            questionView.setText(randomQuestion.getQuestion());
            correctAnswer = randomQuestion.getAnswer();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(soundPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        submitButton.setOnClickListener(v -> {
            String userAnswer = answerInput.getText().toString().trim();
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                mediaPlayer.stop();
                finish();
            } else {
                answerInput.setText("");
                mediaPlayer.start();
            }
        });
    }
}
