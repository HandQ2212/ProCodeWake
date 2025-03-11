package com.example.procodewake2;

import android.net.Uri;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class TimeAlarm{
    private Integer hour, minute;
    private ArrayList<DayOfWeek> cacNgayDatBaoThuc;
    private boolean duocBat;
    private Uri alarmSound;

    public TimeAlarm(Integer hour, Integer minute, ArrayList<DayOfWeek> cacNgayDatBaoThuc, boolean duocBat, Uri alarmSound) {
        this.hour = hour;
        this.minute = minute;
        this.cacNgayDatBaoThuc = cacNgayDatBaoThuc;
        this.duocBat = duocBat;
        this.alarmSound = alarmSound;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public ArrayList<DayOfWeek> getCacNgayDatBaoThuc() {
        return cacNgayDatBaoThuc;
    }

    public void setCacNgayDatBaoThuc(ArrayList<DayOfWeek> cacNgayDatBaoThuc) {
        this.cacNgayDatBaoThuc = cacNgayDatBaoThuc;
    }

    public boolean isDuocBat() {
        return duocBat;
    }

    public void setDuocBat(boolean duocBat) {
        this.duocBat = duocBat;
    }

    public Uri getAlarmSound() {
        return alarmSound;
    }

    public void setAlarmSound(Uri alarmSound) {
        this.alarmSound = alarmSound;
    }
}