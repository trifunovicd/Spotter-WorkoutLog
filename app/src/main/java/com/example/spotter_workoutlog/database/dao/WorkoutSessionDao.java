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

    @Query("SELECT * FROM workout_sessions")
    LiveData<List<WorkoutSession>> getAllWorkoutSessions();

    @Query("SELECT * FROM workout_sessions ORDER BY ID DESC LIMIT 1")
    WorkoutSession getLastWorkoutSession();
}
