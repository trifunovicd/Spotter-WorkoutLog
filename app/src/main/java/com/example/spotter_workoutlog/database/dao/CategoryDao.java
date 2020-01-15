package com.example.spotter_workoutlog.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spotter_workoutlog.database.models.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);
/*
    @Query("DELETE FROM categories")
    void deleteAllCategories();*/

    @Query("SELECT * FROM categories WHERE deleted = 0 ORDER BY name")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT COUNT(1) FROM categories WHERE name = :name")
    int checkIfNameExists(String name);
}
