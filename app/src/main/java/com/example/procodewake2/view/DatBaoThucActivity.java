package com.example.procodewake2.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.procodewake2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatBaoThucActivity extends AppCompatActivity {
    EditText timeHour, timeMinute;
    Button setTime, setAlarm;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    TextView tvLapLai, tvAmThanh, tvChuDe;

    String[] days = {"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
    String[] chuDe = {"Code", "Toán", "Tiếng Anh", "Khác"};

    List<JSONObject> soundList = new ArrayList<>();
    String selectedSoundPath = "mac_dinh.mp3"; // Đường dẫn âm thanh đã chọn
    ArrayList<Integer> selectedIndexes;
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

        loadSoundsFromAssets(); // Đọc danh sách âm thanh từ assets

        setTime.setOnClickListener(v -> showTimePickerDialog());
        tvLapLai.setOnClickListener(v -> showRepeatDialog());
        tvAmThanh.setOnClickListener(v -> showSoundSelectionDialog());
        tvChuDe.setOnClickListener(v -> showTopicSelectionDialog());
        setAlarm.setOnClickListener(v -> saveAlarm());
    }

    private void loadSoundsFromAssets() {
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("sound.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                soundList.add(jsonArray.getJSONObject(i));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTimePickerDialog() {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minutes) -> {
            timeHour.setText(String.format("%02d", hourOfDay));
            timeMinute.setText(String.format("%02d", minutes));
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }

    private void showRepeatDialog() {
        boolean[] selectedDays = new boolean[days.length];
        selectedIndexes = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ngày lặp lại");
        builder.setMultiChoiceItems(days, selectedDays, (dialog, which, isChecked) -> {
            if (isChecked) selectedIndexes.add(which);
            else selectedIndexes.remove(Integer.valueOf(which));
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selectedIndexes.isEmpty()) {
                tvLapLai.setText("Không lặp");
            } else {
                StringBuilder selectedText = new StringBuilder();
                for (int index : selectedIndexes) {
                    selectedText.append(days[index]).append(", ");
                }
                tvLapLai.setText(selectedText.substring(0, selectedText.length() - 2));
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showSoundSelectionDialog() {
        if (soundList.isEmpty()) return;

        String[] soundNames = new String[soundList.size()];
        for (int i = 0; i < soundList.size(); i++) {
            try {
                soundNames[i] = soundList.get(i).getString("nameSound");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn âm thanh");

        int[] selectedSoundIndex = {-1};
        builder.setSingleChoiceItems(soundNames, selectedSoundIndex[0], (dialog, which) -> {
            selectedSoundIndex[0] = which;
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selectedSoundIndex[0] != -1) {
                try {
                    selectedSoundPath = soundList.get(selectedSoundIndex[0]).getString("alarmSoundPath");
                    tvAmThanh.setText(soundNames[selectedSoundIndex[0]]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showTopicSelectionDialog() {
        int[] selectedTopicIndex = {-1};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn chủ đề");
        builder.setSingleChoiceItems(chuDe, selectedTopicIndex[0], (dialog, which) -> selectedTopicIndex[0] = which);

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selectedTopicIndex[0] != -1) {
                tvChuDe.setText(chuDe[selectedTopicIndex[0]]);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void saveAlarm() {
        int hour = Integer.parseInt(timeHour.getText().toString());
        int minute = Integer.parseInt(timeMinute.getText().toString());

        boolean[] selectedDaysArray = new boolean[days.length];
        for (int index : selectedIndexes) {
            selectedDaysArray[index] = true; // Đánh dấu ngày được chọn
        }

        String topic = tvChuDe.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("hour", hour);
        resultIntent.putExtra("minute", minute);
        resultIntent.putExtra("days", selectedDaysArray);
        resultIntent.putExtra("sound", selectedSoundPath);
        resultIntent.putExtra("topic", topic);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
