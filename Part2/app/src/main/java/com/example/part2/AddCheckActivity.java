package com.example.part2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.data.Defect;
import com.example.part2.data.SafetyCheck;
import com.example.part2.ui.AddCheckViewModel;
import com.example.part2.ui.SafetyViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddCheckActivity extends AppCompatActivity {

    private EditText editVehicleReg, editDriverName, editDate, editDefectDescription;
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
        Button buttonSaveCheck = findViewById(R.id.buttonSaveCheck);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        formViewModel = new ViewModelProvider(this).get(AddCheckViewModel.class);
        safetyViewModel = new ViewModelProvider(this).get(SafetyViewModel.class);

        editVehicleReg.setText(formViewModel.vehicleReg);
        editDriverName.setText(formViewModel.driverName);
        editDate.setText(formViewModel.date);
        editDefectDescription.setText(formViewModel.defectDescription);

        buttonSaveCheck.setOnClickListener(v -> {
            formViewModel.vehicleReg = editVehicleReg.getText().toString().trim();
            formViewModel.driverName = editDriverName.getText().toString().trim();
            formViewModel.date = editDate.getText().toString().trim();
            formViewModel.defectDescription = editDefectDescription.getText().toString().trim();

            if (formViewModel.vehicleReg.isEmpty()) {
                Toast.makeText(this, "Please enter vehicle details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (formViewModel.driverName.isEmpty()) {
                Toast.makeText(this, "Please enter driver name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (formViewModel.date.isEmpty()) {
                Toast.makeText(this, "Please enter date", Toast.LENGTH_SHORT).show();
                return;
            }

            String overallStatus = "Pass";
            List<Defect> defects = new ArrayList<>();

            if (!formViewModel.defectDescription.isEmpty()) {
                overallStatus = "Fail";
                defects.add(new Defect(0, formViewModel.defectDescription, "High"));
            }

            SafetyCheck check = new SafetyCheck(
                    formViewModel.date,
                    formViewModel.vehicleReg,
                    formViewModel.driverName,
                    overallStatus
            );

            safetyViewModel.insertCheckWithDefects(check, defects);
            finish();
        });

        buttonCancel.setOnClickListener(v -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();
        formViewModel.vehicleReg = editVehicleReg.getText().toString();
        formViewModel.driverName = editDriverName.getText().toString();
        formViewModel.date = editDate.getText().toString();
        formViewModel.defectDescription = editDefectDescription.getText().toString();
    }
}