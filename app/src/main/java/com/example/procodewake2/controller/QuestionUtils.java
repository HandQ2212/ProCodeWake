package com.example.procodewake2.controller;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuestionUtils {
    private static final String FILE_NAME = "questions.json";

    public static Question getRandomQuestion(Context context, String topic) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Map<String, List<Question>> questionMap = gson.fromJson(reader, new TypeToken<Map<String, List<Question>>>() {}.getType());

            List<Question> questions = questionMap.get(topic);
            if (questions == null || questions.isEmpty()) return null;

            return questions.get(new Random().nextInt(questions.size())); // Lấy ngẫu nhiên 1 câu
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Question {
        private String question;
        private String answer;

        public String getQuestion() { return question; }
        public String getAnswer() { return answer; }
    }
}
