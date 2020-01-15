package com.example.spotter_workoutlog.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(
        indices = {@Index("session_exercise_id")},
        tableName = "sets",
        foreignKeys = @ForeignKey(
                entity = SessionExercise.class,
                parentColumns = "id",
                childColumns = "session_exercise_id",
                onDelete = ForeignKey.CASCADE))
public class Set implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int session_exercise_id;

    private int reps;

    private float weight;

    private int order;

    public Set(int session_exercise_id, int reps, float weight, int order) {
        this.session_exercise_id = session_exercise_id;
        this.reps = reps;
        this.weight = weight;
        this.order = order;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSession_exercise_id(int session_exercise_id) {
        this.session_exercise_id = session_exercise_id;
    }

    public int getSession_exercise_id() {
        return session_exercise_id;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getReps() {
        return reps;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
