package com.example.procodewake2.model;

import java.io.Serializable;
import java.util.UUID;

public class TimeAlarm implements Serializable {
    private String id;
    private int hour, minute;
    private boolean[] cacNgayDatBaoThuc;
    private boolean duocBat;
    private String alarmSoundPath;
    private String topic;

    public TimeAlarm(int hour, int minute, boolean[] cacNgayDatBaoThuc, boolean duocBat, String alarmSoundPath, String topic) {
        this.id = UUID.randomUUID().toString();
        this.hour = hour;
        this.minute = minute;
        this.cacNgayDatBaoThuc = cacNgayDatBaoThuc;
        this.duocBat = duocBat;
        this.alarmSoundPath = alarmSoundPath;
        this.topic = topic;
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean[] getCacNgayDatBaoThuc() {
        return cacNgayDatBaoThuc;
    }

    public void setCacNgayDatBaoThuc(boolean[] cacNgayDatBaoThuc) {
        this.cacNgayDatBaoThuc = cacNgayDatBaoThuc;
    }

    public boolean isDuocBat() {
        return duocBat;
    }

    public void setDuocBat(boolean duocBat) {
        this.duocBat = duocBat;
    }

    public String getAlarmSoundPath() {
        return alarmSoundPath;
    }

    public void setAlarmSoundPath(String alarmSoundPath) {
        this.alarmSoundPath = alarmSoundPath;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setId(String id) {
        this.id = id;
    }
}