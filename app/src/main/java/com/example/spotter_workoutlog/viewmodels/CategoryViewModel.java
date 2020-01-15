package com.example.spotter_workoutlog.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.models.Category;
import com.example.spotter_workoutlog.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private OnCategoryVMTaskFinish onCategoryVMTaskFinish;
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();

        categoryRepository.setOnFinishListener(new CategoryRepository.OnCategoryTaskFinish() {
            @Override
            public void checkIfNameExists(int check) {
                onCategoryVMTaskFinish.checkIfNameExists(check);
            }
        });
    }

    public void insertCategory(Category category){
        categoryRepository.insertCategory(category);
    }

    public void updateCategory(Category category){
        categoryRepository.updateCategory(category);
    }

    public void deleteCategory(Category category){
        categoryRepository.deleteCategory(category);
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void checkIfNameExists(String name){
        categoryRepository.checkIfNameExists(name);
    }

    public interface OnCategoryVMTaskFinish{
        void checkIfNameExists(int check);
    }

    public void setOnFinishListener(OnCategoryVMTaskFinish listener){
        this.onCategoryVMTaskFinish = listener;
    }
}
