package com.tsc.devicefinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tsc.devicefinder.core.Events;
import com.tsc.devicefinder.utils.FragmentAdapter;

public class AuthActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FragmentAdapter fragmentAdapter;
    public ViewPager2 viewPager2;
    public SharedPreferences preferences;
    public String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Events.getInstance().registerAuthMessageListener(authMessageListener);

        viewPager2 = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Log In"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // callback for swiping
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private final Events.AuthMessageListener authMessageListener = (message, extra) -> {
        if (extra == -1)
            Snackbar.make(findViewById(R.id.mainLayout), message, 2000).show();
        else if (extra == 0)
            viewPager2.setCurrentItem(0);
        else {
            // save details
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
            // launch activity
            Intent i = new Intent(this, InnerActivity.class);
            i.putExtra("data", message.replace("SUCCESS", ""));
            startActivity(i);

            finish();
        }
    };
}