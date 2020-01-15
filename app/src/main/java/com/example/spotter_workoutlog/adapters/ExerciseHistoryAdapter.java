package com.example.spotter_workoutlog.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.dialogs.HistoryItemDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExerciseHistoryAdapter extends RecyclerView.Adapter<ExerciseHistoryAdapter.ExerciseHistoryItemHolder>{
    private static final String TAG = "MyActivity";
    //private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<ExerciseHistoryItem> exerciseHistoryItems = new ArrayList<>();
    private OnHistoryItemLongClickListener listener;

    @NonNull
    @Override
    public ExerciseHistoryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_history_layout, parent, false);
        ExerciseHistoryItemHolder exerciseHistoryItemHolder = new ExerciseHistoryItemHolder(view);
        return exerciseHistoryItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseHistoryItemHolder holder, int position) {
        ExerciseHistoryItem exerciseHistoryItem = exerciseHistoryItems.get(position);
        holder.date.setText(DateFormat.getDateInstance(DateFormat.LONG).format(exerciseHistoryItem.getDate()));
        holder.time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(exerciseHistoryItem.getDate()));
        if(!exerciseHistoryItem.getNote().isEmpty()){
           // holder.note.setText(holder.itemView.getContext().getString (R.string.note_2, exerciseHistoryItem.getNote()));
            holder.noteLayout.setVisibility(View.VISIBLE);
            holder.note.setText(exerciseHistoryItem.getNote());
        }
        else{
            holder.noteLayout.setVisibility(View.GONE);
        }


//holder.date.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(exerciseHistoryItem.getDate()));

/*
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.setsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(exerciseHistoryItem.getSets().size());*/

        ExerciseHistorySetAdapter exerciseHistorySetAdapter = new ExerciseHistorySetAdapter();
        exerciseHistorySetAdapter.setSets(exerciseHistoryItem.getSets());

        holder.setsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.setsRecyclerView.getContext()));
        holder.setsRecyclerView.setHasFixedSize(true);
        holder.setsRecyclerView.setAdapter(exerciseHistorySetAdapter);
        //holder.setsRecyclerView.setRecycledViewPool(viewPool);

        exerciseHistorySetAdapter.setOnHistoryItemLongClickListener(new ExerciseHistorySetAdapter.OnSetItemLongClickListener() {
            @Override
            public void OnItemClick(int session_exercise_id) {/*
                for (ExerciseHistoryItem exerciseHistoryItem : exerciseHistoryItems) {
                    if(exerciseHistoryItem.getId() == session_exercise_id){
                        exerciseHistoryItems.get()
                        listener.OnItemClick(exerciseHistoryItem, mCardView);
                    }
                }*/

                for (int position = 0; position < exerciseHistoryItems.size(); position++){
                    if (exerciseHistoryItems.get(position).getId() == session_exercise_id){
                        listener.OnItemClick(exerciseHistoryItems.get(position), position);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseHistoryItems.size();
    }

    public void setExerciseHistoryItems(List<ExerciseHistoryItem> exerciseHistoryItems){
        this.exerciseHistoryItems = exerciseHistoryItems;
        notifyDataSetChanged();
    }

    public class ExerciseHistoryItemHolder extends RecyclerView.ViewHolder{
        private TextView date, time, note;
        private RecyclerView setsRecyclerView;
        private LinearLayout noteLayout;

        //private CardView cardView;

        public ExerciseHistoryItemHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.exercise_history_date);
            time = itemView.findViewById(R.id.exercise_history_time);
            note = itemView.findViewById(R.id.exercise_history_note);
            setsRecyclerView = itemView.findViewById(R.id.exercise_history_sets_recycler_view);
            noteLayout = itemView.findViewById(R.id.note_layout_true);

            //cardView = itemView.findViewById(R.id.history_item_card_view);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(exerciseHistoryItems.get(position), position);
                    }
                    return true;
                }
            });
        }
    }

    public interface OnHistoryItemLongClickListener{
        void OnItemClick(ExerciseHistoryItem exerciseHistoryItem, int position);
    }

    public void setOnHistoryItemLongClickListener(OnHistoryItemLongClickListener listener){
        this.listener = listener;
    }
}