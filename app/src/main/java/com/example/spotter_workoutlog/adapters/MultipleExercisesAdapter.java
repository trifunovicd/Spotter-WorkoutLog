package com.example.spotter_workoutlog.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MultipleExercisesAdapter extends RecyclerView.Adapter<MultipleExercisesAdapter.SelectExerciseHolder> implements Filterable {
    private static final String TAG = "MyActivity";
    private List<Exercise> exercises = new ArrayList<>();
    private List<Exercise> exercisesFull;
    private List<Exercise> selectedExercises = new ArrayList<>();

    @NonNull
    @Override
    public SelectExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_exercises_layout, parent, false);
        SelectExerciseHolder selectExerciseHolder = new SelectExerciseHolder(view);
        return selectExerciseHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectExerciseHolder holder, int position) {
        Exercise currentExercise = exercises.get(position);
        holder.exerciseName.setText(currentExercise.getName());

        if(currentExercise.isSelected()){
            holder.checkBox.setChecked(true);
            //Log.d(TAG, "onBindViewHolder: true " + currentExercise.getName());
        }
        else {
            holder.checkBox.setChecked(false);
            //Log.d(TAG, "onBindViewHolder: false " + currentExercise.getName());
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises){
        this.exercises = exercises;
        this.exercisesFull = new ArrayList<>(this.exercises);
        notifyDataSetChanged();
    }

    public List<Exercise> getSelectedExercises(){
        return selectedExercises;
    }

    public class SelectExerciseHolder extends RecyclerView.ViewHolder{
        private TextView exerciseName;
        private CheckBox checkBox;

        public SelectExerciseHolder(@NonNull View itemView) {
            super(itemView);

            exerciseName = itemView.findViewById(R.id.exercise_name);
            checkBox = itemView.findViewById(R.id.exercise_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }
                    else{
                        checkBox.setChecked(true);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Exercise currentExercise = exercises.get(getAdapterPosition());

                    if(isChecked){

                        if (!selectedExercises.contains(currentExercise)){
                            selectedExercises.add(currentExercise);
                            //Log.d(TAG, "onCheckedChanged: dodajem" + selectedExercises.size());
                        }

                    }
                    else{
                        selectedExercises.remove(currentExercise);
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return exercisesFilter;
    }

    private Filter exercisesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Exercise> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(exercisesFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Exercise exercise : exercisesFull) {
                    if(exercise.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(exercise);
                    }
                }
            }

            for (Exercise exercise : filteredList) {
                for (Exercise selectedExercise : selectedExercises) {
                    if(exercise.getId() == selectedExercise.getId()){
                        exercise.setSelected(true);
                        //Log.d(TAG, "performFiltering: " + exercise.getName());
                    }
                }
            }

            //Log.d(TAG, "performFiltering: selected size" + selectedExercises.size());
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exercises.clear();
            exercises.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
