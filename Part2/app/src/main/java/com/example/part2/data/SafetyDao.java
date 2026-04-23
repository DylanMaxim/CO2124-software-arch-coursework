package com.example.part2.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface SafetyDao {

    @Insert
    long insertSafetyCheck(SafetyCheck safetyCheck);

    @Insert
    void insertDefect(Defect defect);

    @Transaction
    @Query("SELECT * FROM safety_checks ORDER BY checkId DESC")
    LiveData<List<SafetyCheckWithDefects>> getAllChecksWithDefects();

    @Transaction
    @Query("SELECT * FROM safety_checks WHERE checkId = :checkId")
    LiveData<SafetyCheckWithDefects> getCheckWithDefects(int checkId);

    @Query("DELETE FROM safety_checks WHERE checkId = :checkId")
    void deleteCheckById(int checkId);
}