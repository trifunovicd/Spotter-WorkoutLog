package com.example.spotter_workoutlog.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.AppDatabase;
import com.example.spotter_workoutlog.database.AppExecutor;
import com.example.spotter_workoutlog.database.dao.ExerciseDao;
import com.example.spotter_workoutlog.database.dao.ExerciseInCategoryDao;
import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.database.models.ExerciseInCategory;

import java.util.List;

public class ExerciseRepository {
    private static final String TAG = "MyActivity";
    private OnExerciseTaskFinish onExerciseTaskFinish;
    private OnExerciseInsertFinish onExerciseInsertFinish;
    private ExerciseDao exerciseDao;
    private LiveData<List<Exercise>> allExercises;
    private ExerciseInCategoryDao exerciseInCategoryDao;
    private LiveData<List<Exercise>> allExercisesInCategory;

    public ExerciseRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        exerciseDao = database.exerciseDao();
        exerciseInCategoryDao = database.exerciseInCategoryDao();
        allExercises = exerciseDao.getAllExercises();
    }

    public void insertExercise(final Exercise exercise, final boolean return_id){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                long lastExerciseId = exerciseDao.insertExercise(exercise);

                if(return_id){
                    onExerciseInsertFinish.lastExerciseId(lastExerciseId);
                }
            }
        });
    }

    public void insertExerciseInCategory(final ExerciseInCategory exerciseInCategory){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                exerciseInCategoryDao.insertExerciseInCategory(exerciseInCategory);
            }
        });
    }

    public void updateExercise(final Exercise exercise){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                exerciseDao.updateExercise(exercise);
            }
        });
    }

    public void deleteExercise(final Exercise exercise){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                exerciseDao.deleteExercise(exercise);
            }
        });
    }

    public void deleteExerciseInCategory(final ExerciseInCategory exerciseInCategory){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                exerciseInCategoryDao.deleteExerciseInCategory(exerciseInCategory);
            }
        });
    }

    public LiveData<List<Exercise>> getAllExercises(){
        return allExercises;
    }

    public LiveData<List<Exercise>> getAllExercisesInCategory(int category_id){
        allExercisesInCategory = exerciseInCategoryDao.getAllExercisesInCategory(category_id);
        return allExercisesInCategory;
    }

    public void checkIfNameExists(final String name){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int check = exerciseDao.checkIfNameExists(name);
                onExerciseTaskFinish.checkIfNameExists(check);
            }
        });
    }

    public interface OnExerciseTaskFinish{
        void checkIfNameExists(int check);
    }

    public void setOnFinishListener(OnExerciseTaskFinish listener){
        this.onExerciseTaskFinish = listener;
    }

    public interface OnExerciseInsertFinish{
        void lastExerciseId(Long exercise_id);
    }

    public void setOnInsertFinishListener(OnExerciseInsertFinish listener){
        this.onExerciseInsertFinish = listener;
    }
}
