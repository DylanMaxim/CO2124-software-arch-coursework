package com.example.part2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.data.Defect;
import com.example.part2.data.SafetyCheck;
import com.example.part2.ui.AddCheckViewModel;
import com.example.part2.ui.SafetyViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddCheckActivity extends AppCompatActivity {

    private EditText editVehicleReg, editDriverName, editDate, editDefectDescription, editDefectSeverity;
    private TextView textDefectList;
    private AddCheckViewModel formViewModel;
    private SafetyViewModel safetyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);

        editVehicleReg = findViewById(R.id.editVehicleReg);
        editDriverName = findViewById(R.id.editDriverName);
        editDate = findViewById(R.id.editDate);
        editDefectDescription = findViewById(R.id.editDefectDescription);
        editDefectSeverity = findViewById(R.id.editDefectSeverity);
        textDefectList = findViewById(R.id.textDefectList);

        Button buttonAddDefect = findViewById(R.id.buttonAddDefect);
        Button buttonSaveCheck = findViewById(R.id.buttonSaveCheck);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        formViewModel = new ViewModelProvider(this).get(AddCheckViewModel.class);
        safetyViewModel = new ViewModelProvider(this).get(SafetyViewModel.class);

        editVehicleReg.setText(formViewModel.vehicleReg);
        editDriverName.setText(formViewModel.driverName);
        editDate.setText(formViewModel.date);
        editDefectDescription.setText(formViewModel.defectDescription);
        editDefectSeverity.setText(formViewModel.defectSeverity);

        updateDefectListDisplay();

        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddCheckActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editDate.setText(formattedDate);
                        formViewModel.date = formattedDate;
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        buttonAddDefect.setOnClickListener(v -> {
            String description = editDefectDescription.getText().toString().trim();
            String severity = editDefectSeverity.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter a defect description", Toast.LENGTH_SHORT).show();
                return;
            }

            if (severity.isEmpty()) {
                Toast.makeText(this, "Please enter defect severity", Toast.LENGTH_SHORT).show();
                return;
            }

            formViewModel.pendingDefects.add(new Defect(0, description, severity));
            editDefectDescription.setText("");
            editDefectSeverity.setText("");
            formViewModel.defectDescription = "";
            formViewModel.defectSeverity = "";

            updateDefectListDisplay();
        });

        buttonSaveCheck.setOnClickListener(v -> {
            formViewModel.vehicleReg = editVehicleReg.getText().toString().trim();
            formViewModel.driverName = editDriverName.getText().toString().trim();
            formViewModel.date = editDate.getText().toString().trim();

            if (formViewModel.vehicleReg.isEmpty()) {
                Toast.makeText(this, "Please enter vehicle details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (formViewModel.driverName.isEmpty()) {
                Toast.makeText(this, "Please enter driver name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (formViewModel.date.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }

            String overallStatus = formViewModel.pendingDefects.isEmpty() ? "Pass" : "Fail";

            SafetyCheck check = new SafetyCheck(
                    formViewModel.date,
                    formViewModel.vehicleReg,
                    formViewModel.driverName,
                    overallStatus
            );

            List<Defect> defectsToSave = new ArrayList<>(formViewModel.pendingDefects);
            safetyViewModel.insertCheckWithDefects(check, defectsToSave);

            formViewModel.pendingDefects.clear();
            finish();
        });

        buttonCancel.setOnClickListener(v -> finish());
    }

    private void updateDefectListDisplay() {
        if (formViewModel.pendingDefects.isEmpty()) {
            textDefectList.setText("No defects added yet");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < formViewModel.pendingDefects.size(); i++) {
            Defect defect = formViewModel.pendingDefects.get(i);
            builder.append(i + 1)
                    .append(". ")
                    .append(defect.getDescription())
                    .append(" (")
                    .append(defect.getSeverity())
                    .append(")\n");
        }
        textDefectList.setText(builder.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        formViewModel.vehicleReg = editVehicleReg.getText().toString();
        formViewModel.driverName = editDriverName.getText().toString();
        formViewModel.date = editDate.getText().toString();
        formViewModel.defectDescription = editDefectDescription.getText().toString();
        formViewModel.defectSeverity = editDefectSeverity.getText().toString();
    }
}