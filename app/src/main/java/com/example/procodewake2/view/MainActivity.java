package com.example.procodewake2.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.procodewake2.controller.AlarmScheduler;
import com.example.procodewake2.controller.JsonHelper;
import com.example.procodewake2.R;
import com.example.procodewake2.model.TimeAlarm;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewBaoThuc;
    List<TimeAlarm> alarmList;
    TimeAlarmAdapter adapter;
    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewBaoThuc = findViewById(R.id.listview_bao_thuc);
        alarmList = JsonHelper.loadAlarms(this);
        if (alarmList == null) alarmList = new ArrayList<>();

        adapter = new TimeAlarmAdapter(this, alarmList);
        listViewBaoThuc.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_xem_bao_thuc);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_xem_bao_thuc) {
                    return true;
                } else if (item.getItemId() == R.id.nav_them_cau_hoi) {
                    startActivity(new Intent(MainActivity.this, ThemCauHoiActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Đăng ký để nhận kết quả từ DatBaoThucActivity
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            int hour = data.getIntExtra("hour", 0);
                            int minute = data.getIntExtra("minute", 0);
                            boolean[] selectedDays = data.getBooleanArrayExtra("days");
                            String soundPath = data.getStringExtra("sound");
                            String topic = data.getStringExtra("topic");

                            TimeAlarm timeAlarm = new TimeAlarm(hour, minute, selectedDays, true, soundPath, topic);
                            alarmList.add(timeAlarm);

                            sortAlarmList();
                            adapter.notifyDataSetChanged();

                            JsonHelper.saveAlarms(MainActivity.this, alarmList);
                            AlarmScheduler.scheduleAlarm(MainActivity.this, timeAlarm.getId());
                        }
                    }
                });

        Button btnDatBaoThuc = findViewById(R.id.btnDatBaoThuc);
        btnDatBaoThuc.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DatBaoThucActivity.class);
            activityResultLauncher.launch(intent);
        });

        listViewBaoThuc.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Xóa báo thức")
                    .setMessage("Bạn có chắc chắn muốn xóa báo thức này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        TimeAlarm alarmToRemove = alarmList.get(position);
                        alarmList.remove(position);
                        adapter.notifyDataSetChanged();

                        JsonHelper.saveAlarms(MainActivity.this, alarmList);
                        AlarmScheduler.cancelAlarm(MainActivity.this, alarmToRemove.getId());
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        });

        // Đặt lại tất cả báo thức khi mở ứng dụng
        AlarmScheduler.scheduleAllAlarms(this);
    }

    private void sortAlarmList() {
        Collections.sort(alarmList, (time1, time2) -> {
            if (time1.getHour() != time2.getHour()) {
                return Integer.compare(time1.getHour(), time2.getHour());
            }
            return Integer.compare(time1.getMinute(), time2.getMinute());
        });
    }
}
