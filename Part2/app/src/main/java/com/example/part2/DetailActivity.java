package com.example.part2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.data.Defect;
import com.example.part2.ui.SafetyViewModel;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private SafetyViewModel viewModel;
    private int currentCheckId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.d(TAG, "onCreate");

        TextView textDetails = findViewById(R.id.textDetails);
        TextView textDefects = findViewById(R.id.textDefects);
        Button buttonEmailReport = findViewById(R.id.buttonEmailReport);
        Button buttonDeleteCheck = findViewById(R.id.buttonDeleteCheck);
        Button buttonBackToList = findViewById(R.id.buttonBackToList);

        currentCheckId = getIntent().getIntExtra(MainActivity.EXTRA_CHECK_ID, -1);
        Log.d(TAG, "Loading check id: " + currentCheckId);

        viewModel = new ViewModelProvider(this).get(SafetyViewModel.class);

        viewModel.getCheckById(currentCheckId).observe(this, item -> {
            if (item == null) {
                Log.d(TAG, "item was null");
                return;
            }

            String details = "Date: " + item.safetyCheck.getDate() + "\n" +
                    "Vehicle: " + item.safetyCheck.getVehicleRegistration() + "\n" +
                    "Driver: " + item.safetyCheck.getDriverName() + "\n" +
                    "Status: " + item.safetyCheck.getOverallStatus();

            textDetails.setText(details);

            StringBuilder defectsText = new StringBuilder();
            StringBuilder emailBody = new StringBuilder();

            if (item.defects.isEmpty()) {
                defectsText.append("No defects recorded.");
            } else {
                for (Defect defect : item.defects) {
                    defectsText.append("- ")
                            .append(defect.getDescription())
                            .append(" (")
                            .append(defect.getSeverity())
                            .append(")\n");

                    emailBody.append("- ").append(defect.getDescription()).append("\n");
                }
            }

            textDefects.setText(defectsText.toString());

            buttonEmailReport.setOnClickListener(v -> {
                Log.d(TAG, "Email report button clicked");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Safety Defect Report: " + item.safetyCheck.getVehicleRegistration());
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody.toString());
                startActivity(emailIntent);
            });
        });

        buttonDeleteCheck.setOnClickListener(v -> {
            if (currentCheckId != -1) {
                Log.d(TAG, "Delete button clicked");
                viewModel.deleteCheck(currentCheckId);
                Toast.makeText(this, "Check deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        buttonBackToList.setOnClickListener(v -> finish());
    }
}