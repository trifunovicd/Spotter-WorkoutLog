package com.example.spotter_workoutlog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.spotter_workoutlog.adapters.CategoryAdapter;
import com.example.spotter_workoutlog.dialogs.CategoryDialog;
import com.example.spotter_workoutlog.utilities.Utility;
import com.example.spotter_workoutlog.viewmodels.CategoryViewModel;
import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryDialog.CategoryDialogListener {
    private static final String TAG = "MyActivity";
    private CategoryViewModel categoryViewModel;
    private FloatingActionButton addCategoryButton;
    private Handler addHandler, editHandler;
    private int mCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.categories_activity));

        addHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(MainActivity.this, getString(R.string.category_add_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Kategorija već postoji!", Toast.LENGTH_SHORT).show();
                }

            }
        };

        editHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(mCheck == 0){
                    Toast.makeText(MainActivity.this, getString(R.string.category_edit_success), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Kategorija već postoji!", Toast.LENGTH_SHORT).show();
                }

            }
        };


        addCategoryButton = findViewById(R.id.add_category);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryDialog();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.categories_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Category category = categoryAdapter.getCategoryAtPosition(viewHolder.getAdapterPosition());
                final int currentPosition = viewHolder.getAdapterPosition();

                switch (direction){
                    case ItemTouchHelper.LEFT:
                        new AlertDialog.Builder(MainActivity.this)
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

                        categoryAdapter.notifyItemChanged(currentPosition);

                        break;
                    case ItemTouchHelper.RIGHT:

                        editCategoryDialog(category.getId(), category.getName());
                        categoryAdapter.notifyItemChanged(currentPosition);

                        break;
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Utility utility = new Utility();
                utility.drawCardBackground(c, viewHolder, dX, actionState, MainActivity.this);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        }).attachToRecyclerView(recyclerView);

        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void OnItemClick(Category category) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("category_id", category.getId());
                intent.putExtra("category_name", category.getName());
                startActivity(intent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public void addCategoryDialog(){
        CategoryDialog categoryDialog = new CategoryDialog();
        categoryDialog.setCancelable(false);
        categoryDialog.show(getSupportFragmentManager(), "category_dialog");
    }

    public void editCategoryDialog(int category_id, String name){
        CategoryDialog categoryDialog = new CategoryDialog();

        Bundle args = new Bundle();
        args.putInt("category_id", category_id);
        args.putString("name", name);
        categoryDialog.setArguments(args);

        categoryDialog.setCancelable(false);
        categoryDialog.show(getSupportFragmentManager(), "category_dialog");
    }


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
                    message.sendToTarget();
                }
                else{
                    message.sendToTarget();
                }
            }
        });
        categoryViewModel.checkIfNameExists(name);
    }

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
                    message.sendToTarget();
                }
                else{
                    message.sendToTarget();
                }
            }
        });
        categoryViewModel.checkIfNameExists(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.calendar_menu:
                Intent calendar_intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(calendar_intent);
                return true;
            case R.id.settings_menu:
                Intent settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
