package com.example.scholarshipr;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScholarshipDetails extends AppCompatActivity {
    private TextView tvScholarshipInfo;
    private TextView tvGPA;
    private TextView tvAmount;
    private TextView tvDueDate;
    private TextView tvDescription;
    private ScholarshipData scholarship;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tvScholarshipName = findViewById(R.id.tvScholarshipName);
        //String name = tvScholarshipName.getText().toString();
        setContentView(R.layout.activity_scholarshipdetails);
        tvScholarshipInfo = findViewById(R.id.tvScholarshipInfo);
        tvGPA = findViewById(R.id.tvGPA);
        tvAmount = findViewById(R.id.tvAmount);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvDescription = findViewById(R.id.tvDescription);
        //scholarship = Parcels.unwrap(getIntent().getParcelableExtra("scholarship"));
        //String name = scholarship.getName();
        //Log.v("debug", "Attempting to access data" + name);
        Bundle extras = getIntent().getExtras();
        tvScholarshipInfo.setText(extras.getString("scholarshipName"));
        tvGPA.setText(String.valueOf(extras.getFloat("gpa")));
        //tvAmount.setText(extras.getInt("amount"));
        tvAmount.setText(String.valueOf(extras.getInt("amount")));
        tvDescription.setText(extras.getString("scholarshipDesc"));
    }
}

