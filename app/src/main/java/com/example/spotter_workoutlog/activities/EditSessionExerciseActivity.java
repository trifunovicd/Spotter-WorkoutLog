package com.example.spotter_workoutlog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.SetAdapter;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.dialogs.SetDialog;
import com.example.spotter_workoutlog.utilities.Utility;
import com.example.spotter_workoutlog.viewmodels.WorkoutViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class EditSessionExerciseActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private ExerciseHistoryItem exerciseHistoryItem;
    private String currentExerciseName;
    private WorkoutViewModel workoutViewModel;
    private CardView setsCardView, noteCardView;
    private TextInputEditText note;
    private List<Set> sets = new ArrayList<>();
    private List<Set> deletedSets = new ArrayList<>();
    private SetAdapter setsAdapter;
    private Utility utility = new Utility();
    private Boolean show_note = true;
    private Boolean show_note_on_rotation = false;
    private int setOrder = 0;
    private int order = 0;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("sets", (Serializable) sets);
        outState.putSerializable("deletedSets", (Serializable) deletedSets);
        outState.putInt("order", order);
        outState.putBoolean("show_note_on_rotation", show_note_on_rotation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session_exercise);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            exerciseHistoryItem = (ExerciseHistoryItem) extras.getSerializable("exerciseHistoryItem");

            if(extras.getString("currentExerciseName") == null){
                currentExerciseName = getString(R.string.deleted_exercise_name);
            }
            else{
                currentExerciseName = extras.getString("currentExerciseName");
            }

        }

        setTitle(currentExerciseName);

        sets = exerciseHistoryItem.getSets();
        order = exerciseHistoryItem.getSets().size();

        if(savedInstanceState != null){
            sets = (List<Set>)savedInstanceState.getSerializable("sets");
            deletedSets = (List<Set>)savedInstanceState.getSerializable("deletedSets");
            order = savedInstanceState.getInt("order");
            show_note_on_rotation = savedInstanceState.getBoolean("show_note_on_rotation");

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        final TextInputEditText reps = findViewById(R.id.exercise_input_reps);
        FloatingActionButton reps_sub_button = findViewById(R.id.rep_sub);
        FloatingActionButton reps_add_button = findViewById(R.id.rep_add);
        final TextInputEditText weight = findViewById(R.id.exercise_input_weight);
        FloatingActionButton weight_sub_button = findViewById(R.id.weight_sub);
        FloatingActionButton weight_add_button = findViewById(R.id.weight_add);
        FloatingActionButton add_set_button = findViewById(R.id.add_set);

        note = findViewById(R.id.exercise_input_note);
        note.setText(exerciseHistoryItem.getNote());

        noteCardView = findViewById(R.id.note_card_view);
        ShowNoteOnRotation();

        setsCardView = findViewById(R.id.sets_card_view);
        SetVisibility();

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.performed_sets_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        setsAdapter = new SetAdapter();
        recyclerView.setAdapter(setsAdapter);
        setsAdapter.setSets(sets);

        setsAdapter.setOnSetClickListener(new SetAdapter.OnSetClickListener() {
            @Override
            public void OnEditClick(Set set) {
                //setId = set.getId();
                setOrder = set.getOrder();
                SetDialog setDialog = new SetDialog();

                Bundle args = new Bundle();
                args.putInt("reps", set.getReps());
                args.putFloat("weight", set.getWeight());
                setDialog.setArguments(args);

                setDialog.setCancelable(false);
                setDialog.show(getSupportFragmentManager(), "set_dialog");

                setDialog.setClickListener(new SetDialog.SetDialogListener() {
                    @Override
                    public void editSet(int reps, float weight) {
                        for (Set set : sets) {
                            if(setOrder == set.getOrder()){//if(setId == set.getId()){
                                set.setReps(reps);
                                set.setWeight(weight);
                                setsAdapter.setSets(sets);
                            }
                        }
                    }
                });
            }

            @Override
            public void OnDeleteClick(final Set set) {
                //setId = set.getId();
                //setOrder = set.getOrder();

                new AlertDialog.Builder(EditSessionExerciseActivity.this)
                        .setTitle(getString(R.string.set_dialog_delete_title))
                        .setMessage(getString(R.string.set_dialog_delete_text))
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Log.d(TAG, "onClick: " + setOrder);//Log.d(TAG, "onClick: " + setId);
                                //sets.remove(setOrder-1);//sets.remove(setId-1);

                                //workoutViewModel.deleteSet(set);
                                deletedSets.add(set);
                                sets.remove(set);

                                int list_order = 0;
                                for (Set set : sets) {
                                    list_order++;
                                    set.setOrder(list_order);
                                }
                                order = sets.size();
                                setsAdapter.setSets(sets);
                                SetVisibility();
                            }
                        }).show();
            }
        });

        reps_sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.RepsSubtract(reps);
            }
        });

        reps_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.RepsAdd(reps);
            }
        });

        weight_sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.WeightSubtract(weight);

            }
        });

        weight_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.WeightAdd(weight);
            }
        });

        add_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(reps.getText())){
                    if(!TextUtils.isEmpty(weight.getText())){
                        int set_reps = Integer.parseInt(reps.getText().toString().trim());
                        float set_weight = Float.parseFloat(weight.getText().toString().trim());
                        order = order + 1;
                        Set newSet = new Set(exerciseHistoryItem.getId(),set_reps,set_weight,order);
                        //newSet.setId(order);
                        sets.add(newSet);
                        setsAdapter.setSets(sets);
                        SetVisibility();
                    }
                    else{
                        weight.setError(getString(R.string.edit_set_weight_empty));
                    }
                }
                else{
                    reps.setError(getString(R.string.edit_set_reps_empty));
                }

            }
        });
    }

    private void SetVisibility(){
        if(sets.isEmpty()){
            setsCardView.setVisibility(View.GONE);
        }
        else{
            setsCardView.setVisibility(View.VISIBLE);
        }
    }

    private void ShowNote(){
        if(show_note){
            noteCardView.setVisibility(View.VISIBLE);
            show_note = false;
            show_note_on_rotation = true;
        }
        else{
            noteCardView.setVisibility(View.GONE);
            show_note = true;
            show_note_on_rotation = false;
        }
    }

    private void ShowNoteOnRotation(){
        if(show_note_on_rotation){
            noteCardView.setVisibility(View.VISIBLE);
            show_note = false;
        }
        else{
            noteCardView.setVisibility(View.GONE);
            show_note = true;
        }
    }

    private void SaveSets(){
        if(!sets.isEmpty()){
            if(!deletedSets.isEmpty()){
                workoutViewModel.deleteSets(deletedSets);
            }
            SessionExercise sessionExercise = new SessionExercise(exerciseHistoryItem.getWorkout_session_id(),exerciseHistoryItem.getExercise_id(),exerciseHistoryItem.getOrder(),exerciseHistoryItem.getDate(),note.getText().toString());
            sessionExercise.setId(exerciseHistoryItem.getId());
            workoutViewModel.updateSessionExercise(sessionExercise);
            workoutViewModel.insertAllSets(sets);
            Toast.makeText(this, getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.save_sets_dialog_title))
                    .setMessage(getString(R.string.save_sets_dialog_text))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.save_sets_dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sets_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_sets){
            SaveSets();
            return true;
        }
        else if(item.getItemId() == R.id.add_note){
            ShowNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.back_button_dialog_title))
                .setMessage(getString(R.string.back_button_dialog_text))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveSets();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_discard), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
