package com.tsc.devicefinder.core;

import android.os.AsyncTask;

import com.tsc.devicefinder.utils.Events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Auth {

    private String name;
    private final String email;
    private final String password;
    private final String baseUrl = "http://192.168.0.100/dashboard/";
    private final Events events = Events.getInstance();
    private final int flag;

    public Auth(String email, String password) {
        flag = 1;
        this.email = email;
        this.password = password;
    }

    public Auth(String name, String email, String password) {
        flag = 0;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void begin() {
        String url = "";
        if (flag == 1)
            url += baseUrl + "/login.php?email=" + email + "&password=" + password;
        else
            url += baseUrl + "/registration.php?name=" + name + "&email=" + email + "&password=" + password;

        new Task().execute(url);
    }

    class Task extends AsyncTask<String, Void, Void> {

        private String data = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            events.fireAuthMessageEvent(data, data.contains("SUCCESS") ? flag : -1);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String x = "";
                while ((x = br.readLine()) != null)
                    data += x;
                br.close();
                urlConnection.disconnect();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }
    }
}
