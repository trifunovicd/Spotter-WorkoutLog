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
import com.example.spotter_workoutlog.viewmodels.CategoryViewModel;
import com.example.spotter_workoutlog.viewmodels.WorkoutViewModel;

public class CategoryDialog extends AppCompatDialogFragment {

    private EditText categoryName;
    private CategoryAddDialogListener categoryAddDialogListener;
    private CategoryEditDialogListener categoryEditDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_exercise_dialog_layout, null);

        categoryName = view.findViewById(R.id.edit_category_exercise_name);

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
            builder.setTitle(getString(R.string.category_dialog_title));
        }
        else{
            builder.setTitle(getString(R.string.category_dialog_title_edit));
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
                        String category_name = categoryName.getText().toString();
                        if(!category_name.isEmpty()){
                            categoryAddDialogListener.addNewCategory(category_name);
                            //Toast.makeText(getContext(), getString(R.string.category_add_success), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        else{
                            categoryName.setError(getString(R.string.category_add_edit_fail));
                        }
                    }
                });
            }
            else {
                final int category_id = getArguments().getInt("category_id");
                String name = getArguments().getString("name");

                categoryName.setText(name);
                categoryName.requestFocus();

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String category_name = categoryName.getText().toString();
                        if(!category_name.isEmpty()){
                            categoryEditDialogListener.editCategory(category_id, category_name);
                            //Toast.makeText(getContext(), getString(R.string.category_edit_success), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        else{
                            categoryName.setError(getString(R.string.category_add_edit_fail));
                        }
                    }
                });
            }
        }
    }

    public interface CategoryAddDialogListener{
        void addNewCategory(String name);
    }

    public void setCategoryAddDialogListener(CategoryAddDialogListener listener){
        this.categoryAddDialogListener = listener;
    }

    public interface CategoryEditDialogListener{
        void editCategory(int category_id, String name);
    }

    public void setCategoryEditDialogListener(CategoryEditDialogListener listener){
        this.categoryEditDialogListener = listener;
    }
}
