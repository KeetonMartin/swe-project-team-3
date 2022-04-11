package com.example.scholarshipr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class SuggestScholarshipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggestscholarship);

        getSupportActionBar().setTitle("Suggest a Scholarship");

        // Set up a listener on the Send Suggestion button
        final Button button = (Button) findViewById(R.id.btnSendSuggestion);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSuggestion();
            }
        });

    }

    public void sendSuggestion() {
        Log.v("debug", "Submit button was clicked!!!! WOW!");
        final EditText nameField = (EditText) findViewById(R.id.editTextScholarshipName);
        String name = nameField.getText().toString();
        final EditText orgField = (EditText) findViewById(R.id.editTextScholarshipOrganization);
        String org = orgField.getText().toString();
        final EditText descriptionField = (EditText) findViewById(R.id.editTextScholarshipDescription);
        String description = descriptionField.getText().toString();
        final EditText amountField = (EditText) findViewById(R.id.editTextAwardAmount);
        String amount = amountField.getText().toString();
        final EditText deadlineField = (EditText) findViewById(R.id.editTextScholarshipDeadline);
        String deadline = deadlineField.getText().toString();
        final EditText gpaField = (EditText) findViewById(R.id.editTextScholarshipGpaRequirement);
        String gpa = gpaField.getText().toString();

        Log.v("debug", "name: " + name + " org: " + org + " description: "+ description + " amount: " + amount + " deadline: " + deadline + " gpa: "+ gpa);
    }

}