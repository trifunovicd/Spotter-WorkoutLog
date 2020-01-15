package com.example.spotter_workoutlog.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.database.models.WorkoutSession;
import com.example.spotter_workoutlog.repositories.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel{
    private static final String TAG = "MyActivity";
    private OnTaskFinishVM onTaskFinishVM;
    private OnTaskFinishSetsVM onTaskFinishSetsVM;
    private WorkoutRepository workoutRepository;
    private LiveData<List<WorkoutSession>> allWorkoutSessions;
    private LiveData<List<SessionExercise>> allSessionExercises;
    private LiveData<List<SessionExercise>> allSessionExercisesForExercise;
    private LiveData<List<Set>> allSets;
    private List<Set> allSetsForSession;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        workoutRepository = new WorkoutRepository(application);
        allWorkoutSessions = workoutRepository.getAllWorkoutSessions();

        workoutRepository.setOnFinishListener(new WorkoutRepository.OnTaskFinish() {

            @Override
            public void getLastWorkoutSession(WorkoutSession workoutSession) {
                onTaskFinishVM.getLastWorkoutSession(workoutSession);
            }

            @Override
            public void getMaxOrderSessionExercise(int maxOrder) {
                onTaskFinishVM.getMaxOrderSessionExercise(maxOrder);
            }

            @Override
            public void lastWorkoutId(Long workoutId) {
                onTaskFinishVM.lastWorkoutId(workoutId);
            }

            @Override
            public void lastSessionExerciseId(Long sessionExerciseId) {
                onTaskFinishVM.lastSessionExerciseId(sessionExerciseId);
            }
/*
            @Override
            public void getMaxOrderSets(int maxOrder) {
                onTaskFinishVM.getMaxOrderSets(maxOrder);
            }
*/
        });

        workoutRepository.setOnSetsFinishListener(new WorkoutRepository.OnTaskFinishSets() {
            @Override
            public void getAllSetsForSession(List<Set> sets) {
                onTaskFinishSetsVM.getAllSetsForSession(sets);
            }
        });
    }

    public void getLastWorkoutSession(){
        workoutRepository.getLastWorkoutSession();
    }

    public void insertWorkoutSession(WorkoutSession workoutSession){
        workoutRepository.insertWorkoutSession(workoutSession);
    }

    public LiveData<List<WorkoutSession>> getAllWorkoutSessions(){
        return allWorkoutSessions;
    }

    public void getMaxOrderOfSessionExercise(final int workoutSessionId){
        workoutRepository.getMaxOrderOfSessionExercise(workoutSessionId);
    }

    public void insertSessionExercise(SessionExercise sessionExercise){
        workoutRepository.insertSessionExercise(sessionExercise);
    }

    public void deleteSessionExercise(final SessionExercise sessionExercise){
        workoutRepository.deleteSessionExercise(sessionExercise);
    }

    public LiveData<List<SessionExercise>> getAllSessionExercises(int workoutSessionId){
        allSessionExercises = workoutRepository.getAllSessionExercises(workoutSessionId);
        return allSessionExercises;
    }

    public LiveData<List<SessionExercise>> getAllSessionExercisesForExercise(int exerciseId) {
        allSessionExercisesForExercise = workoutRepository.getAllSessionExercisesForExercise(exerciseId);
        return allSessionExercisesForExercise;
    }
/*
    public void getMaxOrderSets(final int sessionExerciseId){
        workoutRepository.getMaxOrderSets(sessionExerciseId);
    }
*/
    public void insertSet(Set set){
        workoutRepository.insertSet(set);
    }

    public void insertAllSets(final List<Set> sets){
        workoutRepository.insertAllSets(sets);
    }

    public LiveData<List<Set>> getAllSets(int sessionExerciseId){
        allSets = workoutRepository.getAllSets(sessionExerciseId);
        return allSets;
    }

    public void getAllSetsForSession(int sessionExerciseId){
        workoutRepository.getAllSetsForSession(sessionExerciseId);
    }

    public interface OnTaskFinishVM{
        void getLastWorkoutSession(WorkoutSession workoutSession);
        void getMaxOrderSessionExercise(int maxOrder);
        void lastWorkoutId(Long workoutId);
        void lastSessionExerciseId(Long sessionExerciseId);
        //void getMaxOrderSets(int maxOrder);

    }

    public void setOnFinishVMListener(OnTaskFinishVM listener){
        this.onTaskFinishVM = listener;
    }


    public interface OnTaskFinishSetsVM{
        void getAllSetsForSession(List<Set> sets);
    }

    public void setOnFinishSetsVMListener(OnTaskFinishSetsVM listener){
        this.onTaskFinishSetsVM = listener;
    }

}