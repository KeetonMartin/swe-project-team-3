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

    // Constructor for a ScholarshipData
    public ScholarshipData(String name, String org, String description, int amount,
                           String approvalStatus, ZonedDateTime dueDate, Float gpaRequirement) {
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


    // Overriding equals() to compare two ScholarshipData objects
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of ScholarshipData or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof ScholarshipData)) {
            return false;
        }

        // typecast o to ScholarshipData so that we can compare data members
        ScholarshipData c = (ScholarshipData) o;

        // Compare the data members and return accordingly
        return name.equals(c.name)
                && org.equals(c.org)
                && description.equals(c.description)
                && amount == c.amount
                && approvalStatus.equals(c.approvalStatus)
                && dueDate.equals(c.dueDate)
                && gpaRequirement == c.gpaRequirement;
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

}
