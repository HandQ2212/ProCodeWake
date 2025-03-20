package com.example.procodewake2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static final String FILE_NAME = "questions.json";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Báo thức được kích hoạt!");

        String topic = intent.getStringExtra("topic");
        String alarmSoundPath = intent.getStringExtra("alarmSoundPath");

        String[] questionData = getQuestionForTopic(context, topic);
        if (questionData == null) {
            Log.e(TAG, "Không tìm thấy câu hỏi cho chủ đề: " + topic);
            return;
        }

        Intent alarmIntent = new Intent(context, WakeUpActivity.class);
        alarmIntent.putExtra("question", questionData[0]);
        alarmIntent.putExtra("answer", questionData[1]);
        alarmIntent.putExtra("alarmSoundPath", alarmSoundPath);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }

    private String[] getQuestionForTopic(Context context, String topic) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) return null;

            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONArray questionsArray = new JSONArray(jsonStr);

            List<JSONObject> matchedQuestions = new ArrayList<>();
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject obj = questionsArray.getJSONObject(i);
                if (obj.getString("topic").equals(topic)) {
                    matchedQuestions.add(obj);
                }
            }

            if (matchedQuestions.isEmpty()) return null;

            Random random = new Random();
            JSONObject selectedQuestion = matchedQuestions.get(random.nextInt(matchedQuestions.size()));
            return new String[]{selectedQuestion.getString("question"), selectedQuestion.getString("answer")};
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi đọc file questions.json", e);
        }
        return null;
    }
}
