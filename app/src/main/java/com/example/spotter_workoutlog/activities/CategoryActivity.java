package com.example.spotter_workoutlog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.ExerciseAdapter;
import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.dialogs.ExerciseDialog;
import com.example.spotter_workoutlog.utilities.Utility;
import com.example.spotter_workoutlog.viewmodels.ExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements ExerciseDialog.ExerciseDialogListener{

    private ExerciseViewModel exerciseViewModel;
    private FloatingActionButton addExerciseButton;
    private String category_name;
    private int category_id;
    private Handler addHandler, editHandler;
    private int mCheck;

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
                    Toast.makeText(CategoryActivity.this, "Vježba već postoji!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CategoryActivity.this, "Vježba već postoji!", Toast.LENGTH_SHORT).show();
                }

            }
        };

        addExerciseButton = findViewById(R.id.add_exercise);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseDialog();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.exercises_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter();
        recyclerView.setAdapter(exerciseAdapter);

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises(category_id).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseAdapter.setExercises(exercises);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Exercise exercise = exerciseAdapter.getExerciseAtPosition(viewHolder.getAdapterPosition());
                final int currentPosition = viewHolder.getAdapterPosition();

                switch (direction){
                    case ItemTouchHelper.LEFT:

                        new AlertDialog.Builder(CategoryActivity.this)
                                .setTitle(getString(R.string.exercise_dialog_delete_title) + " " + exercise.getName())
                                .setMessage(getString(R.string.exercise_dialog_delete_text))
                                .setCancelable(false)
                                .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton(getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        exercise.setDeleted(true);
                                        exerciseViewModel.updateExercise(exercise);

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

                        exerciseAdapter.notifyItemChanged(currentPosition);

                        break;
                    case ItemTouchHelper.RIGHT:

                        editCategoryDialog(exercise.getId(), exercise.getName());
                        exerciseAdapter.notifyItemChanged(currentPosition);

                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Utility utility = new Utility();
                utility.drawCardBackground(c, viewHolder, dX, actionState, CategoryActivity.this);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        exerciseAdapter.setOnExerciseClickListener(new ExerciseAdapter.OnExerciseClickListener() {
            @Override
            public void OnItemClick(Exercise exercise) {
                Intent intent = new Intent(CategoryActivity.this, ExerciseActivity.class);
                intent.putExtra("exercise_id", exercise.getId());
                intent.putExtra("exercise_name", exercise.getName());
                startActivity(intent);
            }
        });
    }

    public void addExerciseDialog(){
        ExerciseDialog exerciseDialog = new ExerciseDialog();
        exerciseDialog.setCancelable(false);
        exerciseDialog.show(getSupportFragmentManager(), "exercise_dialog");
    }

    public void editCategoryDialog(int exercise_id, String name){
        ExerciseDialog exerciseDialog = new ExerciseDialog();

        Bundle args = new Bundle();
        args.putInt("exercise_id", exercise_id);
        args.putString("name", name);
        exerciseDialog.setArguments(args);

        exerciseDialog.setCancelable(false);
        exerciseDialog.show(getSupportFragmentManager(), "exercise_dialog");
    }

    @Override
    public void addNewExercise(final String name) {
        exerciseViewModel.setOnFinishListener(new ExerciseViewModel.OnExerciseVMTaskFinish() {
            @Override
            public void checkIfNameExists(int check) {
                mCheck = check;
                Message message = addHandler.obtainMessage();

                if(mCheck == 0){
                    Exercise exercise = new Exercise(category_id, name, false);
                    exerciseViewModel.insertExercise(exercise);
                    message.sendToTarget();
                }
                else{
                    message.sendToTarget();
                }
            }
        });
        exerciseViewModel.checkIfNameExists(name);
    }

    @Override
    public void editExercise(final int exercise_id, final String name) {
        exerciseViewModel.setOnFinishListener(new ExerciseViewModel.OnExerciseVMTaskFinish() {
            @Override
            public void checkIfNameExists(int check) {
                mCheck = check;
                Message message = editHandler.obtainMessage();

                if(check == 0){
                    Exercise exercise = new Exercise(category_id, name, false);
                    exercise.setId(exercise_id);
                    exerciseViewModel.updateExercise(exercise);
                    message.sendToTarget();
                }
                else{
                    message.sendToTarget();
                }
            }
        });
        exerciseViewModel.checkIfNameExists(name);
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
