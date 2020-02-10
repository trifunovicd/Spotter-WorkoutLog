package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.spotter_workoutlog.database.models.WorkoutSession;

import java.util.Date;
import java.util.List;

@Dao
public interface WorkoutSessionDao {

    @Insert
    long insertWorkoutSession(WorkoutSession workoutSession);

    @Delete
    void deleteWorkoutSession(WorkoutSession workoutSession);

    @Query("DELETE FROM workout_sessions WHERE id = :workout_session_id")
    void deleteWorkoutSessionById(int workout_session_id);

    @Query("SELECT * FROM workout_sessions ORDER BY date")
    LiveData<List<WorkoutSession>> getAllWorkoutSessions();

    @Query("SELECT * FROM workout_sessions ORDER BY date DESC LIMIT 1")
    WorkoutSession getLastWorkoutSession();
}
