package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spotter_workoutlog.database.models.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insertExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);
/*
    @Query("DELETE FROM exercises")
    void deleteAllExercises();*/

    @Query("SELECT * FROM exercises WHERE category_id = :categoryId and deleted = 0 ORDER BY name")
    LiveData<List<Exercise>> getAllExercises(int categoryId);

    @Query("SELECT COUNT(1) FROM exercises WHERE name = :name")
    int checkIfNameExists(String name);
}