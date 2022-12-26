package com.masnajiib27.diaryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.masnajiib27.diaryapp.dashboard.Dashboard;
import com.masnajiib27.diaryapp.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferences = getSharedPreferences("userdiary", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.getBoolean("autologin", false)) {
                    startActivity(new Intent(Splash.this, Dashboard.class));
                } else {
                    startActivity(new Intent(Splash.this, Signin.class));
                }
                finish();
            }
        }, 3000);
    }
}

