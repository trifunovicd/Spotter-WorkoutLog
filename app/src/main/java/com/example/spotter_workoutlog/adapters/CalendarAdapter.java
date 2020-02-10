package com.example.spotter_workoutlog.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;
import com.example.spotter_workoutlog.database.models.WorkoutStats;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "MyActivity";
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    private List<ExerciseHistoryItem> exerciseHistoryItems = new ArrayList<>();
    private List<String> exerciseNames = new ArrayList<>();
    private WorkoutStats workoutStats = new WorkoutStats(0,0,0f);
    private OnCalendarItemLongClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_header_layout, parent, false);
            CalendarHeaderHolder calendarHeaderHolder = new CalendarHeaderHolder(view);
            return calendarHeaderHolder;
        }
        else if(viewType == TYPE_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_history_layout, parent, false);
            CalendarItemHolder calendarItemHolder = new CalendarItemHolder(view);
            return calendarItemHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEAD){
            ((CalendarHeaderHolder) holder).header_exercises.setText(String.valueOf(exerciseHistoryItems.size()));
            ((CalendarHeaderHolder) holder).header_sets.setText(String.valueOf(workoutStats.getSets()));
            ((CalendarHeaderHolder) holder).header_reps.setText(String.valueOf(workoutStats.getReps()));

            if(workoutStats.getWeight() == Math.round(workoutStats.getWeight())){
                ((CalendarHeaderHolder) holder).header_weight.setText(String.valueOf((int)workoutStats.getWeight()));
            }
            else
            {
                ((CalendarHeaderHolder) holder).header_weight.setText(String.valueOf(workoutStats.getWeight()));
            }
        }
        else {
            position--;
            ExerciseHistoryItem exerciseHistoryItem = exerciseHistoryItems.get(position);

            if(exerciseNames.get(position) != null){
                ((CalendarItemHolder) holder).name.setText(exerciseNames.get(position));
            }
            else{
                ((CalendarItemHolder) holder).name.setText(exerciseNames.get(exerciseNames.size()-1));
            }

            ((CalendarItemHolder) holder).time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(exerciseHistoryItem.getDate()));

            if(!exerciseHistoryItem.getNote().isEmpty()){
                ((CalendarItemHolder) holder).noteLayout.setVisibility(View.VISIBLE);
                ((CalendarItemHolder) holder).note.setText(exerciseHistoryItem.getNote());
            }
            else{
                ((CalendarItemHolder) holder).noteLayout.setVisibility(View.GONE);
            }

            ExerciseHistorySetAdapter exerciseHistorySetAdapter = new ExerciseHistorySetAdapter();
            exerciseHistorySetAdapter.setSets(exerciseHistoryItem.getSets());

            ((CalendarItemHolder) holder).setsRecyclerView.setLayoutManager(new LinearLayoutManager(((CalendarItemHolder) holder).setsRecyclerView.getContext()));
            ((CalendarItemHolder) holder).setsRecyclerView.setHasFixedSize(true);
            ((CalendarItemHolder) holder).setsRecyclerView.setAdapter(exerciseHistorySetAdapter);

            exerciseHistorySetAdapter.setOnHistoryItemLongClickListener(new ExerciseHistorySetAdapter.OnSetItemLongClickListener() {
                @Override
                public void OnItemClick(int session_exercise_id) {
                    for (int position = 0; position < exerciseHistoryItems.size(); position++){
                        if (exerciseHistoryItems.get(position).getId() == session_exercise_id){
                            listener.OnItemClick(exerciseHistoryItems.get(position), position+1, exerciseNames.get(position));
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return exerciseHistoryItems.size() + 1;
    }

    public void setExerciseHistoryItems(List<ExerciseHistoryItem> exerciseHistoryItems){
        this.exerciseHistoryItems = exerciseHistoryItems;
        notifyDataSetChanged();
    }

    public void setExerciseNames(List<String> exerciseNames){
        this.exerciseNames = exerciseNames;
    }

    public void setWorkoutStats(WorkoutStats workoutStats){
        this.workoutStats = workoutStats;
    }

    public class CalendarHeaderHolder extends RecyclerView.ViewHolder{
        TextView header_exercises,header_sets,header_reps,header_weight;

        public CalendarHeaderHolder(@NonNull View itemView) {
            super(itemView);

            header_exercises = itemView.findViewById(R.id.exercises_value);
            header_sets = itemView.findViewById(R.id.sets_value);
            header_reps = itemView.findViewById(R.id.reps_value);
            header_weight = itemView.findViewById(R.id.weight_value);
        }
    }

    public class CalendarItemHolder extends RecyclerView.ViewHolder{
        private TextView name, time, note;
        private RecyclerView setsRecyclerView;
        private LinearLayout noteLayout;

        public CalendarItemHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.exercise_history_date_name);
            time = itemView.findViewById(R.id.exercise_history_time);
            note = itemView.findViewById(R.id.exercise_history_note);
            setsRecyclerView = itemView.findViewById(R.id.exercise_history_sets_recycler_view);
            noteLayout = itemView.findViewById(R.id.note_layout_true);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(exerciseHistoryItems.get(position-1), position, exerciseNames.get(position-1));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEAD;
        }
        else{
            return TYPE_LIST;
        }
    }

    public interface OnCalendarItemLongClickListener{
        void OnItemClick(ExerciseHistoryItem exerciseHistoryItem, int position, String name);
    }

    public void setOnCalendarItemLongClickListener(OnCalendarItemLongClickListener listener){
        this.listener = listener;
    }
}
