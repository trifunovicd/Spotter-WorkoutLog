package com.example.spotter_workoutlog.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.spotter_workoutlog.database.dao.CategoryDao;
import com.example.spotter_workoutlog.database.dao.ExerciseDao;
import com.example.spotter_workoutlog.database.dao.ExerciseInCategoryDao;
import com.example.spotter_workoutlog.database.dao.SessionExerciseDao;
import com.example.spotter_workoutlog.database.dao.SetDao;
import com.example.spotter_workoutlog.database.dao.WorkoutSessionDao;
import com.example.spotter_workoutlog.database.models.Category;
import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.database.models.ExerciseInCategory;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.database.models.WorkoutSession;

@Database(entities = {Category.class, Exercise.class, ExerciseInCategory.class, WorkoutSession.class, SessionExercise.class, Set.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract CategoryDao categoryDao();
    public abstract ExerciseDao exerciseDao();
    public abstract ExerciseInCategoryDao exerciseInCategoryDao();
    public abstract WorkoutSessionDao workoutSessionDao();
    public abstract SessionExerciseDao sessionExerciseDao();
    public abstract SetDao setDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()/*
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                                @Override
                                public void run() {

                                    getInstance(context).categoryDao().insertCategory(new Category("biceps"));
                                    getInstance(context).categoryDao().insertCategory(new Category("triceps"));
                                    getInstance(context).categoryDao().insertCategory(new Category("ramena"));
                                }
                            });
                        }
                    })*/
                    .build();
        }
        return instance;
    }

}
