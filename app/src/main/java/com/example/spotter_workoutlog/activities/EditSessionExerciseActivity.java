package com.example.spotter_workoutlog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;


public class EditSessionExerciseActivity extends AppCompatActivity {

    private ExerciseHistoryItem exerciseHistoryItem;
    private String currentExerciseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session_exercise);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            exerciseHistoryItem = (ExerciseHistoryItem) extras.getSerializable("exerciseHistoryItem");
            currentExerciseName = extras.getString("currentExerciseName");
        }

        setTitle(currentExerciseName);
    }
}
