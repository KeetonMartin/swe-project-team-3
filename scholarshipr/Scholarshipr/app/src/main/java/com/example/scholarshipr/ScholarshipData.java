package com.example.scholarshipr;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class ScholarshipData extends AppCompatActivity {
    public static final String KEY_Name = "ScholarshipName";
    public static final String KEY_Info = "ScholarshipInfo";
    public static final int KEY_Amount = 0;
    public static final String KEY_Description = "Description";

    public String getScholarshipName(){
        return KEY_Name;
    }
    public String getScholarshipDescrip(){
        return KEY_Info;
    }

}
