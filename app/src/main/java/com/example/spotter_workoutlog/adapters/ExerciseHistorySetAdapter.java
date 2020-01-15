package com.example.spotter_workoutlog.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.Set;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHistorySetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    private List<Set> sets = new ArrayList<>();

    //////////
    private OnSetItemLongClickListener listener;
    private static final String TAG = "MyActivity";
    //////////

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_history_header_layout, parent, false);
            ExerciseHistoryHeaderHolder exerciseHistoryHeaderHolder = new ExerciseHistoryHeaderHolder(view);
            return exerciseHistoryHeaderHolder;
        }
        else if(viewType == TYPE_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_history_sets_layout, parent, false);
            ExerciseHistorySetHolder exerciseHistorySetHolder = new ExerciseHistorySetHolder(view);
            return exerciseHistorySetHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEAD){
            ((ExerciseHistoryHeaderHolder) holder).header_set.setText(R.string.set);
            ((ExerciseHistoryHeaderHolder) holder).header_reps.setText(R.string.reps);
            ((ExerciseHistoryHeaderHolder) holder).header_weight.setText(R.string.weight);

        }
        else {
            Set currentSet = sets.get(position - 1);
            ((ExerciseHistorySetHolder) holder).set.setText(String.valueOf(currentSet.getOrder()));
            ((ExerciseHistorySetHolder) holder).rep.setText(String.valueOf(currentSet.getReps()));

            float result = currentSet.getWeight() - (int)currentSet.getWeight();
            if (result != 0){
                ((ExerciseHistorySetHolder) holder).weight.setText(String.valueOf(currentSet.getWeight()));
            }
            else{
                ((ExerciseHistorySetHolder) holder).weight.setText(String.valueOf((int)currentSet.getWeight()));
            }

            //((ExerciseHistorySetHolder) holder).weight.setText(String.valueOf(currentSet.getWeight()));
        }
    }

    @Override
    public int getItemCount() {
        return sets.size() + 1;
    }

    public void setSets(List<Set> sets){
        this.sets = sets;
        notifyDataSetChanged();
    }

    public class ExerciseHistoryHeaderHolder extends RecyclerView.ViewHolder{

        TextView header_set,header_reps,header_weight;

        public ExerciseHistoryHeaderHolder(@NonNull View itemView) {
            super(itemView);

            header_set = itemView.findViewById(R.id.exercise_history_header_set);
            header_reps = itemView.findViewById(R.id.exercise_history_header_rep);
            header_weight = itemView.findViewById(R.id.exercise_history_header_weight);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(sets.get(position).getSession_exercise_id());
                    }
                    return true;
                }
            });
        }
    }
    public class ExerciseHistorySetHolder extends RecyclerView.ViewHolder {
        TextView set, rep, weight;

        public ExerciseHistorySetHolder(@NonNull View itemView) {
            super(itemView);

            set = itemView.findViewById(R.id.exercise_history_set);
            rep = itemView.findViewById(R.id.exercise_history_rep);
            weight = itemView.findViewById(R.id.exercise_history_weight);

            /////////////////////////
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(sets.get(position-1).getSession_exercise_id());
                    }
                    return true;
                }
            });//////////////////////
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

///////////////////////

    public interface OnSetItemLongClickListener{
        void OnItemClick(int session_exercise_id);
    }

    public void setOnHistoryItemLongClickListener(OnSetItemLongClickListener listener){
        this.listener = listener;
    }
}
