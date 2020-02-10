package com.example.spotter_workoutlog.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.CalendarAdapter;
import com.example.spotter_workoutlog.adapters.ExerciseHistoryAdapter;
import com.example.spotter_workoutlog.database.models.ExerciseHistoryItem;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.database.models.WorkoutSession;
import com.example.spotter_workoutlog.database.models.WorkoutStats;
import com.example.spotter_workoutlog.viewmodels.ExerciseViewModel;
import com.example.spotter_workoutlog.viewmodels.WorkoutViewModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private AppBarLayout appBarLayout;
    private CompactCalendarView compactCalendarView;
    private TextView datePickerTextView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    private boolean isExpanded = false;
    private ImageView arrow;
    private LinearLayout datePickerButton;
    private boolean closed = true;
    private boolean opened = true;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private List<WorkoutSession> workoutSessionsList = new ArrayList<>();
    private List<SessionExercise> sessionExerciseList = new ArrayList<>();
    private List<List<Set>> setsForSession = new ArrayList<>();
    private List<ExerciseHistoryItem> exerciseHistoryItems = new ArrayList<>();
    private List<String> exerciseNamesList = new ArrayList<>();
    private CalendarAdapter calendarAdapter;
    private ActionMode actionMode;
    private int workoutId;
    private LiveData<List<SessionExercise>> observable;
    private Observer observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar_layout);
        datePickerTextView = findViewById(R.id.date_picker_text_view);
        arrow = findViewById(R.id.date_picker_arrow);

        compactCalendarView = findViewById(R.id.compact_calendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                observable.removeObserver(observer);
                FetchData(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        datePickerButton = findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

               if(i >= (-compactCalendarView.getHeight()/2)){
                   opened = true;
                   if(closed){
                       ViewCompat.animate(arrow).rotation(180).start();
                       isExpanded = true;
                       closed = false;
                   }
               }
               else{
                   closed = true;
                   if(opened){
                       ViewCompat.animate(arrow).rotation(0).start();
                       isExpanded = false;
                       opened = false;
                   }
               }
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.calendar_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        calendarAdapter = new CalendarAdapter();
        recyclerView.setAdapter(calendarAdapter);

        calendarAdapter.setOnCalendarItemLongClickListener(new CalendarAdapter.OnCalendarItemLongClickListener() {
            @Override
            public void OnItemClick(final ExerciseHistoryItem exerciseHistoryItem, final int position, final String name) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                final CardView cardView = viewHolder.itemView.findViewById(R.id.history_item_card_view);

                if(actionMode == null){
                    actionMode = startActionMode(new ActionMode.Callback() {
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
                                    Intent intent = new Intent(CalendarActivity.this, EditSessionExerciseActivity.class);
                                    intent.putExtra("exerciseHistoryItem", exerciseHistoryItem);
                                    intent.putExtra("currentExerciseName", name);
                                    startActivity(intent);
                                    actionMode.finish();
                                    return true;
                                case R.id.delete_item:
                                    new AlertDialog.Builder(CalendarActivity.this)
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

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.setOnNameFinishVMListener(new ExerciseViewModel.OnExerciseNameFinishVM() {
            @Override
            public void getExerciseName(String name) {
                exerciseNamesList.add(name);
                if(exerciseNamesList.size() == sessionExerciseList.size()){
                    exerciseNamesList.add(getString(R.string.deleted_exercise_name));
                    calendarAdapter.setExerciseNames(exerciseNamesList);
                    workoutViewModel.getWorkoutStats(workoutId);
                }
            }
        });

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getAllWorkoutSessions().observe(this, new Observer<List<WorkoutSession>>() {
            @Override
            public void onChanged(List<WorkoutSession> workoutSessions) {
                workoutSessionsList = workoutSessions;
                Date currentDate = workoutSessionsList.get(workoutSessionsList.size() - 1).getDate();
                compactCalendarView.setCurrentDate(currentDate);
                setTitle(dateFormat.format(currentDate));
                compactCalendarView.removeAllEvents();
                MarkEvents();
                FetchData(currentDate);
            }
        });

        workoutViewModel.setOnFinishSetsVMListener(new WorkoutViewModel.OnTaskFinishSetsVM() {
            @Override
            public void getAllSetsForSession(List<Set> sets) {
                setsForSession.add(sets);
                if(sessionExerciseList.size() == setsForSession.size()){
                    SetExerciseHistory();
                }
            }

            @Override
            public void getSessionsCount(int count) {

            }

            @Override
            public void getWorkoutStats(WorkoutStats workoutStats) {
                calendarAdapter.setWorkoutStats(workoutStats);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        calendarAdapter.setExerciseHistoryItems(exerciseHistoryItems);
                    }
                });
            }
        });
    }

    private void MarkEvents(){
        for (WorkoutSession workoutSession : workoutSessionsList) {
            Event event = new Event(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null), workoutSession.getDate().getTime(), workoutSession.getId());
            compactCalendarView.addEvent(event);
        }
    }

    private void setTitle(String title) {
        datePickerTextView.setText(title);
    }

    @Override
    public void onBackPressed() {
        if(isExpanded){
            appBarLayout.setExpanded(false, true);
        }
        else{
            super.onBackPressed();
        }
    }

    private void FetchData(final Date dateClicked){
        List<Event> events = compactCalendarView.getEvents(dateClicked);
        if(!events.isEmpty()){
            setTitle(dateFormat.format(dateClicked));
            appBarLayout.setExpanded(false, true);
            workoutId = Integer.valueOf(events.get(0).getData().toString());

            observer = new Observer<List<SessionExercise>>() {
                @Override
                public void onChanged(List<SessionExercise> sessionExercises) {
                    sessionExerciseList = sessionExercises;
                    setsForSession.clear();
                    exerciseHistoryItems.clear();
                    exerciseNamesList.clear();
                    if(sessionExerciseList.size() == 0){
                        calendarAdapter.setExerciseHistoryItems(exerciseHistoryItems);
                        WorkoutSession workoutSession = new WorkoutSession(dateClicked);
                        workoutSession.setId(workoutId);
                        workoutViewModel.deleteWorkoutSession(workoutSession);
                    }
                    else{
                        GetSets();
                    }
                }
            };

            observable = workoutViewModel.getAllSessionExercises(workoutId);
            observable.observe(CalendarActivity.this, observer);
        }
        else{
            Toast.makeText(CalendarActivity.this, getString(R.string.calendar_no_data), Toast.LENGTH_SHORT).show();
        }
    }

    private void GetSets(){
        for (SessionExercise sessionExercise : sessionExerciseList) {
            workoutViewModel.getAllSetsForSession(sessionExercise.getId());
        }
    }

    private void SetExerciseHistory(){
        int counter = 0;
        for (SessionExercise sessionExercise : sessionExerciseList) {
            ExerciseHistoryItem exerciseHistoryItem = new ExerciseHistoryItem(sessionExercise.getId(),sessionExercise.getWorkout_session_id(),sessionExercise.getExercise_id(),sessionExercise.getOrder(),sessionExercise.getDate(),sessionExercise.getNote(),setsForSession.get(counter));
            exerciseHistoryItems.add(exerciseHistoryItem);
            exerciseViewModel.getExerciseName(sessionExercise.getExercise_id());
            counter++;
        }
    }
}
