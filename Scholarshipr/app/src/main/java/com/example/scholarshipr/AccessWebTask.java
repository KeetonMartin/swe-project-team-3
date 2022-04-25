package com.example.scholarshipr;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {

    @Override
    protected String doInBackground(URL... urls) {

        try {
            URL url = urls[0];

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner in = new Scanner(url.openStream());
            String response = in.nextLine();

            JSONObject jo = new JSONObject(response);
            String message = jo.getString("message");
            return message;

        }
        catch (Exception e) {
            return e.toString();
        }

    }

    @Override
    protected void onPostExecute(String s) {
        // this method would be called in the UI thread after doInBackground finishes
        // it can access the Views and update them asynchronously
    }

}
