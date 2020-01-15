package com.example.spotter_workoutlog.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import androidx.viewpager.widget.ViewPager;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.activities.EditSessionExerciseActivity;
import com.example.spotter_workoutlog.adapters.ExerciseHistoryAdapter;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.dialogs.HistoryItemDialog;
import com.example.spotter_workoutlog.viewmodels.WorkoutViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HistoryExerciseFragment extends Fragment {
    private static final String TAG = "MyActivity";
    private static final String EXERCISE_ID = "exercise_id";
    private static final String EXERCISE_NAME = "exercise_name";
    private int currentExerciseId;
    private String currentExerciseName;
    private WorkoutViewModel workoutViewModel;
    private List<ExerciseHistoryItem> exerciseHistoryItems = new ArrayList<>();
    private List<SessionExercise> sessionExercisesList;
    private ExerciseHistoryAdapter exerciseHistoryAdapter;
    private List<List<Set>> setsForSession = new ArrayList<>();

    public static ActionMode actionMode;

    public static HistoryExerciseFragment newInstance(int exercise_id, String exercise_name) {
        HistoryExerciseFragment fragment = new HistoryExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXERCISE_ID, exercise_id);
        bundle.putString(EXERCISE_NAME, exercise_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentExerciseId = getArguments().getInt(EXERCISE_ID);
            currentExerciseName = getArguments().getString(EXERCISE_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.exercise_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        exerciseHistoryAdapter = new ExerciseHistoryAdapter();
        recyclerView.setAdapter(exerciseHistoryAdapter);

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getAllSessionExercisesForExercise(currentExerciseId).observe(this, new Observer<List<SessionExercise>>() {
            @Override
            public void onChanged(List<SessionExercise> sessionExercises) {
                Log.d(TAG, "onChanged: obrisan zapis");
                sessionExercisesList = sessionExercises;
                setsForSession.clear();
                exerciseHistoryItems.clear();
                Log.d(TAG, "onChanged: number of session exercises " + sessionExercisesList.size());
                if(sessionExercisesList.size() == 0){
                    exerciseHistoryAdapter.setExerciseHistoryItems(exerciseHistoryItems);
                }
                else{
                    GetSets();
                }
            }
        });

        exerciseHistoryAdapter.setOnHistoryItemLongClickListener(new ExerciseHistoryAdapter.OnHistoryItemLongClickListener() {
            @Override
            public void OnItemClick(final ExerciseHistoryItem exerciseHistoryItem, int position) {
                /*HistoryItemDialog historyItemDialog = new HistoryItemDialog(exerciseHistoryItem, currentExerciseName);
                historyItemDialog.show(getActivity().getSupportFragmentManager(), "history_item_dialog");

                historyItemDialog.setHistoryItemDialogListener(new HistoryItemDialog.HistoryItemDialogListener() {
                    @Override
                    public void DeleteHistoryItem(SessionExercise sessionExercise) {
                        workoutViewModel.deleteSessionExercise(sessionExercise);
                    }
                });*/
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                final CardView cardView = viewHolder.itemView.findViewById(R.id.history_item_card_view);

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
                                    Intent intent = new Intent(getActivity(), EditSessionExerciseActivity.class);
                                    intent.putExtra("exerciseHistoryItem", exerciseHistoryItem);
                                    intent.putExtra("currentExerciseName", currentExerciseName);
                                    startActivity(intent);
                                    actionMode.finish();
                                    return true;
                                case R.id.delete_item:
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle(getString(R.string.history_item_delete_title))
                                            .setMessage(getString(R.string.history_item_delete_text))
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
                                                    SessionExercise sessionExercise = new SessionExercise(exerciseHistoryItem.getWorkout_session_id(),exerciseHistoryItem.getExercise_id(),exerciseHistoryItem.getOrder(),exerciseHistoryItem.getDate(),exerciseHistoryItem.getNote());
                                                    sessionExercise.setId(exerciseHistoryItem.getId());
                                                    workoutViewModel.deleteSessionExercise(sessionExercise);
                                                    dialog.dismiss();
                                                    actionMode.finish();
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

    private void GetSets(){
        Log.d(TAG, "GetSets: uspio");
        workoutViewModel.setOnFinishSetsVMListener(new WorkoutViewModel.OnTaskFinishSetsVM() {
            @Override
            public void getAllSetsForSession(List<Set> sets) {
                Log.d(TAG, "getAllSetsForSession: uspio opet");
                setsForSession.add(sets);
                SetExerciseHistory();
            }
        });
        for (SessionExercise sessionExercise : sessionExercisesList) {
            workoutViewModel.getAllSetsForSession(sessionExercise.getId());
        }
    }

    private void SetExerciseHistory(){
        Log.d(TAG, "SetExerciseHistory: uspio treci");
        Log.d(TAG, "SetExerciseHistory: sessionExercisesList: " + sessionExercisesList.size() + ", setsForSession: " + setsForSession.size());
        if(sessionExercisesList.size() == setsForSession.size()){
            Log.d(TAG, "SetExerciseHistory: isti su" );
            int counter = 0;
            for (SessionExercise sessionExercise : sessionExercisesList) {
                ExerciseHistoryItem exerciseHistoryItem = new ExerciseHistoryItem(sessionExercise.getId(),sessionExercise.getWorkout_session_id(),sessionExercise.getExercise_id(),sessionExercise.getOrder(),sessionExercise.getDate(),sessionExercise.getNote(),setsForSession.get(counter));
                exerciseHistoryItems.add(exerciseHistoryItem);
                counter++;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    exerciseHistoryAdapter.setExerciseHistoryItems(exerciseHistoryItems);
                }
            });

        }
    }
}
