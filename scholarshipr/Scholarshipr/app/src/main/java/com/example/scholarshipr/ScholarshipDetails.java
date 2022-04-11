package com.example.scholarshipr;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScholarshipDetails extends AppCompatActivity {
    private TextView tvScholarshipName;
    private ScholarshipData scholarship;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdetails);
        tvScholarshipName = findViewById(R.id.tvScholarshipName);
        //tvScholarshipName.setText(scholarship.getName());
    }
}

