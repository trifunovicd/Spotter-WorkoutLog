package com.example.spotter_workoutlog.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.activities.CategoryActivity;
import com.example.spotter_workoutlog.adapters.CategoryAdapter;
import com.example.spotter_workoutlog.database.models.Category;
import com.example.spotter_workoutlog.dialogs.CategoryDialog;
import com.example.spotter_workoutlog.viewmodels.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CategoryFragment extends Fragment{

    private static final String TAG = "MyActivity";
    private CategoryViewModel categoryViewModel;
    private FloatingActionButton addCategoryButton;
    private Handler addHandler, editHandler;
    private int mCheck;
    public static ActionMode actionMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(getActivity(), getString(R.string.category_add_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.category_add_fail), Toast.LENGTH_SHORT).show();
                }

            }
        };

        editHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(getActivity(), getString(R.string.category_edit_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.category_add_fail), Toast.LENGTH_SHORT).show();
                }

            }
        };


        addCategoryButton = view.findViewById(R.id.add_category);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryDialog();
            }
        });

        final RecyclerView recyclerView = view.findViewById(R.id.categories_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final CategoryAdapter categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.setCategories(categories);
            }
        });

        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void OnItemClick(Category category) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("category_id", category.getId());
                intent.putExtra("category_name", category.getName());
                startActivity(intent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        categoryAdapter.setOnCategoryLongClickListener(new CategoryAdapter.OnCategoryLongClickListener() {
            @Override
            public void OnItemClick(final Category category, int position) {

                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                final CardView cardView = viewHolder.itemView.findViewById(R.id.category_exercise_card_view);

                if(actionMode == null){
                    actionMode = getActivity().startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                            mode.setTitle(getString(R.string.item_selected_title));
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.edit_item:
                                    editCategoryDialog(category.getId(), category.getName());
                                    actionMode.finish();
                                    return true;
                                case R.id.delete_item:
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle(getString(R.string.category_dialog_delete_title) + " " + category.getName())
                                            .setMessage(getString(R.string.category_dialog_delete_text))
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
                                                    Log.d(TAG, "onClick: before delete" + categoryViewModel.getAllCategories().getValue().size());
                                                    category.setDeleted(true);
                                                    categoryViewModel.updateCategory(category);
                                                    dialog.dismiss();
                                                    actionMode.finish();////
                                                    Snackbar.make(recyclerView, getString(R.string.snackbar_category_text_part1) + " " + category.getName() + " " + getString(R.string.snackbar_category_text_part2), Snackbar.LENGTH_LONG)
                                                            .setAction(getString(R.string.snackbar_cancel), new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    category.setDeleted(false);
                                                                    categoryViewModel.updateCategory(category);
                                                                }
                                                            })
                                                            .addCallback(new Snackbar.Callback(){
                                                                @Override
                                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                                    super.onDismissed(transientBottomBar, event);
                                                                    if(event == DISMISS_EVENT_TIMEOUT){
                                                                        categoryViewModel.deleteCategory(category);
                                                                        Log.d(TAG, "onDismissed: deleted" + categoryViewModel.getAllCategories().getValue().size());
                                                                    }

                                                                }
                                                            }).show();
                                                }
                                            }).show();
                                    return true;
                                default:
                                    return false;
                            }

                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            actionMode = null;
                            cardView.setCardBackgroundColor(Color.WHITE);
                        }
                    });
                    cardView.setCardBackgroundColor(Color.LTGRAY);
                }
            }
        });


    }

    private void addCategoryDialog(){
        CategoryDialog categoryDialog = new CategoryDialog();

        categoryDialog.setCategoryAddDialogListener(new CategoryDialog.CategoryAddDialogListener() {
            @Override
            public void addNewCategory(final String name) {
                categoryViewModel.setOnFinishListener(new CategoryViewModel.OnCategoryVMTaskFinish() {
                    @Override
                    public void checkIfNameExists(int check) {
                        mCheck = check;
                        Message message = addHandler.obtainMessage();

                        Log.d(TAG, "addNewCategory: check name " + check);
                        if(mCheck == 0){
                            Category category = new Category(name, false);
                            categoryViewModel.insertCategory(category);
                        }
                        message.sendToTarget();
                    }
                });
                categoryViewModel.checkIfNameExists(name);
            }
        });

        categoryDialog.setCancelable(false);
        categoryDialog.show(getActivity().getSupportFragmentManager(), "category_dialog");
    }

    private void editCategoryDialog(int category_id, String name){
        CategoryDialog categoryDialog = new CategoryDialog();

        categoryDialog.setCategoryEditDialogListener(new CategoryDialog.CategoryEditDialogListener() {
            @Override
            public void editCategory(final int category_id, final String name) {
                categoryViewModel.setOnFinishListener(new CategoryViewModel.OnCategoryVMTaskFinish() {
                    @Override
                    public void checkIfNameExists(int check) {
                        mCheck = check;
                        Message message = editHandler.obtainMessage();

                        Log.d(TAG, "addNewCategory: check name " + check);
                        if(check == 0){
                            Category category = new Category(name, false);
                            category.setId(category_id);
                            categoryViewModel.updateCategory(category);
                        }
                        message.sendToTarget();
                    }
                });
                categoryViewModel.checkIfNameExists(name);
            }
        });

        Bundle args = new Bundle();
        args.putInt("category_id", category_id);
        args.putString("name", name);
        categoryDialog.setArguments(args);

        categoryDialog.setCancelable(false);
        categoryDialog.show(getActivity().getSupportFragmentManager(), "category_dialog");
    }
}
