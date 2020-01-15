package com.example.spotter_workoutlog.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.activities.EditSessionExerciseActivity;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;
import com.example.spotter_workoutlog.database.models.SessionExercise;


public class HistoryItemDialog extends AppCompatDialogFragment {

    private ImageButton edit, delete;
    private ExerciseHistoryItem exerciseHistoryItem;
    private String currentExerciseName;
    private HistoryItemDialogListener listener;
    //private SessionExercise sessionExercise;

    public HistoryItemDialog(ExerciseHistoryItem exerciseHistoryItem, String currentExerciseName) {
        this.exerciseHistoryItem = exerciseHistoryItem;
        this.currentExerciseName = currentExerciseName;
       /* this.sessionExercise.setId(exerciseHistoryItem.getId());
        this.sessionExercise.setWorkout_session_id(exerciseHistoryItem.getWorkout_session_id());
        this.sessionExercise.setExercise_id(exerciseHistoryItem.getExercise_id());
        this.sessionExercise.setOrder(exerciseHistoryItem.getOrder());
        this.sessionExercise.setDate(exerciseHistoryItem.getDate());
        this.sessionExercise.setNote(exerciseHistoryItem.getNote());*/
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.history_item_dialog_layout, null);

        edit = view.findViewById(R.id.history_item_edit_button);
        delete = view.findViewById(R.id.history_item_delete_button);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditSessionExerciseActivity.class);
                intent.putExtra("exerciseHistoryItem", exerciseHistoryItem);
                intent.putExtra("currentExerciseName", currentExerciseName);
                startActivity(intent);
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.history_item_delete_title))
                        .setMessage(getString(R.string.history_item_delete_text))
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
                                SessionExercise sessionExercise = new SessionExercise(exerciseHistoryItem.getWorkout_session_id(),exerciseHistoryItem.getExercise_id(),exerciseHistoryItem.getOrder(),exerciseHistoryItem.getDate(),exerciseHistoryItem.getNote());
                                sessionExercise.setId(exerciseHistoryItem.getId());
                                listener.DeleteHistoryItem(sessionExercise);
                                dialog.dismiss();
                            }
                        }).show();
                dismiss();
            }
        });


        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if(dialog != null){
            dialog.getWindow().setLayout(500,240);
        }

/*
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 500;
        lp.height = 240;
        dialog.getWindow().setAttributes(lp);*/

    }

    public interface HistoryItemDialogListener{
        void DeleteHistoryItem(SessionExercise sessionExercise);
    }

    public void setHistoryItemDialogListener(HistoryItemDialogListener listener){
        this.listener = listener;
    }
}
