package com.example.scholarshipr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
    private static final String CHANNEL_ID = "888";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvScholarships;
    protected ArrayList<ScholarshipData> allScholarships;
    protected SearchView searchView;
    protected ScholarshipAdapter scholarshipAdapter;
    protected JSONArray jso;

    public static final int SUGGEST_SCHOLARSHIP_ACTIVITY_ID = 1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdata);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        createNotificationChannel();

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

                List<String> oldShipNames = scholarshipAdapter.getScholarshipNames();

                // Clear out old items before appending in the new ones
                scholarshipAdapter.clear();
                // Get and add new items to adapter
                getAllScholarships();

                List<String> newShipNames = scholarshipAdapter.getScholarshipNames();

                if (newShipDetected(oldShipNames, newShipNames)) {
                    Log.i(TAG, "Found some new 'ships!");
                    notifyOfNewShip();
                }

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

    private boolean newShipDetected(List<String> oldShips, List<String> newShips) {
        for (String shipName :
                newShips) {
            if (! oldShips.contains(shipName)) {
                return true;
            }
        }
        return false;
    }
    
    private void notifyOfNewShip() {
        String message = "New Scholarship Added!";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_suggest_scholarship_button)
                .setContentTitle("New Scholarships Added!")
                .setContentText("New scholarships have been added to the list!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("New Scholarships Added to Database"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(811, builder.build());


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                filterNoGPA();
                break;
            case R.id.GPA1:
                filter0GPA1();
                break;
            case R.id.GPA2:
                filter1GPA2();
                break;
            case R.id.GPA3:
                filter2GPA3();
                break;
            case R.id.GPA4:
                filter3GPAUP();
                break;
            case R.id.clear_text:
                filter("");
                break;
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

    private void filterNoGPA() {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement() == 0) {
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

    private void filter1GPA2() {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement() < 2.0 && scholarship.getGpaRequirement() > 1.0) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }

    private void filter2GPA3() {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement() < 3.0 && scholarship.getGpaRequirement() >= 2.0) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }

    private void filter0GPA1() {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement() < 1.0 && scholarship.getGpaRequirement() > 0) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }

    private void filter3GPAUP() {
        List<ScholarshipData> filteredList = new ArrayList<>();
        for (ScholarshipData scholarship : allScholarships) {
            if (scholarship.getGpaRequirement() > 3.0) {
                filteredList.add(scholarship);
            }
        }
        scholarshipAdapter.filterList(filteredList);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
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
            executor.awaitTermination(2, TimeUnit.SECONDS);
            ArrayList<ScholarshipData> scholarships = new ArrayList<>();
            String name;
            String org;
            String descrip;
            int amount;
            String status;
            ZonedDateTime date;
            float gpa;

            Log.v("debug", "Connection has not timed out");
            for (int i = 0; i < jso.length(); i++) {
                JSONObject value = jso.getJSONObject(i);
                Log.v("debug", "This scholarship:" + value);

                name = value.getString("name");
                Log.v("debug", "Value of Name:" + name);

                status = value.getString("approvalStatus");
                Log.v("debug", "Value of Status:" + status);
                if (!status.equals("true")) {
                    Log.v("debug", "Skipping over unapproved scholarship \""+name+"\".");
                    continue;
                }

                if(value.isNull("org")){
                    org = " ";
                }else{
                    org = value.getString("org");
                }

                Log.v("debug", "Value of Org:" + org);

                if(value.isNull("description")){
                    descrip = " ";
                }else{
                    descrip = value.getString("description");
                }
                Log.v("debug", "Value of Descrip:" + descrip);

                if(value.isNull("dueDate")){
                    date = null;

                }else{
                    date = ZonedDateTime.parse(value.getString("dueDate"));
                }

                if(value.isNull("gpaRequirement")){
                    gpa = 0;
                    Log.v("debug", "Value of GPA" + gpa);
                }else{
                    Log.v("debug", "Value of GPA");
                    gpa = (float) value.getDouble("gpaRequirement");
                }
                Log.v("debug", "Value of Name:" + gpa);
                if(value.isNull("dollarAmount")){
                    amount = 0;
                }else{
                    amount = value.getInt("dollarAmount");
                }
                Log.v("debug", "Value of Name:" + amount);


                scholarship = new ScholarshipData(name,org,descrip,amount,status,date,gpa);
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

            Log.v("debug", "Attempting to check stuff");
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
