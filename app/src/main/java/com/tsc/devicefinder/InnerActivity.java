package com.tsc.devicefinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc.devicefinder.fragments.DeviceFragment;
import com.tsc.devicefinder.fragments.MapFragment;
import com.tsc.devicefinder.utils.Device;
import com.tsc.devicefinder.utils.User;

import java.util.ArrayList;
import java.util.List;

public class InnerActivity extends AppCompatActivity {

    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(listener);

        // select device fragment when activity is launched
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DeviceFragment()).commit();

        // check if permission to access location is granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            reqPermission();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        Fragment selected = (item.getItemId() == R.id.device_menu) ? new DeviceFragment(): new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
        return true;
    };

    private void reqPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("You must provide location access to track devices.")
                    .setPositiveButton("Fine", (dialog, which) -> {
                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
                    })
                    .setNegativeButton("Nope", (dialog, which) -> {
                        Snackbar.make(findViewById(R.id.container), "You won't be able to access maps until you grant location access.", 3000).show();
                    })
                    .create().show();
        } else ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
            Snackbar.make(findViewById(R.id.container), "You won't be able to access maps until you grant location access.", 3000).show();
            bnv.getMenu().findItem(R.id.map_menu).setEnabled(false);
        } else bnv.getMenu().findItem(R.id.map_menu).setEnabled(true);
    }

    private static class GetJsonData {

        static List<Device> getDeviceList(String json) {
            JsonElement element = JsonParser.parseString(json);
            // get devices
            List<Device> devices = new ArrayList<>();
            JsonArray array = element.getAsJsonObject().get("device_info").getAsJsonArray();
            for(JsonElement e : array) {
                Device d = new Device();
                JsonObject obj = e.getAsJsonObject();
                d.setOwnerName(obj.get("owner").getAsString());
                d.setDeviceName(obj.get("model").getAsString());
                devices.add(d);
            }
            return devices;
        }

        static User getUserData(String json) {
            JsonElement element = JsonParser.parseString(json);
            // get current user info
            User u = new User();
            JsonObject obj = element.getAsJsonObject().get("user_info").getAsJsonObject();
            u.setName(obj.get("name").getAsString());
            u.setUid(obj.get("uid").getAsString());
            u.setDeviceCount(obj.get("device_count").getAsInt());

            return u;
        }
    }
}