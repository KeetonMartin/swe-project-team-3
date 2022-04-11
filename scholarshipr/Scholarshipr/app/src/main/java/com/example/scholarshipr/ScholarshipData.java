package com.example.scholarshipr;

import androidx.appcompat.app.AppCompatActivity;

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
