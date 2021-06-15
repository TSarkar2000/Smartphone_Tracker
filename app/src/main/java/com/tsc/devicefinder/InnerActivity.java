package com.tsc.devicefinder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tsc.devicefinder.fragments.DeviceFragment;
import com.tsc.devicefinder.fragments.MapFragment;

public class InnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(listener);

        // select device fragment when activity is launched
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DeviceFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        Fragment selected = (item.getItemId() == R.id.device_menu) ? new DeviceFragment(): new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
        return true;
    };
}