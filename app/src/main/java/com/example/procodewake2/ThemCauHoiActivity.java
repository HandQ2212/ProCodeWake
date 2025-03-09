package com.example.procodewake2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ThemCauHoiActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_cac_cau_hoi);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_them_cau_hoi); //highlight nav thêm câu hỏi
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_xem_bao_thuc){
                    Intent intent = new Intent(ThemCauHoiActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (itemId == R.id.nav_them_cau_hoi){
                    return true;
                }
                return false;
            }
        });
    }
}
