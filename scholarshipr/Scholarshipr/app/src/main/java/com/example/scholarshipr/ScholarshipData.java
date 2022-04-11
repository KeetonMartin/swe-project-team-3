package com.example.scholarshipr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.ZonedDateTime;


public class ScholarshipData extends AppCompatActivity {
    public String name;
    public String org;
    public String description;
    public int amount;
    public String approvalStatus;
    public ZonedDateTime dueDate;
    public float gpaRequirement;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdata);

        // Set up a listener on the Suggest Scholarship button
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btnSuggestScholarship);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSuggestScholarshipActivity();
            }
        });

    }

    public static final int SUGGEST_SCHOLARSHIP_ACTIVITY_ID = 1;

    private void openSuggestScholarshipActivity() {
        // Create an Intent
        Intent i = new Intent(this, SuggestScholarshipActivity.class);
        Log.v("debug", "New Scholarship button was clicked.");
        startActivityForResult(i, SUGGEST_SCHOLARSHIP_ACTIVITY_ID);
    }

    // Constructor for a ScholarshipData
    public ScholarshipData(String name, String org, String description, int amount,
                           String approvalStatus, ZonedDateTime dueDate, Float gpaRequirement,
                           String id) {
        this.name = name;
        this.org = org;
        this.description = description;
        this.amount = amount;
        this.approvalStatus = approvalStatus;
        this.dueDate = dueDate;
        this.gpaRequirement = gpaRequirement;
        this.id = id;
    }

    public ScholarshipData() {
    }

    public ScholarshipData(String name){
        this.name = name;
    }

    public ScholarshipData(){}

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getOrg(){
        return org;
    }
    public int getAmount(){
        return amount;
    }
    public String getApprovalStatus(){
        return approvalStatus;
    }
    public ZonedDateTime getDueDate(){
        return dueDate;
    }
    public Float getGpaRequirement(){
        return gpaRequirement;
    }
    public String getId(){
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setOrg(String org) {
        this.org = org;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }
    public void setGpaRequirement(Float gpaRequirement) {
        this.gpaRequirement = gpaRequirement;
    }
    public void setId(String id) {
        this.id = id;
    }

}
