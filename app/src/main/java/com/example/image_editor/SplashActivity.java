package com.example.image_editor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;

import com.example.image_editor.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    //for fetching data
    SharedPreferences sharedPreferences;
    //so create a sharedpreferences name and also create key name
    private static final String SHARED_PREF_NAME = "User";
    private static final String KEY_VALUE = "Value";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if (sharedPreferences.contains(KEY_VALUE)) {
                    String email = sharedPreferences.getString(KEY_VALUE, null);
                    //10.if data is available goto home page
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                    finish();
                }
            }
        },5000);
    }

    @Override
    public void onBackPressed() {
    }
}