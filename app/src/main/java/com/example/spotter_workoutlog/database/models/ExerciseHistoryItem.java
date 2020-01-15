package com.example.spotter_workoutlog.database.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ExerciseHistoryItem implements Serializable {
    private int id;

    private int workout_session_id;

    private int exercise_id;

    private int order;

    private Date date;

    private String note;

    private List<Set> sets;

    public ExerciseHistoryItem(int id, int workout_session_id, int exercise_id, int order, Date date, String note, List<Set> sets) {
        this.id = id;
        this.workout_session_id = workout_session_id;
        this.exercise_id = exercise_id;
        this.order = order;
        this.date = date;
        this.note = note;
        this.sets = sets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }
}
