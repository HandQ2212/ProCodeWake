package com.example.procodewake2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewBaoThuc;
    List<TimeAlarm> alarmList;
    TimeAlarmAdapter adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo ListView
        listViewBaoThuc = findViewById(R.id.listview_bao_thuc);
        // Đọc danh sách báo thức từ file JSON
        alarmList = JsonHelper.loadAlarms(this);
        if (alarmList == null) {
            alarmList = new ArrayList<>();
        }
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
                    Intent intent = new Intent(MainActivity.this, ThemCauHoiActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Đăng ký ActivityResultLauncher để nhận kết quả từ DatBaoThucActivity
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

                            if (alarmList == null) {
                                alarmList = new ArrayList<>();
                            }

                            TimeAlarm timeAlarm = new TimeAlarm(hour, minute, selectedDays, true, soundPath, topic);
                            alarmList.add(timeAlarm);
                            adapter.notifyDataSetChanged();
                            //Lưu lại danh sách báo thức
                            JsonHelper.saveAlarms(MainActivity.this, alarmList);
                        }
                    }
                });

        //Đặt nút báo thức
        Button btnDatBaoThuc = findViewById(R.id.btnDatBaoThuc);
        btnDatBaoThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatBaoThucActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }
}
