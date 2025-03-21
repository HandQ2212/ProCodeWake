package com.example.procodewake2.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.procodewake2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThemCauHoiActivity extends AppCompatActivity {
    private Spinner spinnerTopic;
    private EditText edtQuestion, edtAnswer;
    private Button btnAddQuestion;
    private static final String FILE_NAME = "questions.json";
    private String selectedTopic = "Code"; // Chủ đề mặc định
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_cac_cau_hoi);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_them_cau_hoi);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_xem_bao_thuc) {
                    Intent intent = new Intent(ThemCauHoiActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.nav_them_cau_hoi) {
                    return true;
                }
                return false;
            }
        });

        spinnerTopic = findViewById(R.id.spinnerTopic);
        edtQuestion = findViewById(R.id.edtQuestion);
        edtAnswer = findViewById(R.id.edtAnswer);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);

        // Thiết lập danh sách chủ đề cho Spinner
        String[] topics = {"Code", "Toán", "Tiếng Anh", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, topics);
        spinnerTopic.setAdapter(adapter);

        btnAddQuestion.setOnClickListener(v -> {
            selectedTopic = spinnerTopic.getSelectedItem().toString();
            addQuestion();
        });
    }

    private void addQuestion() {
        String question = edtQuestion.getText().toString().trim();
        String answer = edtAnswer.getText().toString().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ câu hỏi và câu trả lời!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonObject = readJsonFile();
            JSONArray questionArray = jsonObject.optJSONArray(selectedTopic);
            if (questionArray == null) {
                questionArray = new JSONArray();
            }

            JSONObject newQuestion = new JSONObject();
            newQuestion.put("question", question);
            newQuestion.put("answer", answer);

            questionArray.put(newQuestion);
            jsonObject.put(selectedTopic, questionArray);

            writeJsonFile(jsonObject);

            Toast.makeText(this, "Đã thêm câu hỏi vào " + selectedTopic, Toast.LENGTH_SHORT).show();
            edtQuestion.setText("");
            edtAnswer.setText("");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu câu hỏi!", Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject readJsonFile() {
        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) return new JSONObject();

        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return new JSONObject(new String(data));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private void writeJsonFile(JSONObject jsonObject) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(jsonObject.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}