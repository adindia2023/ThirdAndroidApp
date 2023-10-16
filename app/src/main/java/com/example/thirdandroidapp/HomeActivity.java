package com.example.thirdandroidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TextView tvTitle;
    private SharedPreferences sp;
    private String userName, userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
        getSharedData();
        setData();

    }

    private void setData() {
        tvTitle.setText(userName);
    }

    private void getSharedData() {
        userName = sp.getString("UserName","");
        userEmail = sp.getString("UserEmail", "");
    }

    private void initialize() {
        tvTitle = findViewById(R.id.tvTitle);
        sp = getSharedPreferences("LoginDetails", MODE_PRIVATE);
    }
}
