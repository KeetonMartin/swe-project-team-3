package com.example.scholarshipr;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scholarshipr.databinding.ActivityAddScholarshipBinding;

public class AddScholarship extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddScholarshipBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddScholarshipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

    }

    @Override
    public boolean onSupportNavigateUp() {

    }
}