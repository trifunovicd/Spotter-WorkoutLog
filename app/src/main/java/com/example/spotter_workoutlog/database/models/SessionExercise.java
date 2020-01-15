package com.example.spotter_workoutlog.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(
        indices = {@Index("workout_session_id"), @Index("exercise_id")},
        tableName = "session_exercises",
        foreignKeys = {
                @ForeignKey(
                    entity = WorkoutSession.class,
                    parentColumns = "id",
                    childColumns = "workout_session_id",
                    onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                    entity = Exercise.class,
                    parentColumns = "id",
                    childColumns = "exercise_id"/*,
                    onDelete = ForeignKey.CASCADE*/)})
public class SessionExercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int workout_session_id;

    private int exercise_id;

    private int order;

    private Date date;

    private String note;

    public SessionExercise(int workout_session_id, int exercise_id, int order, Date date, String note) {
        this.workout_session_id = workout_session_id;
        this.exercise_id = exercise_id;
        this.order = order;
        this.date = date;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getWorkout_session_id() {
        return workout_session_id;
    }

    public void setWorkout_session_id(int workout_session_id) {
        this.workout_session_id = workout_session_id;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
