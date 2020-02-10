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
import com.example.spotter_workoutlog.database.models.WorkoutStats;

import java.util.Date;
import java.util.List;

public class WorkoutRepository {
    private static final String TAG = "MyActivity";
    private OnTaskFinish onTaskFinish;
    private OnTaskFinishSets onTaskFinishSets;
    private OnTaskFinishSetsVolume onTaskFinishSetsVolume;
    //private OnTaskFinishCalendar onTaskFinishCalendar;
    private WorkoutSessionDao workoutSessionDao;
    private LiveData<List<WorkoutSession>> allWorkoutSessions;
    private SessionExerciseDao sessionExerciseDao;
    private LiveData<List<SessionExercise>> allSessionExercises;
    private LiveData<List<SessionExercise>> allSessionExercisesForExercise;
    private LiveData<List<SessionExercise>> allSessionExercisesForGraph;
    private SetDao setDao;
    private LiveData<List<Set>> allSets;
    private List<Set> allSetsForSession;
    private WorkoutStats workoutStats;

    public WorkoutRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        workoutSessionDao = database.workoutSessionDao();
        sessionExerciseDao = database.sessionExerciseDao();
        setDao = database.setDao();
        allWorkoutSessions = workoutSessionDao.getAllWorkoutSessions();
    }


    ///// WorkoutSession
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

    public void deleteWorkoutSession(final WorkoutSession workoutSession){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                workoutSessionDao.deleteWorkoutSession(workoutSession);
            }
        });
    }

    public void deleteWorkoutSessionById(final int workout_session_id){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                workoutSessionDao.deleteWorkoutSessionById(workout_session_id);
            }
        });
    }

    ///// SessionExercise
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
        //onTaskFinishCalendar.getAllSessionExercises(allSessionExercises);
        return allSessionExercises;
    }

    public LiveData<List<SessionExercise>> getAllSessionExercisesForExercise(int exerciseId){
        allSessionExercisesForExercise = sessionExerciseDao.getAllSessionExercisesForExercise(exerciseId);
        return allSessionExercisesForExercise;
    }

    public LiveData<List<SessionExercise>> getAllSessionExercisesForGraph(int exerciseId){
        allSessionExercisesForGraph = sessionExerciseDao.getAllSessionExercisesForGraph(exerciseId);
        return allSessionExercisesForGraph;
    }

    public void getSessionsCount(final int workout_session_id){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int count = sessionExerciseDao.getSessionsCount(workout_session_id);
                onTaskFinishSets.getSessionsCount(count);
            }
        });
    }

    ///// Set
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

    public void getTotalVolume(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Float totalVolume = setDao.getTotalVolume(sessionExerciseId);
                onTaskFinishSetsVolume.getTotalVolume(totalVolume);
            }
        });
    }

    public void getTotalReps(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Float totalReps = setDao.getTotalReps(sessionExerciseId);
                onTaskFinishSetsVolume.getTotalReps(totalReps);
            }
        });
    }

    public void getMaxWeight(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Float maxWeight = setDao.getMaxWeight(sessionExerciseId);
                onTaskFinishSetsVolume.getMaxWeight(maxWeight);
            }
        });
    }

    public void getMaxReps(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Float maxReps = setDao.getMaxReps(sessionExerciseId);
                onTaskFinishSetsVolume.getMaxReps(maxReps);
            }
        });
    }

    public void getSetWithMaxValues(final int sessionExerciseId){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Set set = setDao.getSetWithMaxValues(sessionExerciseId);
                onTaskFinishSetsVolume.getSetWithMaxValues(set);
            }
        });
    }

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

    /*public LiveData<List<Float>> getWorkoutStats(int workout_id){
        workoutStats = setDao.getWorkoutStats(workout_id);
        return workoutStats;
    }*/

    public void getWorkoutStats(final int workout_id){
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                workoutStats = setDao.getWorkoutStats(workout_id);
                onTaskFinishSets.getWorkoutStats(workoutStats);
            }
        });
    }

    ///// for NewExerciseFragment
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


    ///// for HistoryExerciseFragment
    public interface OnTaskFinishSets{
        void getAllSetsForSession(List<Set> sets);
        void getSessionsCount(int count);
        void getWorkoutStats(WorkoutStats workoutStats);
    }

    public void setOnSetsFinishListener(OnTaskFinishSets listener){
        this.onTaskFinishSets = listener;
    }


    ///// for GraphExerciseFragment
    public interface OnTaskFinishSetsVolume{
        void getTotalVolume(Float totalVolume);
        void getTotalReps(Float totalReps);
        void getMaxWeight(Float maxWeight);
        void getMaxReps(Float maxReps);
        void getSetWithMaxValues(Set set);
    }

    public void setOnSetsVolumeFinishListener(OnTaskFinishSetsVolume listener){
        this.onTaskFinishSetsVolume = listener;
    }
}