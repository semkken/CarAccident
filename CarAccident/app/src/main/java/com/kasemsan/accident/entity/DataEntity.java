package com.kasemsan.accident.entity;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_history")
public class DataEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "category_name")
    public String categoryName;
    @ColumnInfo(name = "probability_score")
    public double probabilityScore;

    @ColumnInfo(name = "date_event")
    public long dateEvent;

    public String getCategoryName() {
        return categoryName;
    }

    public double getProbabilityScore() {
        return probabilityScore;
    }

    public long getDateEvent() {
        return dateEvent;
    }
}

