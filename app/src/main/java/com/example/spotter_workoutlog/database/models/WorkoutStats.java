package com.example.spotter_workoutlog.database.models;

public class WorkoutStats {
    private int sets;

    private int reps;

    private float weight;

    public WorkoutStats(int sets, int reps, float weight) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
