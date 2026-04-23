package com.example.part2.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "safety_checks")
public class SafetyCheck {

    @PrimaryKey(autoGenerate = true)
    private int checkId;

    @NonNull
    private String date;

    @NonNull
    private String vehicleRegistration;

    @NonNull
    private String driverName;

    @NonNull
    private String overallStatus;

    public SafetyCheck(@NonNull String date,
                       @NonNull String vehicleRegistration,
                       @NonNull String driverName,
                       @NonNull String overallStatus) {
        this.date = date;
        this.vehicleRegistration = vehicleRegistration;
        this.driverName = driverName;
        this.overallStatus = overallStatus;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    @NonNull
    public String getDriverName() {
        return driverName;
    }

    @NonNull
    public String getOverallStatus() {
        return overallStatus;
    }
}