package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spotter_workoutlog.database.models.Category;
import com.example.spotter_workoutlog.database.models.Set;

import java.util.List;

@Dao
public interface SetDao {

    @Insert
    void insertSet(Set set);

    @Insert
    void insertAllSets(List<Set> sets);

    @Update
    void updateSet(Set set);

    @Delete
    void deleteSet(Set set);

    @Query("SELECT * FROM sets WHERE session_exercise_id = :sessionExerciseId ORDER BY `order`")
    LiveData<List<Set>> getAllSets(int sessionExerciseId);

    @Query("SELECT * FROM sets WHERE session_exercise_id = :sessionExerciseId ORDER BY `order`")
    List<Set> getAllSetsForSession(int sessionExerciseId);

/*
    @Query("SELECT MAX(`order`) FROM sets WHERE session_exercise_id = :sessionExerciseId")
    int getMaxOrderSets(int sessionExerciseId);*/
}
