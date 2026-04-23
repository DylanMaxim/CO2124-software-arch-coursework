package com.example.part2.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.part2.data.Defect;
import com.example.part2.data.SafetyCheck;
import com.example.part2.data.SafetyCheckWithDefects;
import com.example.part2.data.SafetyRepository;

import java.util.List;

public class SafetyViewModel extends AndroidViewModel {

    private final SafetyRepository repository;
    private final LiveData<List<SafetyCheckWithDefects>> allChecks;

    public SafetyViewModel(@NonNull Application application) {
        super(application);
        repository = new SafetyRepository(application);
        allChecks = repository.getAllChecks();
    }

    public LiveData<List<SafetyCheckWithDefects>> getAllChecks() {
        return allChecks;
    }

    public LiveData<SafetyCheckWithDefects> getCheckById(int checkId) {
        return repository.getCheckById(checkId);
    }

    public void insertCheckWithDefects(SafetyCheck check, List<Defect> defects) {
        repository.insertCheckWithDefects(check, defects);
    }

    public void deleteCheck(int checkId) {
        repository.deleteCheck(checkId);
    }
}