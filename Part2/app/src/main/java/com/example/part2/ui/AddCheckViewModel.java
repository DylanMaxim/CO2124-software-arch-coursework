package com.example.part2.ui;

import androidx.lifecycle.ViewModel;

import com.example.part2.data.Defect;

import java.util.ArrayList;
import java.util.List;

public class AddCheckViewModel extends ViewModel {
    public String vehicleReg = "";
    public String driverName = "";
    public String date = "";
    public String defectDescription = "";
    public String defectSeverity = "";
    public List<Defect> pendingDefects = new ArrayList<>();
}