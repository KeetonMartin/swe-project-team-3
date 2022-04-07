package com.example.scholarshipr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvScholarships;
    protected List<ScholarshipData> allScholarships;
    protected SearchView searchView;
    protected ScholarshipAdapter scholarshipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdata);

        rvScholarships = findViewById(R.id.rvScholarships);
        allScholarships = new ArrayList<>();
        swipeContainer = findViewById(R.id.swipeContainer);
        rvScholarships.setLayoutManager(new LinearLayoutManager(this));
        // Initialize an adapter
        scholarshipAdapter = new ScholarshipAdapter(this, allScholarships);
        // Set the adapter on the recycler view
        rvScholarships.setAdapter(scholarshipAdapter);
        // Set a layout manager on the recycler view
        rvScholarships.setLayoutManager(new LinearLayoutManager(this));
        getAllScholarships();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refreshing scholarships...");
                // Clear out old items before appending in the new ones
                scholarshipAdapter.clear();
                // Get and add new items to adapter
                getAllScholarships();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void getAllScholarships() {
        //TextView tv = findViewById(R.id.statusField);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                        try {
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"

                            URL url = new URL("http://10.0.2.2:3000/api");

                            Log.v("debug", "Trying to make a connection to " + url);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Log.v("debug", "We connected. Connection: " + conn);


                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();
                            String name;
                            JSONObject jso = new JSONObject(response);

                            Log.v("debug", "Here's the JSON we got: " + jso);

                            Iterator<String> keys = (Iterator<String>) jso.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject value = jso.getJSONObject(key);
                                name = value.getString("name");
                                ScholarshipData scholarship = new ScholarshipData(name);
                                allScholarships.add(scholarship);
                                scholarshipAdapter.notifyDataSetChanged();

                            }
                            //String org;
                            //int amount;
                            //String date;



                            // need to set the instance variable in the Activity object
                            // because we cannot directly access the TextView from here

                            /*amount = jo.getInt("Dollar Amount");
                            date = jo.getString("Due Date");
                            org = jo.getString("Organization");*/

                        } catch (Exception e) {
                            e.toString();
                        }
                    }
            );

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

            // now we can set the status in the TextView
            //tv.setText(message);
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
        }
    }
}
