package com.example.scholarshipr;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.scholarshipr.databinding.ActivityAddScholarshipBinding;
import com.google.android.material.snackbar.Snackbar;


public class ScholarshipData {
    public static final String KEY_Name = "ScholarshipName";
    public static final int KEY_Amount = 0;
    public static final String KEY_Description = "Description";

    public String getScholarshipName(){
        return KEY_Name;
    }


    binding.btnAdd.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            // should open AddScholarship activity
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    });

}
