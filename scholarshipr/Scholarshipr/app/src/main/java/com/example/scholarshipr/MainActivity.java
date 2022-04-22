package com.example.scholarshipr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;
import org.json.JSONArray;

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
    protected ArrayList<ScholarshipData> allScholarships;
    protected SearchView searchView;
    protected ScholarshipAdapter scholarshipAdapter;
    protected JSONArray jso;

    public static final int SUGGEST_SCHOLARSHIP_ACTIVITY_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdata);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

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

        // Set up a listener on the Suggest Scholarship button
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btnSuggestScholarship);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSuggestScholarshipActivity();
            }
        });

    }

    private void openSuggestScholarshipActivity() {
        // Create an Intent
        Intent i = new Intent(this, SuggestScholarshipActivity.class);
        Log.v("debug", "New Scholarship button was clicked.");
        startActivityForResult(i, SUGGEST_SCHOLARSHIP_ACTIVITY_ID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        // Set up search bar functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.NoGPA:
                filterNoGPA(0);
                break;
            case R.id.GPA:
                filterGPA();
        }
        return true;
    }

    private void filter(String newText) {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }

    private void filterNoGPA(int amount) {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement().equals(amount)) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }

    private void filterGPA() {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement() > 0) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }


    public void getAllScholarships() {
        //TextView tv = findViewById(R.id.statusField);


        ScholarshipData scholarship;

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                        try {
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"


                            URL url = new URL("http://10.0.2.2:3000/api");




                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Log.v("debug", "We connected. Connection: " + conn);


                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();

                            jso = new JSONArray(response);
                            Log.v("debug", "We connected. Json Objects: " + jso);

                            //JsonReader jsonReader = Json.createReader(...);
                            //JSONArray jso = JSONObject.getArray();
                            //jsonReader.close();

                            //Log.v("debug", "Here's the JSON we got: " + jso);
                            //String name;
                            //Iterator<String> keys = (Iterator<String>) jso.keys();

                            //Log.v("debug", "Attempting to access data" + jso.getJSONObject(0));

                            //Log.v("debug", "We read in all scholarship");
                            //String org;
                            //int amount;
                            //String date;
                            //Log.v("debug", "List of Scholarships: " + allScholarships);



                            // need to set the instance variable in the Activity object
                            // because we cannot directly access the TextView from here

                            /*amount = jo.getInt("Dollar Amount");
                            date = jo.getString("Due Date");
                            org = jo.getString("Organization");*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            );
            //Log.v("debug", "Connection has not timed out");

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(5, TimeUnit.SECONDS);
            ArrayList<ScholarshipData> scholarships = new ArrayList<>();
            String name;
            String org;
            String descrip;
            int amount;
            String status;
            String date;
            float gpa;

            Log.v("debug", "Connection has not timed out");
            for (int i = 0; i < jso.length(); i++) {
                JSONObject value = jso.getJSONObject(i);
                Log.v("debug", "Value of Value:" + value);
                name = value.getString("name");
                Log.v("debug", "Value of Name:" + name);
                org = value.getString("org");
                Log.v("debug", "Value of Name:" + org);
                descrip = value.getString("description");
                Log.v("debug", "Value of Name:" + descrip);
                status = value.getString("approvalStatus");
                Log.v("debug", "Value of Name:" + status);
                date = "";
                amount = value.getInt("dollarAmount");
                Log.v("debug", "Value of Name:" + amount);
                gpa = (float) value.getDouble("gpaRequirement");
                Log.v("debug", "Value of Name:" + gpa);

                scholarship = new ScholarshipData(name,org,descrip,amount,status,date,gpa);
                //scholarship = new ScholarshipData(name);
                scholarships.add(scholarship);
                Log.v("debug", "Attempting to add " + scholarship.getDescription());
                Log.v("debug", "Attempting to add " + name);
                Log.v("debug", "Attempting to add " + org);
                //Log.v("debug", "Attempting to add " + date);
                Log.v("debug", "Attempting to add " + gpa);
                //scholarshipAdapter.notifyDataSetChanged();
                //allScholarships.add(scholarship);
                //scholarshipAdapter.notifyDataSetChanged();
            }


            allScholarships.addAll(scholarships);
            scholarshipAdapter.notifyDataSetChanged();

            // now we can set the status in the TextView
            //tv.setText(message);
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
        }
    }

}
