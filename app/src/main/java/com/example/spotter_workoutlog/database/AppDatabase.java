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

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static synchronized AppDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                                @Override
                                public void run() {

                                    getInstance(context).categoryDao().insertCategory(new Category("Prsa", false));
                                    getInstance(context).exerciseDao().insertExercise(new Exercise("Bench press", false));
                                    getInstance(context).exerciseInCategoryDao().insertExerciseInCategory(new ExerciseInCategory(1, 1));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1546297200000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(1,1,1, new Date(1546297200000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(1,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(1,12,12,2));
                                    getInstance(context).setDao().insertSet(new Set(1,12,12,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1546470000000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(2,1,1, new Date(1546470000000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(2,15,12,1));
                                    getInstance(context).setDao().insertSet(new Set(2,15,15,2));
                                    getInstance(context).setDao().insertSet(new Set(2,15,15,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1546729200000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(3,1,1, new Date(1546729200000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(3,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(3,22,12,2));
                                    getInstance(context).setDao().insertSet(new Set(3,22,12,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1547247600000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(4,1,1, new Date(1547247600000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(4,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(4,32,16,2));
                                    getInstance(context).setDao().insertSet(new Set(4,32,14,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1548975600000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(5,1,1, new Date(1548975600000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(5,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(5,3,16,2));
                                    getInstance(context).setDao().insertSet(new Set(5,3,14,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1550185200000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(6,1,1, new Date(1550185200000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(6,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(6,30,16,2));
                                    getInstance(context).setDao().insertSet(new Set(6,30,14,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1551308400000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(7,1,1, new Date(1551308400000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(7,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(7,3,34,2));
                                    getInstance(context).setDao().insertSet(new Set(7,3,34,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1552604400000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(8,1,1, new Date(1552604400000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(8,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(8,16,34,2));
                                    getInstance(context).setDao().insertSet(new Set(8,19,34,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1553900400000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(9,1,1, new Date(1553900400000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(9,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(9,13,34,2));
                                    getInstance(context).setDao().insertSet(new Set(9,13,34,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1555538400000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(10,1,1, new Date(1555538400000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(10,12,11,1));
                                    getInstance(context).setDao().insertSet(new Set(10,11,34,2));
                                    getInstance(context).setDao().insertSet(new Set(10,13,11,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1558130400000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(11,1,1, new Date(1558130400000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(11,11,12,1));
                                    getInstance(context).setDao().insertSet(new Set(11,11,34,2));
                                    getInstance(context).setDao().insertSet(new Set(11,11,34,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1560376800000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(12,1,1, new Date(1560376800000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(12,12,13,1));
                                    getInstance(context).setDao().insertSet(new Set(12,11,13,2));
                                    getInstance(context).setDao().insertSet(new Set(12,13,13,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1565647200000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(13,1,1, new Date(1565647200000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(13,12,12,1));
                                    getInstance(context).setDao().insertSet(new Set(13,12,12,2));
                                    getInstance(context).setDao().insertSet(new Set(13,12,12,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1570917600000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(14,1,1, new Date(1570917600000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(14,14,12,1));
                                    getInstance(context).setDao().insertSet(new Set(14,14,34,2));
                                    getInstance(context).setDao().insertSet(new Set(14,14,34,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1576191600000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(15,1,1, new Date(1576191600000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(15,12,13,1));
                                    getInstance(context).setDao().insertSet(new Set(15,11,15,2));
                                    getInstance(context).setDao().insertSet(new Set(15,15,13,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1578870000000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(16,1,1, new Date(1578870000000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(16,16,12,1));
                                    getInstance(context).setDao().insertSet(new Set(16,16,16,2));
                                    getInstance(context).setDao().insertSet(new Set(16,12,12,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1579474800000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(17,1,1, new Date(1579474800000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(17,16,17,1));
                                    getInstance(context).setDao().insertSet(new Set(17,17,17,2));
                                    getInstance(context).setDao().insertSet(new Set(17,12,12,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1579820400000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(18,1,1, new Date(1579820400000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(18,16,12,1));
                                    getInstance(context).setDao().insertSet(new Set(18,18,18,2));
                                    getInstance(context).setDao().insertSet(new Set(18,18,12,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1580079600000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(19,1,1, new Date(1580079600000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(19,16,19,1));
                                    getInstance(context).setDao().insertSet(new Set(19,17,19,2));
                                    getInstance(context).setDao().insertSet(new Set(19,12,19,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1580338800000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(20,1,1, new Date(1580338800000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(20,20,19,1));
                                    getInstance(context).setDao().insertSet(new Set(20,20,19,2));
                                    getInstance(context).setDao().insertSet(new Set(20,12,19,3));

                                    getInstance(context).workoutSessionDao().insertWorkoutSession(new WorkoutSession(new Date(1580511600000L)));
                                    getInstance(context).sessionExerciseDao().insertSessionExercise(new SessionExercise(21,1,1, new Date(1580511600000L), ""));

                                    getInstance(context).setDao().insertSet(new Set(21,16,19,1));
                                    getInstance(context).setDao().insertSet(new Set(21,17,19,2));
                                    getInstance(context).setDao().insertSet(new Set(21,12,19,3));
                                }
                            });
                        }
                    })
                    .build();
        }
        return instance;
    }

}
