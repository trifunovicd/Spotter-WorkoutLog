package com.example.spotter_workoutlog.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "workout_sessions")
public class WorkoutSession {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date date;

    public WorkoutSession(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
