package com.example.part2.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { SafetyCheck.class, Defect.class }, version = 2, exportSchema = false)
public abstract class SafeCheckDatabase extends RoomDatabase {

    private static final String TAG = SafeCheckDatabase.class.getSimpleName();

    public abstract SafetyDao safetyDao();

    private static SafeCheckDatabase INSTANCE;

    public static synchronized SafeCheckDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            Log.d(TAG, "Creating new database instance");
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SafeCheckDatabase.class,
                    "safecheck_db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}