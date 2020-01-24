package com.example.spotter_workoutlog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener onCategoryClickListener;
    private OnCategoryLongClickListener onCategoryLongClickListener;

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_exercise_layout, parent, false);
        CategoryHolder categoryHolder = new CategoryHolder(view);
        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.categoryName.setText(currentCategory.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{
        private TextView categoryName;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_exercise_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(onCategoryClickListener != null && position != RecyclerView.NO_POSITION) {
                        onCategoryClickListener.OnItemClick(categories.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(onCategoryLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onCategoryLongClickListener.OnItemClick(categories.get(position), position);
                    }
                    return true;
                }
            });

        }
    }

    public interface OnCategoryClickListener{
        void OnItemClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener){
        this.onCategoryClickListener = listener;
    }

    public interface OnCategoryLongClickListener{
        void OnItemClick(Category category, int position);
    }

    public void setOnCategoryLongClickListener(OnCategoryLongClickListener listener){
        this.onCategoryLongClickListener = listener;
    }
}
