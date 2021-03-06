package com.example.spotter_workoutlog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.ExerciseAdapter;
import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.database.models.ExerciseInCategory;
import com.example.spotter_workoutlog.dialogs.ExerciseDialog;
import com.example.spotter_workoutlog.dialogs.MultipleExercisesDialog;
import com.example.spotter_workoutlog.utilities.Utility;
import com.example.spotter_workoutlog.viewmodels.ExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CategoryActivity extends AppCompatActivity{

    private ExerciseViewModel exerciseViewModel;
    private FloatingActionButton addExerciseButton, addSingle, addMultiple;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen = false;
    private String category_name;
    private int category_id;
    private Handler addHandler, editHandler;
    private int mCheck;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            category_name = extras.getString("category_name");
            category_id = extras.getInt("category_id");
        }

        setTitle(category_name);


        addHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(CategoryActivity.this, getString(R.string.exercise_add_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CategoryActivity.this, getString(R.string.exercise_add_fail), Toast.LENGTH_SHORT).show();
                }

            }
        };

        editHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(CategoryActivity.this, getString(R.string.exercise_edit_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CategoryActivity.this, getString(R.string.exercise_add_fail), Toast.LENGTH_SHORT).show();
                }

            }
        };

        addExerciseButton = findViewById(R.id.add_exercise);
        addSingle = findViewById(R.id.add_single_exercise);
        addMultiple = findViewById(R.id.add_multiple_exercises);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        addSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                addExerciseDialog();
            }
        });

        addMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                MultipleExercisesDialog multipleExercisesDialog = new MultipleExercisesDialog();
                Bundle args = new Bundle();
                args.putInt("category_id", category_id);
                multipleExercisesDialog.setArguments(args);
                multipleExercisesDialog.setCancelable(false);
                multipleExercisesDialog.show(getSupportFragmentManager(), "multiple_exercise_dialog");
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.exercises_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter();
        recyclerView.setAdapter(exerciseAdapter);

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercisesInCategory(category_id).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseAdapter.setExercises(exercises);
            }
        });

        exerciseAdapter.setOnExerciseClickListener(new ExerciseAdapter.OnExerciseClickListener() {
            @Override
            public void OnItemClick(Exercise exercise) {
                Intent intent = new Intent(CategoryActivity.this, ExerciseActivity.class);
                intent.putExtra("exercise_id", exercise.getId());
                intent.putExtra("exercise_name", exercise.getName());
                startActivity(intent);
            }
        });

        exerciseAdapter.setOnExerciseLongClickListener(new ExerciseAdapter.OnExerciseLongClickListener() {
            @Override
            public void OnItemClick(final Exercise exercise, int position) {

                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                final CardView cardView = viewHolder.itemView.findViewById(R.id.category_exercise_card_view);

                if(actionMode == null){
                    actionMode = startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.getMenuInflater().inflate(R.menu.action_mode_category_menu, menu);
                            mode.setTitle(getString(R.string.item_selected_title));
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.edit_item:
                                    editExerciseDialog(exercise.getId(), exercise.getName());
                                    actionMode.finish();
                                    return true;
                                case R.id.delete_item:
                                    new AlertDialog.Builder(CategoryActivity.this)
                                            .setTitle(getString(R.string.exercise_dialog_delete_title) + " " + exercise.getName())
                                            .setMessage(getString(R.string.exercise_dialog_delete_text))
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
                                                    exercise.setDeleted(true);
                                                    exerciseViewModel.updateExercise(exercise);
                                                    actionMode.finish();
                                                    Snackbar.make(recyclerView, getString(R.string.snackbar_exercise_text_part1) + " " + exercise.getName() + " " + getString(R.string.snackbar_exercise_text_part2), Snackbar.LENGTH_LONG)
                                                            .setAction(getString(R.string.snackbar_cancel), new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    exercise.setDeleted(false);
                                                                    exerciseViewModel.updateExercise(exercise);
                                                                }
                                                            })
                                                            .addCallback(new Snackbar.Callback(){
                                                                @Override
                                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                                    super.onDismissed(transientBottomBar, event);
                                                                    if(event == DISMISS_EVENT_TIMEOUT){
                                                                        exerciseViewModel.deleteExercise(exercise);
                                                                    }

                                                                }
                                                            }).show();
                                                }
                                            }).show();
                                    return true;
                                case R.id.remove_item:
                                    new AlertDialog.Builder(CategoryActivity.this)
                                            .setTitle(getString(R.string.exercise_dialog_remove_title) + " " + exercise.getName())
                                            .setMessage(getString(R.string.exercise_dialog_remove_text))
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
                                                    ExerciseInCategory exerciseInCategory = new ExerciseInCategory(category_id, exercise.getId());
                                                    exerciseViewModel.deleteExerciseInCategory(exerciseInCategory);
                                                    actionMode.finish();
                                                }
                                            }).show();
                                    return true;
                                default:
                                    return false;
                            }

                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            actionMode = null;
                            cardView.setCardBackgroundColor(Color.WHITE);
                        }
                    });
                    cardView.setCardBackgroundColor(Color.LTGRAY);
                }
            }
        });
    }

    private void animateFab(){

        if(isOpen){
            addExerciseButton.startAnimation(rotateBackward);
            addSingle.startAnimation(fabClose);
            addMultiple.startAnimation(fabClose);
            addSingle.setClickable(false);
            addMultiple.setClickable(false);
            isOpen = false;
        }
        else{
            addExerciseButton.startAnimation(rotateForward);
            addSingle.startAnimation(fabOpen);
            addMultiple.startAnimation(fabOpen);
            addSingle.setClickable(true);
            addMultiple.setClickable(true);
            isOpen = true;
        }
    }

    private void addExerciseDialog(){

        exerciseViewModel.setOnInsertVMFinishListener(new ExerciseViewModel.OnExerciseInsertVMFinish() {
            @Override
            public void lastExerciseId(Long exercise_id) {
                ExerciseInCategory exerciseInCategory = new ExerciseInCategory(category_id, exercise_id.intValue());
                exerciseViewModel.insertExerciseInCategory(exerciseInCategory);
            }
        });

        ExerciseDialog exerciseDialog = new ExerciseDialog();

        exerciseDialog.setExerciseAddDialogListener(new ExerciseDialog.ExerciseAddDialogListener() {
            @Override
            public void addNewExercise(final String name) {
                exerciseViewModel.setOnFinishListener(new ExerciseViewModel.OnExerciseVMTaskFinish() {
                    @Override
                    public void checkIfNameExists(int check) {
                        mCheck = check;
                        Message message = addHandler.obtainMessage();

                        if(mCheck == 0){
                            Exercise exercise = new Exercise(name, false);
                            exerciseViewModel.insertExercise(exercise, true);
                        }
                        message.sendToTarget();
                    }
                });
                exerciseViewModel.checkIfNameExists(name);
            }
        });

        exerciseDialog.setCancelable(false);
        exerciseDialog.show(getSupportFragmentManager(), "exercise_dialog");
    }

    private void editExerciseDialog(int exercise_id, String name){
        ExerciseDialog exerciseDialog = new ExerciseDialog();

        exerciseDialog.setExerciseEditDialogListener(new ExerciseDialog.ExerciseEditDialogListener() {
            @Override
            public void editExercise(final int exercise_id, final String name) {
                exerciseViewModel.setOnFinishListener(new ExerciseViewModel.OnExerciseVMTaskFinish() {
                    @Override
                    public void checkIfNameExists(int check) {
                        mCheck = check;
                        Message message = editHandler.obtainMessage();

                        if(check == 0){
                            Exercise exercise = new Exercise(name, false);
                            exercise.setId(exercise_id);
                            exerciseViewModel.updateExercise(exercise);
                        }
                        message.sendToTarget();
                    }
                });
                exerciseViewModel.checkIfNameExists(name);
            }
        });

        Bundle args = new Bundle();
        args.putInt("exercise_id", exercise_id);
        args.putString("name", name);
        exerciseDialog.setArguments(args);

        exerciseDialog.setCancelable(false);
        exerciseDialog.show(getSupportFragmentManager(), "exercise_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.calendar_menu:
                Intent calendar_intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(calendar_intent);
                return true;
            case R.id.settings_menu:
                Intent settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
