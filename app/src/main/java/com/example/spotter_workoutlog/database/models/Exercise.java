package com.example.spotter_workoutlog.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(
        indices = {@Index("category_id")},
        tableName = "exercises",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE))
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int category_id;

    private String name;

    private boolean deleted;

    public Exercise(int category_id, String name, boolean deleted) {
        this.category_id = category_id;
        this.name = name;
        this.deleted = deleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
