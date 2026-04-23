package com.example.part2.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SafetyRepository {

    private static final String TAG = SafetyRepository.class.getSimpleName();

    private final SafetyDao safetyDao;
    private final LiveData<List<SafetyCheckWithDefects>> allChecks;
    private final ExecutorService databaseExecutor;

    public SafetyRepository(Application application) {
        SafeCheckDatabase db = SafeCheckDatabase.getDatabase(application);
        safetyDao = db.safetyDao();
        allChecks = safetyDao.getAllChecksWithDefects();
        databaseExecutor = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<SafetyCheckWithDefects>> getAllChecks() {
        return allChecks;
    }

    public LiveData<SafetyCheckWithDefects> getCheckById(int checkId) {
        return safetyDao.getCheckWithDefects(checkId);
    }

    public void insertCheckWithDefects(SafetyCheck check, List<Defect> defects) {
        databaseExecutor.execute(() -> {
            Log.d(TAG, "Inserting safety check for: " + check.getVehicleRegistration());
            long checkId = safetyDao.insertSafetyCheck(check);

            for (Defect defect : defects) {
                Defect defectWithParent = new Defect(
                        (int) checkId,
                        defect.getDescription(),
                        defect.getSeverity()
                );
                safetyDao.insertDefect(defectWithParent);
            }

            Log.d(TAG, "Inserted " + defects.size() + " defects for checkId: " + checkId);
        });
    }

    public void deleteCheck(int checkId) {
        Log.d(TAG, "Deleting check: " + checkId);
        databaseExecutor.execute(() -> safetyDao.deleteCheckById(checkId));
    }
}