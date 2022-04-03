package com.example.scholarshipr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvScholarships;
    protected List<ScholarshipData> allScholarships;
    protected SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdata);

        rvScholarships = findViewById(R.id.rvScholarships);
        allScholarships = new ArrayList<>();
        swipeContainer = findViewById(R.id.swipeContainer);
        rvScholarships.setLayoutManager(new LinearLayoutManager(this));


    }
}