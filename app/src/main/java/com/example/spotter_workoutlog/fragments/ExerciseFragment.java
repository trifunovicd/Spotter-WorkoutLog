package com.example.spotter_workoutlog.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.activities.ExerciseActivity;
import com.example.spotter_workoutlog.adapters.ExerciseAdapter;
import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.dialogs.ExerciseDialog;
import com.example.spotter_workoutlog.viewmodels.ExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class ExerciseFragment extends Fragment {
    private static final String TAG = "MyActivity";
    private ExerciseViewModel exerciseViewModel;
    private FloatingActionButton addExerciseButton;
    private Handler addHandler, editHandler;
    private int mCheck;
    public static ActionMode actionMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(getActivity(), getString(R.string.exercise_add_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.exercise_add_fail), Toast.LENGTH_SHORT).show();
                }

            }
        };

        editHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(getActivity(), getString(R.string.exercise_edit_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.exercise_add_fail), Toast.LENGTH_SHORT).show();
                }

            }
        };

        addExerciseButton = view.findViewById(R.id.add_exercise);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseDialog();
            }
        });

        final RecyclerView recyclerView = view.findViewById(R.id.exercises_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter exerciseAdapter = new ExerciseAdapter();
        recyclerView.setAdapter(exerciseAdapter);

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseAdapter.setExercises(exercises);
            }
        });

        exerciseAdapter.setOnExerciseClickListener(new ExerciseAdapter.OnExerciseClickListener() {
            @Override
            public void OnItemClick(Exercise exercise) {
                Intent intent = new Intent(getActivity(), ExerciseActivity.class);
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
                    actionMode = getActivity().startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
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
                                    new AlertDialog.Builder(getActivity())
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

    private void addExerciseDialog(){
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
                            exerciseViewModel.insertExercise(exercise,false);
                        }
                        message.sendToTarget();
                    }
                });
                exerciseViewModel.checkIfNameExists(name);
            }
        });

        exerciseDialog.setCancelable(false);
        exerciseDialog.show(getActivity().getSupportFragmentManager(), "exercise_dialog");
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
        exerciseDialog.show(getActivity().getSupportFragmentManager(), "exercise_dialog");
    }
}
