package com.example.spotter_workoutlog.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private boolean deleted;

    private boolean selected;

    public Exercise(String name, boolean deleted) {
        this.name = name;
        this.deleted = deleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isSelected() {
        return selected;
    }
}
