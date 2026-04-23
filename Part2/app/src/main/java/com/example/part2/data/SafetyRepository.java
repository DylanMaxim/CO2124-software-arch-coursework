package com.example.part2.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SafetyRepository {

    private final SafetyDao safetyDao;
    private final LiveData<List<SafetyCheckWithDefects>> allChecks;
    private final ExecutorService databaseExecutor;

    public SafetyRepository(Application application) {
        SafeCheckDatabase db = SafeCheckDatabase.getDatabase(application);
        safetyDao = db.safetyDao();
        allChecks = safetyDao.getAllChecksWithDefects();
        databaseExecutor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<SafetyCheckWithDefects>> getAllChecks() {
        return allChecks;
    }

    public LiveData<SafetyCheckWithDefects> getCheckById(int checkId) {
        return safetyDao.getCheckWithDefects(checkId);
    }

    public void insertCheckWithDefects(SafetyCheck check, List<Defect> defects) {
        databaseExecutor.execute(() -> {
            long checkId = safetyDao.insertSafetyCheck(check);
            for (Defect defect : defects) {
                Defect defectWithParent = new Defect(
                        (int) checkId,
                        defect.getDescription(),
                        defect.getSeverity()
                );
                safetyDao.insertDefect(defectWithParent);
            }
        });
    }

    public void deleteCheck(int checkId) {
        databaseExecutor.execute(() -> safetyDao.deleteCheckById(checkId));
    }
}