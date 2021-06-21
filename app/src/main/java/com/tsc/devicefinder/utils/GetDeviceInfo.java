package com.tsc.devicefinder.utils;

import android.os.Build;

import com.google.gson.Gson;

public class GetDeviceInfo {

    public String toJson(String owner) {
        return new Gson().toJson(new DeviceInfo(owner));
    }

    class DeviceInfo {
        String owner = "";

        public DeviceInfo(String owner) {
            this.owner = owner;
        }

        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        int os = Build.VERSION.SDK_INT;
    }
}
