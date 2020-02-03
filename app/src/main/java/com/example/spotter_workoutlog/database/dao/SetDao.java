package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spotter_workoutlog.database.models.Category;
import com.example.spotter_workoutlog.database.models.Set;

import java.util.List;

@Dao
public interface SetDao {

    @Insert
    void insertSet(Set set);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSets(List<Set> sets);

    @Update
    void updateSet(Set set);

    @Delete
    void deleteSet(Set set);

    @Delete
    void deleteSets(List<Set> sets);

    @Query("SELECT * FROM sets WHERE session_exercise_id = :sessionExerciseId ORDER BY `order`")
    LiveData<List<Set>> getAllSets(int sessionExerciseId);

    @Query("SELECT * FROM sets WHERE session_exercise_id = :sessionExerciseId ORDER BY `order`")
    List<Set> getAllSetsForSession(int sessionExerciseId);

    @Query("SELECT SUM(weight * reps) FROM sets WHERE session_exercise_id = :sessionExerciseId")
    Float getTotalVolume(int sessionExerciseId);

    @Query("SELECT SUM(reps) FROM sets WHERE session_exercise_id = :sessionExerciseId")
    Float getTotalReps(int sessionExerciseId);

    @Query("SELECT MAX(weight) FROM sets WHERE session_exercise_id = :sessionExerciseId")
    Float getMaxWeight(int sessionExerciseId);

    @Query("SELECT MAX(reps) FROM sets WHERE session_exercise_id = :sessionExerciseId")
    Float getMaxReps(int sessionExerciseId);

    @Query("SELECT * FROM sets WHERE session_exercise_id = :sessionExerciseId ORDER BY weight DESC, reps DESC LIMIT 1 ")
    Set getSetWithMaxValues(int sessionExerciseId);

/*
    @Query("SELECT MAX(`order`) FROM sets WHERE session_exercise_id = :sessionExerciseId")
    int getMaxOrderSets(int sessionExerciseId);*/
}
