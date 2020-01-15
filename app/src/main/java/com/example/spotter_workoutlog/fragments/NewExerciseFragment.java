package com.example.spotter_workoutlog.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.adapters.SetAdapter;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.database.models.WorkoutSession;
import com.example.spotter_workoutlog.dialogs.SetDialog;
import com.example.spotter_workoutlog.viewmodels.WorkoutViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.joda.time.DateTimeComparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewExerciseFragment extends Fragment {
    private static final String TAG = "MyActivity";
    private static final String EXERCISE_ID = "exercise_id";
    private int currentExerciseId;
    private int currentWorkoutId;
    private int currentSessionExerciseId = 27;
    private WorkoutViewModel workoutViewModel;
    private List<Set> sets = new ArrayList<>();
    private int order = 0;
    //private int setId = 0;
    private int setOrder = 0;
    private SetAdapter setsAdapter;
    private List<WorkoutSession> sessions;//samo provjera u observe
    private Handler mHandler;
    private CardView setsCardView;
    private TextInputEditText note;
    private CardView noteCardView;
    private Boolean show_note = true;
    private Boolean show_note_on_rotation = false;

    public static NewExerciseFragment newInstance(int exercise_id) {
        NewExerciseFragment fragment = new NewExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXERCISE_ID, exercise_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            currentExerciseId = getArguments().getInt(EXERCISE_ID);
        }

        if(savedInstanceState != null){
            sets = (List<Set>)savedInstanceState.getSerializable("sets");

            show_note_on_rotation = savedInstanceState.getBoolean("show_note_on_rotation");

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("sets", (Serializable) sets);

        outState.putBoolean("show_note_on_rotation", show_note_on_rotation);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextInputEditText reps = view.findViewById(R.id.exercise_input_reps);
        FloatingActionButton reps_sub_button = view.findViewById(R.id.rep_sub);
        FloatingActionButton reps_add_button = view.findViewById(R.id.rep_add);
        final TextInputEditText weight = view.findViewById(R.id.exercise_input_weight);
        FloatingActionButton weight_sub_button = view.findViewById(R.id.weight_sub);
        FloatingActionButton weight_add_button = view.findViewById(R.id.weight_add);
        FloatingActionButton add_set_button = view.findViewById(R.id.add_set);

        note = view.findViewById(R.id.exercise_input_note);
        noteCardView = view.findViewById(R.id.note_card_view);
        ShowNoteOnRotation();

        setsCardView = view.findViewById(R.id.sets_card_view);
        SetVisibility();

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getAllWorkoutSessions().observe(this, new Observer<List<WorkoutSession>>() {
            @Override
            public void onChanged(List<WorkoutSession> workoutSessions) {
                sessions = workoutSessions;
                Log.d(TAG, "onChanged: " + sessions.size());
            }
        });
        workoutViewModel.getAllSets(currentSessionExerciseId).observe(this, new Observer<List<Set>>() {
            @Override
            public void onChanged(List<Set> sets) {
                for (Set set:sets) {
                    Log.d(TAG, "onChanged: sets" + set.getId() +","+ set.getSession_exercise_id()+","+set.getReps()+","+set.getWeight()+","+set.getOrder());
                }

            }
        });

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Toast.makeText(getActivity(), "Podaci su spremljeni!", Toast.LENGTH_SHORT).show();
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.performed_sets_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        setsAdapter = new SetAdapter();
        recyclerView.setAdapter(setsAdapter);
        setsAdapter.setSets(sets);

        setsAdapter.setOnSetClickListener(new SetAdapter.OnSetClickListener() {
            @Override
            public void OnEditClick(Set set) {
                //setId = set.getId();
                setOrder = set.getOrder();
                SetDialog setDialog = new SetDialog();

                Bundle args = new Bundle();
                args.putInt("reps", set.getReps());
                args.putFloat("weight", set.getWeight());
                setDialog.setArguments(args);

                setDialog.setCancelable(false);
                setDialog.show(getFragmentManager(), "set_dialog");

                setDialog.setClickListener(new SetDialog.SetDialogListener() {
                    @Override
                    public void editSet(int reps, float weight) {
                        for (Set set : sets) {
                            if(setOrder == set.getOrder()){//if(setId == set.getId()){
                                set.setReps(reps);
                                set.setWeight(weight);
                                setsAdapter.setSets(sets);
                            }
                        }
                    }
                });
            }

            @Override
            public void OnDeleteClick(Set set) {
                //setId = set.getId();
                setOrder = set.getOrder();

                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.set_dialog_delete_title))
                        .setMessage(getString(R.string.set_dialog_delete_text))
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
                                Log.d(TAG, "onClick: " + setOrder);//Log.d(TAG, "onClick: " + setId);
                                sets.remove(setOrder-1);//sets.remove(setId-1);

                                int list_order = 0;
                                for (Set set : sets) {
                                    list_order++;
                                    set.setOrder(list_order);
                                }
                                order = sets.size();
                                setsAdapter.setSets(sets);
                                SetVisibility();
                            }
                        }).show();
            }
        });


        reps_sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepsSubtract(reps);
            }
        });

        reps_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepsAdd(reps);
            }
        });

        weight_sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeightSubtract(weight);

            }
        });

        weight_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeightAdd(weight);
            }
        });

        add_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(reps.getText())){
                    if(!TextUtils.isEmpty(weight.getText())){
                        int set_reps = Integer.parseInt(reps.getText().toString().trim());
                        float set_weight = Float.parseFloat(weight.getText().toString().trim());
                        order = order + 1;
                        Set newSet = new Set(-1,set_reps,set_weight,order);
                        //newSet.setId(order);
                        sets.add(newSet);
                        setsAdapter.setSets(sets);
                        SetVisibility();
                    }
                    else{
                        weight.setError(getString(R.string.edit_set_weight_empty));
                    }
                }
                else{
                    reps.setError(getString(R.string.edit_set_reps_empty));
                }

            }
        });
    }

    private void SetVisibility(){
        if(sets.size() == 0){
            setsCardView.setVisibility(View.GONE);
        }
        else{
            setsCardView.setVisibility(View.VISIBLE);
        }
    }

    private void ShowNote(){
        if(show_note){
            noteCardView.setVisibility(View.VISIBLE);
            show_note = false;
            show_note_on_rotation = true;
        }
        else{
            noteCardView.setVisibility(View.GONE);
            show_note = true;
            show_note_on_rotation = false;
        }
    }

    private void ShowNoteOnRotation(){
        if(show_note_on_rotation){
            noteCardView.setVisibility(View.VISIBLE);
            show_note = false;
        }
        else{
            noteCardView.setVisibility(View.GONE);
            show_note = true;
        }
    }

    private void RepsSubtract(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            int quantity = Integer.parseInt(textInputEditText.getText().toString().trim());
            quantity = quantity - 1;
            if(quantity < 0){
                textInputEditText.setText(String.valueOf(0));
            }
            else{
                textInputEditText.setText(String.valueOf(quantity));
            }
        }
        else{
            textInputEditText.setText(String.valueOf(0));
        }
    }

    private void RepsAdd(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            int quantity = Integer.parseInt(textInputEditText.getText().toString().trim());
            quantity = quantity + 1;
            textInputEditText.setText(String.valueOf(quantity));
        }
        else{
            textInputEditText.setText(String.valueOf(1));
        }
    }

    private void WeightSubtract(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            float quantity = Float.parseFloat(textInputEditText.getText().toString().trim());
            quantity = quantity - 1;
            if(quantity < 0){
                textInputEditText.setText(String.valueOf(0.0));
            }
            else{
                textInputEditText.setText(String.valueOf(quantity));
            }
        }
        else{
            textInputEditText.setText(String.valueOf(0.0));
        }
    }

    private void WeightAdd(TextInputEditText textInputEditText){
        if(!TextUtils.isEmpty(textInputEditText.getText())){
            float quantity = Float.parseFloat(textInputEditText.getText().toString().trim());
            quantity = quantity + 1;
            textInputEditText.setText(String.valueOf(quantity));
        }
        else{
            textInputEditText.setText(String.valueOf(1.0));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sets_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_sets){
            SaveSets();
            return true;
        }
        else if(item.getItemId() == R.id.add_note){
            ShowNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveSets(){
        if(sets.size() != 0){

            workoutViewModel.setOnFinishVMListener(new WorkoutViewModel.OnTaskFinishVM() {

                @Override
                public void getLastWorkoutSession(WorkoutSession workoutSession) {
                    //Log.d(TAG, "lastDate: lastdate" + android.text.format.DateFormat.format("yyyy-MM-dd", lastDate));
                    Date currentdate = new Date();
                    if(workoutSession != null){
                        currentWorkoutId = workoutSession.getId();


                        Log.d(TAG, "onOptionsItemSelected: " + currentdate);

                        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();

                        int compare = dateTimeComparator.compare(currentdate, workoutSession.getDate()/*lastDate*/);
                        if(compare == 0){
                            Log.d(TAG, "lastDate: same");

                            workoutViewModel.getMaxOrderOfSessionExercise(workoutSession.getId());

                        }
                        else{
                            Log.d(TAG, "lastDate: not same");
                            WorkoutSession newWorkoutSession = new WorkoutSession(currentdate);
                            workoutViewModel.insertWorkoutSession(newWorkoutSession);
                        }
                    }
                    else{
                        WorkoutSession newWorkoutSession = new WorkoutSession(currentdate);
                        workoutViewModel.insertWorkoutSession(newWorkoutSession);
                    }
                }

                @Override
                public void getMaxOrderSessionExercise(int maxOrder) {
                    Log.d(TAG, "getMaxOrderSessionExercise: " + maxOrder);

                    SessionExercise sessionExercise = new SessionExercise(currentWorkoutId,currentExerciseId,maxOrder+1,new Date(),note.getText().toString());
                    workoutViewModel.insertSessionExercise(sessionExercise);
                }

                @Override
                public void lastWorkoutId(Long workoutId) {
                    Log.d(TAG, "lastWorkoutId: last hope " + workoutId);

                    SessionExercise sessionExercise = new SessionExercise(workoutId.intValue(),currentExerciseId,1,new Date(),note.getText().toString());
                    workoutViewModel.insertSessionExercise(sessionExercise);
                }

                @Override
                public void lastSessionExerciseId(Long sessionExerciseId) {
                    Log.d(TAG, "lastSessionExerciseId: " + sessionExerciseId);
                    currentSessionExerciseId = sessionExerciseId.intValue();
                    //workoutViewModel.getMaxOrderSets(sessionExerciseId.intValue());
                    Log.d(TAG, "lastSessionExerciseId: currentSessionExerciseId" + currentSessionExerciseId);
                    for (Set set : sets) {
                        set.setSession_exercise_id(currentSessionExerciseId);
                    }
                    workoutViewModel.insertAllSets(sets);

                    Message message = mHandler.obtainMessage();
                    message.sendToTarget();

                    getActivity().finish();
                }
/*
                @Override
                public void getMaxOrderSets(int maxOrder) {
                    Log.d(TAG, "getMaxOrderSets: " + maxOrder);
                    if(maxOrder != 0){
                        //order sa 1
                    }
                    else{
                        //Set set = new Set(currentSessionExerciseId,); order sa 1 pocinje
                    }
                }
*/
            });

            workoutViewModel.getLastWorkoutSession();
        }
        else{
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.save_sets_dialog_title))
                    .setMessage(getString(R.string.save_sets_dialog_text))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.save_sets_dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

    }

    public void myOnKeyDown(int keyCode){
        if (keyCode == KeyEvent.KEYCODE_BACK){

            if(sets.size() != 0){
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.back_button_dialog_title))
                        .setMessage(getString(R.string.back_button_dialog_text))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SaveSets();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_discard), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .setNeutralButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
            else{
                getActivity().finish();
            }
        }
    }
}