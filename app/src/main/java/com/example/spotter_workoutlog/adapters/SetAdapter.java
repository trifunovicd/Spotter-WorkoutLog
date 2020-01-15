package com.example.spotter_workoutlog.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.Set;

import java.util.ArrayList;
import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyActivity";
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    private List<Set> sets = new ArrayList<>();
    private OnSetClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_header_layout, parent, false);
            HeaderHolder headerHolder = new HeaderHolder(view);
            return headerHolder;
        }
        else if(viewType == TYPE_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_layout, parent, false);
            SetHolder setHolder = new SetHolder(view);
            return setHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEAD){
            ((HeaderHolder) holder).header_set.setText(R.string.set);
            ((HeaderHolder) holder).header_reps.setText(R.string.reps);
            ((HeaderHolder) holder).header_weight.setText(R.string.weight);

        }
        else {
             Set currentSet = sets.get(position - 1);
             ((SetHolder) holder).set.setText(String.valueOf(currentSet.getOrder()));
             ((SetHolder) holder).rep.setText(String.valueOf(currentSet.getReps()));

             float result = currentSet.getWeight() - (int)currentSet.getWeight();
             if (result != 0){
                 ((SetHolder) holder).weight.setText(String.valueOf(currentSet.getWeight()));
             }
             else{
                 ((SetHolder) holder).weight.setText(String.valueOf((int)currentSet.getWeight()));
             }

             //((SetHolder) holder).weight.setText(String.valueOf(currentSet.getWeight()));
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

    public class SetHolder extends RecyclerView.ViewHolder{
        TextView set;
        TextView rep;
        TextView weight;
        ImageButton edit_set;
        ImageButton delete_set;

        public SetHolder(@NonNull View itemView) {
            super(itemView);

            set = itemView.findViewById(R.id.set);
            rep = itemView.findViewById(R.id.rep);
            weight = itemView.findViewById(R.id.weight);
            edit_set = itemView.findViewById(R.id.set_edit_button);
            delete_set = itemView.findViewById(R.id.set_delete_button);

            edit_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() - 1;
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnEditClick(sets.get(position));
                    }
                }
            });

            delete_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() - 1;
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnDeleteClick(sets.get(position));
                    }
                }
            });
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder{

        TextView header_set,header_reps,header_weight;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);

            header_set = itemView.findViewById(R.id.header_set);
            header_reps = itemView.findViewById(R.id.header_rep);
            header_weight = itemView.findViewById(R.id.header_weight);

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

    public interface OnSetClickListener{
        void OnEditClick(Set set);
        void OnDeleteClick(Set set);
    }

    public void setOnSetClickListener(OnSetClickListener listener){
        this.listener = listener;
    }
}
