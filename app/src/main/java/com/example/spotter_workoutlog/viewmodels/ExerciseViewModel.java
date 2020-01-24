package com.example.spotter_workoutlog.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.database.models.ExerciseInCategory;
import com.example.spotter_workoutlog.repositories.ExerciseRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private OnExerciseVMTaskFinish onExerciseVMTaskFinish;
    private OnExerciseInsertVMFinish onExerciseInsertVMFinish;
    private ExerciseRepository exerciseRepository;
    private LiveData<List<Exercise>> allExercises;
    private LiveData<List<Exercise>> allExercisesInCategory;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        exerciseRepository = new ExerciseRepository(application);
        allExercises = exerciseRepository.getAllExercises();

        exerciseRepository.setOnFinishListener(new ExerciseRepository.OnExerciseTaskFinish() {
            @Override
            public void checkIfNameExists(int check) {
                onExerciseVMTaskFinish.checkIfNameExists(check);
            }
        });

        exerciseRepository.setOnInsertFinishListener(new ExerciseRepository.OnExerciseInsertFinish() {
            @Override
            public void lastExerciseId(Long exercise_id) {
                onExerciseInsertVMFinish.lastExerciseId(exercise_id);
            }
        });
    }

    public void insertExercise(final Exercise exercise, final boolean return_id){
        exerciseRepository.insertExercise(exercise, return_id);
    }

    public void insertExerciseInCategory(final ExerciseInCategory exerciseInCategory){
        exerciseRepository.insertExerciseInCategory(exerciseInCategory);
    }

    public void updateExercise(final Exercise exercise){
        exerciseRepository.updateExercise(exercise);
    }

    public void deleteExercise(final Exercise exercise){
        exerciseRepository.deleteExercise(exercise);
    }

    public void deleteExerciseInCategory(final ExerciseInCategory exerciseInCategory){
        exerciseRepository.deleteExerciseInCategory(exerciseInCategory);
    }

    public LiveData<List<Exercise>> getAllExercises(){
        return allExercises;
    }

    public LiveData<List<Exercise>> getAllExercisesInCategory(int category_id){
        allExercisesInCategory = exerciseRepository.getAllExercisesInCategory(category_id);
        return allExercisesInCategory;
    }

    public void checkIfNameExists(String name){
        exerciseRepository.checkIfNameExists(name);
    }

    public interface OnExerciseVMTaskFinish{
        void checkIfNameExists(int check);
    }

    public void setOnFinishListener(OnExerciseVMTaskFinish listener){
        this.onExerciseVMTaskFinish = listener;
    }

    public interface OnExerciseInsertVMFinish{
        void lastExerciseId(Long exercise_id);
    }

    public void setOnInsertVMFinishListener(OnExerciseInsertVMFinish listener){
        this.onExerciseInsertVMFinish = listener;
    }
}
