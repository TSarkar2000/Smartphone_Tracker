package com.tsc.devicefinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tsc.devicefinder.core.Auth;
import com.tsc.devicefinder.core.Events;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);

        if (preferences.contains("email")) {
            Events.getInstance().registerAuthMessageListener(authMessageListener);
            new Auth(preferences.getString("email", ""), preferences.getString("password", "")).begin(getSupportFragmentManager());
        } else {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }
    }

    private final Events.AuthMessageListener authMessageListener = (message, extra) -> {
        if (message.contains("SUCCESS")) {
            Intent i = new Intent(this, InnerActivity.class);
            i.putExtra("data", message.replace("SUCCESS", ""));
            startActivity(i);
        }
        else
            startActivity(new Intent(this, AuthActivity.class));

        finish();
    };
}