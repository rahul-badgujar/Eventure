package com.example.myeventsapp.activity_splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myeventsapp.activity_login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NOTE: we are not setting any content view for SplashScreenActivity
        // goto Login Activity after cold boot
        Intent intentToLoginScreen=new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(intentToLoginScreen);
        finish();
    }
}