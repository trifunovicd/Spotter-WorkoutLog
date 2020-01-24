package com.example.spotter_workoutlog.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.MultipleExercisesAdapter;
import com.example.spotter_workoutlog.database.models.Exercise;
import com.example.spotter_workoutlog.database.models.ExerciseInCategory;
import com.example.spotter_workoutlog.viewmodels.ExerciseViewModel;

import java.util.List;

public class MultipleExercisesDialog extends AppCompatDialogFragment {
    private static final String TAG = "MyActivity";
    private RecyclerView recyclerView;
    private SearchView searchView;
    private MultipleExercisesAdapter multipleExercisesAdapter;
    private ExerciseViewModel exerciseViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.multiple_exercises_dialog_layout, null);

        recyclerView = view.findViewById(R.id.select_exercise_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        multipleExercisesAdapter = new MultipleExercisesAdapter();
        recyclerView.setAdapter(multipleExercisesAdapter);

        searchView = view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                multipleExercisesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                multipleExercisesAdapter.setExercises(exercises);
            }
        });

        builder.setView(view)
                .setTitle(getString(R.string.add_existing_exercise))
                .setNegativeButton(getString(R.string.category_exercise_dialog_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.category_exercise_dialog_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if(dialog != null){
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Exercise> exercises = multipleExercisesAdapter.getSelectedExercises();
                    if(!exercises.isEmpty()){
                        int category_id = getArguments().getInt("category_id");
                        for (Exercise exercise: exercises) {
                            Log.d(TAG, "onClick: "+ exercise.getName());
                            ExerciseInCategory exerciseInCategory = new ExerciseInCategory(category_id, exercise.getId());
                            exerciseViewModel.insertExerciseInCategory(exerciseInCategory);
                        }
                        dismiss();
                    }
                    else{
                        Toast.makeText(getActivity(), getString(R.string.multiple_exercise_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
