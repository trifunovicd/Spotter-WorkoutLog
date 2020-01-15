package com.example.spotter_workoutlog.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.AppDatabase;
import com.example.spotter_workoutlog.database.AppExecutor;
import com.example.spotter_workoutlog.database.dao.CategoryDao;
import com.example.spotter_workoutlog.database.models.Category;

import java.util.List;

public class CategoryRepository {
    private OnCategoryTaskFinish onCategoryTaskFinish;
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;

    public CategoryRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

    public void insertCategory(final Category category){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.insertCategory(category);
            }
        });
    }

    public void updateCategory(final Category category){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.updateCategory(category);
            }
        });
    }

    public void deleteCategory(final Category category){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.deleteCategory(category);
            }
        });
    }

    public LiveData<List<Category>> getAllCategories(){
        return allCategories;
    }

    public void checkIfNameExists(final String name){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int check = categoryDao.checkIfNameExists(name);
                onCategoryTaskFinish.checkIfNameExists(check);
            }
        });
    }

    public interface OnCategoryTaskFinish{
        void checkIfNameExists(int check);
    }

    public void setOnFinishListener(OnCategoryTaskFinish listener){
        this.onCategoryTaskFinish = listener;
    }
}
