package com.tsc.devicefinder.core;

import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;

import com.tsc.devicefinder.fragments.LoadingDialog;
import com.tsc.devicefinder.utils.GetDeviceInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Auth {

    private FragmentManager manager;
    private String name, deviceID;
    private final String email;
    private final String password;
    private final Events events = Events.getInstance();
    private final int flag;

    public Auth(String email, String password) {
        flag = 1;
        this.email = email;
        this.password = password;
    }

    public Auth(String name, String email, String password, String deviceID) {
        flag = 0;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deviceID = deviceID;
    }

    private String buildParams() {
        StringBuilder params = new StringBuilder();
        if(flag == 1)
            params.append("email=").append(email).append("&password=").append(password);
        else
            params.append("name=").append(name).append("&email=").append(email).append("&password=").append(password)
                    .append("&deviceID=").append(deviceID).append("&deviceInfo=").append(new GetDeviceInfo().toJson(name));

        return params.toString();
    }

    public void begin(FragmentManager manager) {
        this.manager = manager;
        String url = "http://192.168.0.102/dashboard/";
        url += flag == 1 ? "login.php": "registration.php";

        new Task().execute(url, buildParams());
    }

    class Task extends AsyncTask<String, Void, Void> {

        private String msg = "";
        private LoadingDialog ldg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ldg = new LoadingDialog();
            ldg.show(manager, "abc");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            events.fireAuthMessageEvent(msg, msg.contains("SUCCESS") ? flag : -1);
            ldg.dismiss();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                byte[] data = strings[1].getBytes();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty( "Content-Length", data.length+"");
                try(DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream())) {
                    dos.write(data);
                }
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    String x = "";
                    while ((x = reader.readLine()) != null)
                        msg += x;
                }
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
