package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spotter_workoutlog.database.models.SessionExercise;

import java.util.List;

@Dao
public interface SessionExerciseDao {

    @Insert
    long insertSessionExercise(SessionExercise sessionExercise);

    @Delete
    void deleteSessionExercise(SessionExercise sessionExercise);

    @Update
    void updateSessionExercise(SessionExercise sessionExercise);

    @Query("SELECT * FROM session_exercises WHERE workout_session_id = :workoutSessionId ORDER BY `order`")
    LiveData<List<SessionExercise>> getAllSessionExercises(int workoutSessionId);

    @Query("SELECT * FROM session_exercises WHERE exercise_id = :exerciseId ORDER BY date DESC")
    LiveData<List<SessionExercise>> getAllSessionExercisesForExercise(int exerciseId);

    @Query("SELECT * FROM session_exercises WHERE exercise_id = :exerciseId")
    LiveData<List<SessionExercise>> getAllSessionExercisesForGraph(int exerciseId);

    @Query("SELECT MAX(`order`) FROM session_exercises WHERE workout_session_id = :workoutSessionId")
    int getMaxOrderOfSessionExercise(int workoutSessionId);
}
