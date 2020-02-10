package com.example.spotter_workoutlog.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.database.models.WorkoutSession;
import com.example.spotter_workoutlog.database.models.WorkoutStats;
import com.example.spotter_workoutlog.repositories.WorkoutRepository;

import java.util.Date;
import java.util.List;

public class WorkoutViewModel extends AndroidViewModel{
    private static final String TAG = "MyActivity";
    private OnTaskFinishVM onTaskFinishVM;
    private OnTaskFinishSetsVM onTaskFinishSetsVM;
    private OnTaskFinishSetsVolumeVM onTaskFinishSetsVolumeVM;
    //private OnTaskFinishCalendarVM onTaskFinishCalendarVM;
    private WorkoutRepository workoutRepository;
    private LiveData<List<WorkoutSession>> allWorkoutSessions;
    private LiveData<List<SessionExercise>> allSessionExercises;
    private LiveData<List<SessionExercise>> allSessionExercisesForExercise;
    private LiveData<List<SessionExercise>> allSessionExercisesForGraph;
    private LiveData<List<Set>> allSets;
    //private List<Float> workoutStats;
    //private List<Set> allSetsForSession;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        workoutRepository = new WorkoutRepository(application);
        allWorkoutSessions = workoutRepository.getAllWorkoutSessions();

        ///// for NewExerciseFragment
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
        });

        ///// for HistoryExerciseFragment
        workoutRepository.setOnSetsFinishListener(new WorkoutRepository.OnTaskFinishSets() {
            @Override
            public void getAllSetsForSession(List<Set> sets) {
                onTaskFinishSetsVM.getAllSetsForSession(sets);
            }

            @Override
            public void getSessionsCount(int count) {
                onTaskFinishSetsVM.getSessionsCount(count);
            }

            @Override
            public void getWorkoutStats(WorkoutStats workoutStats) {
                onTaskFinishSetsVM.getWorkoutStats(workoutStats);
            }
        });

        ///// for GraphExerciseFragment
        workoutRepository.setOnSetsVolumeFinishListener(new WorkoutRepository.OnTaskFinishSetsVolume() {
            @Override
            public void getTotalVolume(Float totalVolume) {
                onTaskFinishSetsVolumeVM.getTotalVolume(totalVolume);
            }

            @Override
            public void getTotalReps(Float totalReps) {
                onTaskFinishSetsVolumeVM.getTotalReps(totalReps);
            }

            @Override
            public void getMaxWeight(Float maxWeight) {
                onTaskFinishSetsVolumeVM.getMaxWeight(maxWeight);
            }

            @Override
            public void getMaxReps(Float maxReps) {
                onTaskFinishSetsVolumeVM.getMaxReps(maxReps);
            }

            @Override
            public void getSetWithMaxValues(Set set) {
                onTaskFinishSetsVolumeVM.getSetWithMaxValues(set);
            }
        });
/*
        workoutRepository.setOnTaskFinishCalendarListener(new WorkoutRepository.OnTaskFinishCalendar() {
            @Override
            public void getAllSessionExercises(List<SessionExercise> sessionExercises) {
                onTaskFinishCalendarVM.getAllSessionExercises(sessionExercises);
            }
        });*/
    }

    ///// WorkoutSession
    public void getLastWorkoutSession(){
        workoutRepository.getLastWorkoutSession();
    }

    public void insertWorkoutSession(WorkoutSession workoutSession){
        workoutRepository.insertWorkoutSession(workoutSession);
    }

    public LiveData<List<WorkoutSession>> getAllWorkoutSessions(){
        return allWorkoutSessions;
    }

    public void deleteWorkoutSession(WorkoutSession workoutSession){
        workoutRepository.deleteWorkoutSession(workoutSession);
    }

    public void deleteWorkoutSessionById(int workout_session_id){
        workoutRepository.deleteWorkoutSessionById(workout_session_id);
    }

    ///// SessionExercise
    public void getMaxOrderOfSessionExercise(final int workoutSessionId){
        workoutRepository.getMaxOrderOfSessionExercise(workoutSessionId);
    }

    public void insertSessionExercise(SessionExercise sessionExercise){
        workoutRepository.insertSessionExercise(sessionExercise);
    }

    public void updateSessionExercise(SessionExercise sessionExercise){
        workoutRepository.updateSessionExercise(sessionExercise);
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

    public LiveData<List<SessionExercise>> getAllSessionExercisesForGraph(int exerciseId){
        allSessionExercisesForGraph = workoutRepository.getAllSessionExercisesForGraph(exerciseId);
        return allSessionExercisesForGraph;
    }

    public void getSessionsCount(int workout_session_id){
        workoutRepository.getSessionsCount(workout_session_id);
    }

    ///// Set
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

    public void getTotalVolume(int sessionExerciseId){
        workoutRepository.getTotalVolume(sessionExerciseId);
    }

    public void getTotalReps(int sessionExerciseId){
        workoutRepository.getTotalReps(sessionExerciseId);
    }

    public void getMaxWeight(int sessionExerciseId){
        workoutRepository.getMaxWeight(sessionExerciseId);
    }

    public void getMaxReps(int sessionExerciseId){
        workoutRepository.getMaxReps(sessionExerciseId);
    }

    public void getSetWithMaxValues(int sessionExerciseId){
        workoutRepository.getSetWithMaxValues(sessionExerciseId);
    }

    public void deleteSet(Set set){
        workoutRepository.deleteSet(set);
    }

    public void deleteSets(final List<Set> sets){
        workoutRepository.deleteSets(sets);
    }

    public void getWorkoutStats(int workout_id){
        workoutRepository.getWorkoutStats(workout_id);
    }


    ///// for NewExerciseFragment
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


    ///// for HistoryExerciseFragment
    public interface OnTaskFinishSetsVM{
        void getAllSetsForSession(List<Set> sets);
        void getSessionsCount(int count);
        void getWorkoutStats(WorkoutStats workoutStats);
    }

    public void setOnFinishSetsVMListener(OnTaskFinishSetsVM listener){
        this.onTaskFinishSetsVM = listener;
    }


    ///// for GraphExerciseFragment
    public interface OnTaskFinishSetsVolumeVM{
        void getTotalVolume(Float totalVolume);
        void getTotalReps(Float totalReps);
        void getMaxWeight(Float maxWeight);
        void getMaxReps(Float maxReps);
        void getSetWithMaxValues(Set set);
    }

    public void setOnSetsVolumeVMFinishListener(OnTaskFinishSetsVolumeVM listener){
        this.onTaskFinishSetsVolumeVM = listener;
    }
}