package com.example.scholarshipr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.time.ZonedDateTime;


public class ScholarshipData extends AppCompatActivity {
    public static String name;
    public static String org;
    public static String description;
    public static int amount;
    public static String approvalStatus;
    public static ZonedDateTime dueDate;
    public static Float gpaRequirement;
    public static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarshipdata);
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

    public ScholarshipData(String name){
        this.name = name;
    }

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

    public static final int ADD_SCHOLARSHIP_ACTIVITY_ID = 1;

    public void onAddScholarshipButtonClick(View v) {
        // Create an Intent
        Intent i = new Intent(this, AddScholarshipActivity.class);
        Log.v("debug", "Start button was clicked.");
        startActivityForResult(i, ADD_SCHOLARSHIP_ACTIVITY_ID);
    }
}
