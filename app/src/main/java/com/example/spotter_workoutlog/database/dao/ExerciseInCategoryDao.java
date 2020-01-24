package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.database.models.ExerciseInCategory;

import java.util.List;

@Dao
public interface ExerciseInCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExerciseInCategory(ExerciseInCategory exerciseInCategory);

    @Update
    void updateExercise(ExerciseInCategory exerciseInCategory);

    @Delete//from category but it will stay  in all exercises list
    void deleteExerciseInCategory(ExerciseInCategory exerciseInCategory);

    @Query("SELECT exercises.* FROM exercises_in_category, exercises WHERE exercises_in_category.category_id = :categoryId AND exercises_in_category.exercise_id = exercises.id AND exercises.deleted = 0 ORDER BY name")
    LiveData<List<Exercise>> getAllExercisesInCategory(int categoryId);
}
