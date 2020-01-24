package com.example.spotter_workoutlog.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        indices = {@Index("category_id"), @Index("exercise_id")},
        tableName = "exercises_in_category",
        primaryKeys = {"category_id", "exercise_id"},
        foreignKeys = {
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = "id",
                        childColumns = "category_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exercise_id",
                        onDelete = ForeignKey.CASCADE)})
public class ExerciseInCategory {

    private int category_id;
    private int exercise_id;

    public ExerciseInCategory(int category_id, int exercise_id) {
        this.category_id = category_id;
        this.exercise_id = exercise_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getExercise_id() {
        return exercise_id;
    }
}
