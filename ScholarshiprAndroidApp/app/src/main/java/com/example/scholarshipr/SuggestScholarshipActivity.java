package com.example.scholarshipr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /create endpoint that creates a scholarship

                    URL url = new URL("http://10.0.2.2:3000/create?name="+name+"&org="+org+"&description="+description+"&dollarAmount="+amount+"&approvalStatus=false&dueDate="+deadline+"&gpaRequirement="+gpa);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Log.v("debug", "We connected. Connection: " + conn);

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    Log.v("debug", "Response from server: " + response);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
        }
    }
}
