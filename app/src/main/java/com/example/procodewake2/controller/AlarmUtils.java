package com.example.procodewake2.controller;

import android.content.Context;

import com.example.procodewake2.model.TimeAlarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AlarmUtils {
    private static final String FILE_NAME = "alarms.json";

    public static List<TimeAlarm> getAllAlarms(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<TimeAlarm>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveAlarms(Context context, List<TimeAlarm> alarms) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(alarms, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
