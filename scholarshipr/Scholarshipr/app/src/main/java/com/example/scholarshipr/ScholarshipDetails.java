package com.example.scholarshipr;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScholarshipDetails extends AppCompatActivity {
    private TextView tvScholarshipInfo;
    private ScholarshipData scholarship;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tvScholarshipName = findViewById(R.id.tvScholarshipName);
        //String name = tvScholarshipName.getText().toString();
        setContentView(R.layout.activity_scholarshipdetails);
        tvScholarshipInfo = findViewById(R.id.tvScholarshipInfo);
        //scholarship = Parcels.unwrap(getIntent().getParcelableExtra("scholarship"));
        //String name = scholarship.getName();
        //Log.v("debug", "Attempting to access data" + name);
        Bundle extras = getIntent().getExtras();
        tvScholarshipInfo.setText(extras.getString("scholarship"));
    }
}

