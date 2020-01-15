package com.example.spotter_workoutlog.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.AppDatabase;
import com.example.spotter_workoutlog.database.AppExecutor;
import com.example.spotter_workoutlog.database.dao.ExerciseDao;
import com.example.spotter_workoutlog.database.models.Exercise;

import java.util.List;

public class ExerciseRepository {
    private OnExerciseTaskFinish onExerciseTaskFinish;
    private ExerciseDao exerciseDao;
    private LiveData<List<Exercise>> allExercises;

    public ExerciseRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        exerciseDao = database.exerciseDao();

    }

    public void insertExercise(final Exercise exercise){

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                exerciseDao.insertExercise(exercise);
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

    public LiveData<List<Exercise>> getAllExercises(int category_id){
        allExercises = exerciseDao.getAllExercises(category_id);
        return allExercises;
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
}
