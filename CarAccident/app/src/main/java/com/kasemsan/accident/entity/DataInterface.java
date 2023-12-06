package com.kasemsan.accident.entity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataInterface {
    @Insert
    void insert(DataEntity entity);

    @Query("SELECT DISTINCT * FROM tbl_history ORDER BY date_event DESC LIMIT 100")
    List<DataEntity> getAllData();

    @Query("DELETE FROM tbl_history WHERE id NOT IN (SELECT id FROM tbl_history ORDER BY date_event DESC LIMIT 100)")
    void keepLatest100Rows();
}
