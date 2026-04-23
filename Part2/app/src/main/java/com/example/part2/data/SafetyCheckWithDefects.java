package com.example.part2.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SafetyCheckWithDefects {

    @Embedded
    public SafetyCheck safetyCheck;

    @Relation(
            parentColumn = "checkId",
            entityColumn = "parentCheckId"
    )
    public List<Defect> defects;
}