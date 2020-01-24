package com.example.spotter_workoutlog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private OnExerciseClickListener onExerciseClickListener;
    private OnExerciseLongClickListener onExerciseLongClickListener;

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_exercise_layout, parent, false);
        ExerciseHolder exerciseHolder = new ExerciseHolder(view);
        return exerciseHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Exercise currentExercise = exercises.get(position);
        holder.exerciseName.setText(currentExercise.getName());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises){
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public class ExerciseHolder extends RecyclerView.ViewHolder{
        private TextView exerciseName;

        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);

            exerciseName = itemView.findViewById(R.id.category_exercise_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(onExerciseClickListener != null && position != RecyclerView.NO_POSITION) {
                        onExerciseClickListener.OnItemClick(exercises.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(onExerciseLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onExerciseLongClickListener.OnItemClick(exercises.get(position), position);
                    }
                    return true;
                }
            });

        }
    }

    public interface OnExerciseClickListener{
        void OnItemClick(Exercise exercise);
    }

    public void setOnExerciseClickListener(OnExerciseClickListener listener){
        this.onExerciseClickListener = listener;
    }

    public interface OnExerciseLongClickListener{
        void OnItemClick(Exercise exercise, int position);
    }

    public void setOnExerciseLongClickListener(OnExerciseLongClickListener listener){
        this.onExerciseLongClickListener = listener;
    }
}
