package com.example.procodewake2.controller;

import android.content.Context;
import android.util.Log;

import com.example.procodewake2.model.TimeAlarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AlarmUtils {
    private static final String ALARMS_FILE_NAME = "alarms.json";
    private static final String ALARM_IDS_FILE_NAME = "alarm_ids.json";
    private static final String TAG = "AlarmUtils"; // Tag cho Log

    public static List<String> getAllAlarmIds(Context context) {
        File file = new File(context.getFilesDir(), ALARM_IDS_FILE_NAME);

        if (!file.exists()) {
            Log.e(TAG, "Tệp " + ALARM_IDS_FILE_NAME + " không tồn tại!");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> alarmIds = new Gson().fromJson(reader, listType);

            if (alarmIds == null || alarmIds.isEmpty()) {
                Log.e(TAG, "⚠️ Tệp tồn tại nhưng không có báo thức nào!");
                return new ArrayList<>();
            }

            Log.d(TAG, "Danh sách báo thức: " + alarmIds);
            return alarmIds;
        } catch (IOException e) {
            Log.e(TAG, "Lỗi đọc tệp báo thức!", e);
            return new ArrayList<>();
        }
    }


    public static void saveAlarmIds(Context context, List<String> alarmIds) {
        File file = new File(context.getFilesDir(), ALARM_IDS_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(alarmIds, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TimeAlarm getAlarmById(Context context, String id) {
        List<TimeAlarm> alarms = getAllAlarms(context);
        for (TimeAlarm alarm : alarms) {
            if (alarm.getId().equals(id)) {
                return alarm;
            }
        }
        return null;
    }

    public static List<TimeAlarm> getAllAlarms(Context context) {
        File file = new File(context.getFilesDir(), ALARMS_FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<TimeAlarm>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAlarms(Context context, List<TimeAlarm> alarms) {
        File file = new File(context.getFilesDir(), ALARMS_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(alarms, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}