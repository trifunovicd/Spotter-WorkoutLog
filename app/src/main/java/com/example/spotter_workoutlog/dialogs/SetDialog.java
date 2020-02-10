package com.example.spotter_workoutlog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.utilities.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class SetDialog extends AppCompatDialogFragment {
    private TextInputEditText reps;
    private TextInputEditText weight;
    private FloatingActionButton reps_sub_button, reps_add_button, weight_sub_button, weight_add_button;
    private SetDialogListener listener;
    private Utility utility = new Utility();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_dialog_layout, null);

        reps = view.findViewById(R.id.edit_reps);
        weight = view.findViewById(R.id.edit_weight);

        reps_sub_button = view.findViewById(R.id.sub_rep_edit);
        reps_add_button = view.findViewById(R.id.add_rep_edit);
        weight_sub_button = view.findViewById(R.id.sub_weight_edit);
        weight_add_button = view.findViewById(R.id.add_weight_edit);

        builder.setView(view)
                .setTitle(getString(R.string.edit_set_title))
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

            int reps_number = getArguments().getInt("reps");
            float weight_number = getArguments().getFloat("weight");

            reps.setText(String.valueOf(reps_number));
            weight.setText(String.valueOf(weight_number));

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

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(reps.getText())){
                        if(!TextUtils.isEmpty(weight.getText())){
                            int reps_quantity = Integer.parseInt(reps.getText().toString().trim());
                            float weight_quantity = Float.parseFloat(weight.getText().toString().trim());
                            listener.editSet(reps_quantity, weight_quantity);
                            dismiss();
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
    }

    public interface SetDialogListener{
        void editSet(int reps, float weight);
    }

    public void setClickListener(SetDialogListener listener){
        this.listener = listener;
    }
}
