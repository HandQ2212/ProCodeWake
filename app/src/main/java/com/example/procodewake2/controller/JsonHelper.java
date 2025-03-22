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

public class JsonHelper {
    private static final String ALARM_FILE_NAME = "alarms.json";
    private static final String ALARM_IDS_FILE_NAME = "alarm_ids.json";
    private static final String TAG = "JsonHelper";
    private static final Gson gson = new Gson();

    // Lưu danh sách báo thức */
    public static void saveAlarms(Context context, List<TimeAlarm> alarmList) {
        saveToFile(context, ALARM_FILE_NAME, alarmList);
    }

    // Đọc danh sách báo thức */
    public static List<TimeAlarm> loadAlarms(Context context) {
        return loadFromFile(context, ALARM_FILE_NAME, new TypeToken<List<TimeAlarm>>() {}.getType());
    }

    // Lưu danh sách ID báo thức */
    public static void saveAlarmIds(Context context, List<String> alarmIds) {
        saveToFile(context, ALARM_IDS_FILE_NAME, alarmIds);
    }

    // Đọc danh sách ID báo thức */
    public static List<String> getAllAlarmIds(Context context) {
        return loadFromFile(context, ALARM_IDS_FILE_NAME, new TypeToken<List<String>>() {}.getType());
    }

    // Hàm chung để lưu danh sách vào file */
    private static <T> void saveToFile(Context context, String fileName, T data) {
        File file = new File(context.getFilesDir(), fileName);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
            writer.flush();
            Log.d(TAG, "Đã lưu dữ liệu vào " + fileName);
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi lưu file " + fileName, e);
        }
    }

    // Hàm chung để đọc danh sách từ file
    private static <T> List<T> loadFromFile(Context context, String fileName, Type type) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) return new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc file " + fileName, e);
            return new ArrayList<>();
        }
    }
}
