package com.example.part2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.data.Defect;
import com.example.part2.ui.SafetyViewModel;

public class DetailActivity extends AppCompatActivity {

    private SafetyViewModel viewModel;
    private int currentCheckId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textDetails = findViewById(R.id.textDetails);
        TextView textDefects = findViewById(R.id.textDefects);
        Button buttonEmailReport = findViewById(R.id.buttonEmailReport);
        Button buttonDeleteCheck = findViewById(R.id.buttonDeleteCheck);
        Button buttonBackToList = findViewById(R.id.buttonBackToList);

        currentCheckId = getIntent().getIntExtra(MainActivity.EXTRA_CHECK_ID, -1);

        viewModel = new ViewModelProvider(this).get(SafetyViewModel.class);

        viewModel.getCheckById(currentCheckId).observe(this, item -> {
            if (item == null) return;

            String details = "Date: " + item.safetyCheck.getDate() + "\n" +
                    "Vehicle: " + item.safetyCheck.getVehicleRegistration() + "\n" +
                    "Driver: " + item.safetyCheck.getDriverName() + "\n" +
                    "Status: " + item.safetyCheck.getOverallStatus();

            textDetails.setText(details);

            StringBuilder defectsText = new StringBuilder();
            StringBuilder emailBody = new StringBuilder();

            for (Defect defect : item.defects) {
                defectsText.append("- ")
                        .append(defect.getDescription())
                        .append(" (")
                        .append(defect.getSeverity())
                        .append(")\n");

                emailBody.append("- ")
                        .append(defect.getDescription())
                        .append("\n");
            }

            textDefects.setText(defectsText.toString());

            buttonEmailReport.setOnClickListener(v -> {
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
                viewModel.deleteCheck(currentCheckId);
                finish();
            }
        });

        buttonBackToList.setOnClickListener(v -> finish());
    }
}