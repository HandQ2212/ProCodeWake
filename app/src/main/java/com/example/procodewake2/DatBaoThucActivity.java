package com.example.procodewake2;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class DatBaoThucActivity extends AppCompatActivity {
    EditText timeHour, timeMinute;
    Button setTime, setAlarm;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    TextView tvLapLai, tvAmThanh, tvChuDe;

    // Danh sách ngày trong tuần
    String[] days = {"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
    // Danh sách chủ đề
    String[] chuDe ={"Code", "Toán", "Tiếng Anh", "Khác"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dat_bao_thuc);
        timeHour = findViewById(R.id.etHour);
        timeMinute = findViewById(R.id.etMinute);
        setTime = findViewById(R.id.btnTime);
        setAlarm = findViewById(R.id.btnAlarm);
        tvLapLai = findViewById(R.id.tvLapLai);
        tvAmThanh = findViewById(R.id.tvAmThanh);
        tvChuDe = findViewById(R.id.tvChuDe);

        //Thêm thời gian
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(DatBaoThucActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        timeHour.setText(String.format("%02d", hourOfDay));
                        timeMinute.setText(String.format("%02d", minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        //Chọn chế độ lặp lại
        boolean[] selectedDays = new boolean[days.length];
        ArrayList<Integer> selectedIndexes = new ArrayList<>();
        tvLapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DatBaoThucActivity.this);
                builder.setTitle("Chọn ngày lặp lại");

                builder.setMultiChoiceItems(days, selectedDays, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selectedIndexes.add(which);
                    }
                    else {
                        selectedIndexes.remove(Integer.valueOf(which));
                    }
                });

                builder.setPositiveButton("OK", (dialog, which) -> {
                    if (selectedIndexes.isEmpty()) {
                        tvLapLai.setText("Không lặp");
                    }
                    else {
                        StringBuilder selectedText = new StringBuilder();
                        for (int index : selectedIndexes) {
                            selectedText.append(days[index]).append(", ");
                        }
                        tvLapLai.setText(selectedText.substring(0, selectedText.length() - 2)); // Xóa dấu ", " cuối
                    }
                });

                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

                builder.show();
            }
        });

        //Chọn sound

        //Chọn chủ đề câu hỏi
        boolean[] selectedChuDe = new boolean[chuDe.length];
        ArrayList<Integer> selected = new ArrayList<>();
        tvChuDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DatBaoThucActivity.this);
                builder.setTitle("Chọn chủ đề");
                builder.setMultiChoiceItems(chuDe, selectedChuDe, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selected.add(which);
                    }
                    else {
                        selected.remove(Integer.valueOf(which));
                    }
                });

                builder.setPositiveButton("OK", (dialog, which) -> {
                    if (selected.isEmpty()) {
                        tvChuDe.setText("Không lặp");
                    }
                    else {
                        StringBuilder selectedText = new StringBuilder();
                        for (int index : selected) {
                            selectedText.append(chuDe[index]).append(" hoặc ");
                        }
                        tvChuDe.setText(selectedText.substring(0, selectedText.length() - 6)); // Xóa " hoặc " cuối
                    }
                });

                builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

                builder.show();
            }
        });
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code thêm vào danh sách
                finish();
            }
        });
    }
}
