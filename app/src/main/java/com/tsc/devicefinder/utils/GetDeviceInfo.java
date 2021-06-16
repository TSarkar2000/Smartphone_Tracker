package com.tsc.devicefinder.utils;

import android.os.Build;

import com.google.gson.Gson;

public class GetDeviceInfo {

    public String toJson() {
        return new Gson().toJson(new DeviceInfo());
    }

    class DeviceInfo {
        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        int os = Build.VERSION.SDK_INT;
    }
}
