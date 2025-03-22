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

    // Lưu danh sách báo thức
    public static void saveAlarms(Context context, List<TimeAlarm> alarmList) {
        File file = new File(context.getFilesDir(), ALARM_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(alarmList, writer);
            writer.flush();
            Log.d(TAG, "Đã lưu danh sách báo thức!");
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi lưu báo thức!", e);
        }
    }

    // Đọc danh sách báo thức
    public static List<TimeAlarm> loadAlarms(Context context) {
        File file = new File(context.getFilesDir(), ALARM_FILE_NAME);
        if (!file.exists()) return new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<TimeAlarm>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc báo thức!", e);
            return new ArrayList<>();
        }
    }

    public static List<String> loadAlarmsId(Context context) {
        File file = new File(context.getFilesDir(), ALARM_IDS_FILE_NAME);
        if (!file.exists()) return new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<TimeAlarm>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc id báo thức!", e);
            return new ArrayList<>();
        }
    }

    // Lưu danh sách ID báo thức
    public static void saveAlarmIds(Context context, List<String> alarmIds) {
        File file = new File(context.getFilesDir(), ALARM_IDS_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(alarmIds, writer);
            writer.flush();
            Log.d(TAG, "Đã lưu danh sách ID báo thức: " + alarmIds);
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi lưu ID báo thức!", e);
        }
    }

    // Đọc danh sách ID báo thức
    public static List<String> getAllAlarmIds(Context context) {
        File file = new File(context.getFilesDir(), ALARM_IDS_FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            Log.e(TAG, "Lỗi khi đọc ID báo thức!", e);
            return new ArrayList<>();
        }
    }

    // Thêm ID báo thức
    public static void addAlarmId(Context context, String alarmId) {
        List<String> alarmIds = getAllAlarmIds(context);
        if (!alarmIds.contains(alarmId)) {
            alarmIds.add(alarmId);
            saveAlarmIds(context, alarmIds);
        }
    }

    /** Xóa ID báo thức */
    public static void removeAlarmId(Context context, String alarmId) {
        List<String> alarmIds = getAllAlarmIds(context);
        if (alarmIds.remove(alarmId)) {
            saveAlarmIds(context, alarmIds);
        }
    }
}
