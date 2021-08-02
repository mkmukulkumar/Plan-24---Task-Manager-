package com.babra.plan24;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface task_data_dao {
    @Query("SELECT * FROM task_data")
    List<task_data> getAll();

    @Query("DELETE FROM task_data")
    void deleteAll();

    @Insert
    void insert(task_data task);

    @Delete
    void delete(task_data task);

    @Update
    void update(task_data task);
}