package com.example.spotter_workoutlog.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.spotter_workoutlog.database.AppDatabase;
import com.example.spotter_workoutlog.database.AppExecutor;
import com.example.spotter_workoutlog.database.dao.SessionExerciseDao;
import com.example.spotter_workoutlog.database.dao.SetDao;
import com.example.spotter_workoutlog.database.dao.WorkoutSessionDao;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.database.models.WorkoutSession;

import java.util.List;

public class WorkoutRepository {
    private static final String TAG = "MyActivity";
    private OnTaskFinish onTaskFinish;
    private OnTaskFinishSets onTaskFinishSets;
    private WorkoutSessionDao workoutSessionDao;
    private LiveData<List<WorkoutSession>> allWorkoutSessions;
    private SessionExerciseDao sessionExerciseDao;
    private LiveData<List<SessionExercise>> allSessionExercises;
    private LiveData<List<SessionExercise>> allSessionExercisesForExercise;
    private SetDao setDao;
    private LiveData<List<Set>> allSets;
    private List<Set> allSetsForSession;

    public WorkoutRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        workoutSessionDao = database.workoutSessionDao();
        sessionExerciseDao = database.sessionExerciseDao();
        setDao = database.setDao();
        allWorkoutSessions = workoutSessionDao.getAllWorkoutSessions();
    }


    public void getLastWorkoutSession(){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                WorkoutSession workoutSession = workoutSessionDao.getLastWorkoutSession();
                Log.d(TAG, "run: lastDate " + workoutSession);
                onTaskFinish.getLastWorkoutSession(workoutSession);
            }
        });
    }

    public void insertWorkoutSession(final WorkoutSession workoutSession){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                long lastWorkoutSessionId = workoutSessionDao.insertWorkoutSession(workoutSession);
                onTaskFinish.lastWorkoutId(lastWorkoutSessionId);
            }
        });
    }

    public LiveData<List<WorkoutSession>> getAllWorkoutSessions(){
        return allWorkoutSessions;
    }


    public void getMaxOrderOfSessionExercise(final int workoutSessionId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int maxOrderValue = sessionExerciseDao.getMaxOrderOfSessionExercise(workoutSessionId);
                Log.d(TAG, "run: on maxfinish" + maxOrderValue);
                onTaskFinish.getMaxOrderSessionExercise(maxOrderValue);
            }
        });
    }

    public void insertSessionExercise(final SessionExercise sessionExercise){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                long lastSessionExerciseId = sessionExerciseDao.insertSessionExercise(sessionExercise);
                onTaskFinish.lastSessionExerciseId(lastSessionExerciseId);
            }
        });
    }

    public void updateSessionExercise(final SessionExercise sessionExercise){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                sessionExerciseDao.updateSessionExercise(sessionExercise);
            }
        });
    }

    public void deleteSessionExercise(final SessionExercise sessionExercise){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                sessionExerciseDao.deleteSessionExercise(sessionExercise);
            }
        });
    }

    public LiveData<List<SessionExercise>> getAllSessionExercises(int workoutSessionId){
        allSessionExercises = sessionExerciseDao.getAllSessionExercises(workoutSessionId);
        return allSessionExercises;
    }

    public LiveData<List<SessionExercise>> getAllSessionExercisesForExercise(int exerciseId){
        allSessionExercisesForExercise = sessionExerciseDao.getAllSessionExercisesForExercise(exerciseId);
        return allSessionExercisesForExercise;
    }
/*
    public void getMaxOrderSets(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int maxOrderSets = setDao.getMaxOrderSets(sessionExerciseId);
                onTaskFinish.getMaxOrderSets(maxOrderSets);
            }
        });
    }
*/
    public void insertSet(final Set set){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                setDao.insertSet(set);
            }
        });
    }

    public void insertAllSets(final List<Set> sets){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                setDao.insertAllSets(sets);
            }
        });
    }

    public LiveData<List<Set>> getAllSets(int sessionExerciseId){
        allSets = setDao.getAllSets(sessionExerciseId);
        return allSets;
    }

    public void getAllSetsForSession(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                allSetsForSession = setDao.getAllSetsForSession(sessionExerciseId);
                onTaskFinishSets.getAllSetsForSession(allSetsForSession);
            }
        });
    }
/*
    public void updateSet(final Set set){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                setDao.updateSet(set);
            }
        });
    }*/

    public void deleteSet(final Set set){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                setDao.deleteSet(set);
            }
        });
    }

    public void deleteSets(final List<Set> sets){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                setDao.deleteSets(sets);
            }
        });
    }


    public interface OnTaskFinish{
        void getLastWorkoutSession(WorkoutSession workoutSession);
        void getMaxOrderSessionExercise(int maxOrder);
        void lastWorkoutId(Long workoutId);
        void lastSessionExerciseId(Long sessionExerciseId);
        //void getMaxOrderSets(int maxOrder);
    }

    public void setOnFinishListener(OnTaskFinish listener){
        this.onTaskFinish = listener;
    }

    public interface OnTaskFinishSets{
        void getAllSetsForSession(List<Set> sets);
    }

    public void setOnSetsFinishListener(OnTaskFinishSets listener){
        this.onTaskFinishSets = listener;
    }
}