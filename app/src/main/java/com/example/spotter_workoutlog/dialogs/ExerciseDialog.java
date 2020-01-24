package com.example.spotter_workoutlog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.spotter_workoutlog.R;

public class ExerciseDialog extends AppCompatDialogFragment {

    private EditText exerciseName;
    private ExerciseAddDialogListener exerciseAddDialogListener;
    private ExerciseEditDialogListener exerciseEditDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_exercise_dialog_layout, null);

        exerciseName = view.findViewById(R.id.edit_category_exercise_name);

        builder.setView(view)
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

        if(getArguments() == null){
            builder.setTitle(getString(R.string.exercise_dialog_title));
        }
        else{
            builder.setTitle(getString(R.string.exercise_dialog_title_edit));
        }

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if(dialog != null){
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);

            if(getArguments() == null){

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String exercise_name = exerciseName.getText().toString();
                        if(!exercise_name.isEmpty()){
                            exerciseAddDialogListener.addNewExercise(exercise_name);
                            //Toast.makeText(getContext(), getString(R.string.exercise_add_success), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        else{
                            exerciseName.setError(getString(R.string.exercise_add_edit_fail));
                        }
                    }
                });
            }
            else {
                final int exercise_id = getArguments().getInt("exercise_id");
                String name = getArguments().getString("name");

                exerciseName.setText(name);
                exerciseName.requestFocus();

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String exercise_name = exerciseName.getText().toString();
                        if(!exercise_name.isEmpty()){
                            exerciseEditDialogListener.editExercise(exercise_id, exercise_name);
                            //Toast.makeText(getContext(), getString(R.string.exercise_edit_success), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        else{
                            exerciseName.setError(getString(R.string.exercise_add_edit_fail));
                        }
                    }
                });
            }
        }
    }

    public interface ExerciseAddDialogListener{
        void addNewExercise(String name);
    }

    public void setExerciseAddDialogListener(ExerciseAddDialogListener listener){
        this.exerciseAddDialogListener = listener;
    }

    public interface ExerciseEditDialogListener{
        void editExercise(int exercise_id, String name);
    }

    public void setExerciseEditDialogListener(ExerciseEditDialogListener listener){
        this.exerciseEditDialogListener = listener;
    }
}
