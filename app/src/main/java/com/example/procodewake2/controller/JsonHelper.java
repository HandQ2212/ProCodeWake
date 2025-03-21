package com.example.procodewake2.controller;
import android.content.Context;

import com.example.procodewake2.model.TimeAlarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonHelper {
    private static final String FILE_NAME = "alarms.json";

    public static void saveAlarms(Context context, List<TimeAlarm> alarmList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(alarmList);

        try (FileWriter writer = new FileWriter(new File(context.getFilesDir(), FILE_NAME))) {
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<TimeAlarm> loadAlarms(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) return null;

        Gson gson = new Gson();
        Type listType = new TypeToken<List<TimeAlarm>>() {}.getType();

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
