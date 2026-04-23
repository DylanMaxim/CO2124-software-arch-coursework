package com.example.part2.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SafetyCheck.class, Defect.class}, version = 1, exportSchema = false)
public abstract class SafeCheckDatabase extends RoomDatabase {

    public abstract SafetyDao safetyDao();

    private static volatile SafeCheckDatabase INSTANCE;

    public static SafeCheckDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (SafeCheckDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            SafeCheckDatabase.class,
                            "safecheck_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}