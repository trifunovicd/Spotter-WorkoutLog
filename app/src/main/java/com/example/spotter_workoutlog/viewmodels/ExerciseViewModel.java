package com.example.spotter_workoutlog.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.repositories.ExerciseRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private OnExerciseVMTaskFinish onExerciseVMTaskFinish;
    private ExerciseRepository exerciseRepository;
    private LiveData<List<Exercise>> allExercises;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        exerciseRepository = new ExerciseRepository(application);

        exerciseRepository.setOnFinishListener(new ExerciseRepository.OnExerciseTaskFinish() {
            @Override
            public void checkIfNameExists(int check) {
                onExerciseVMTaskFinish.checkIfNameExists(check);
            }
        });
    }

    public void insertExercise(final Exercise exercise){
        exerciseRepository.insertExercise(exercise);
    }

    public void updateExercise(final Exercise exercise){
        exerciseRepository.updateExercise(exercise);
    }

    public void deleteExercise(final Exercise exercise){
        exerciseRepository.deleteExercise(exercise);
    }

    public LiveData<List<Exercise>> getAllExercises(int category_id){
        allExercises = exerciseRepository.getAllExercises(category_id);
        return allExercises;
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
}
