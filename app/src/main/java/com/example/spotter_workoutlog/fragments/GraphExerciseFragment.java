package com.example.spotter_workoutlog.fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.database.models.GraphData;
import com.example.spotter_workoutlog.database.models.SessionExercise;
import com.example.spotter_workoutlog.database.models.Set;
import com.example.spotter_workoutlog.dialogs.GraphDialog;
import com.example.spotter_workoutlog.utilities.DateAxisValueFormatter;
import com.example.spotter_workoutlog.viewmodels.WorkoutViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphExerciseFragment extends Fragment {
    private static final String TAG = "MyActivity";
    private static final String EXERCISE_ID = "exercise_id";
    private int currentExerciseId;
    private WorkoutViewModel workoutViewModel;
    private List<SessionExercise> sessionExercisesList = new ArrayList<>();
    private List<Float> totalVolumesList = new ArrayList<>();
    private List<Float> totalRepsList = new ArrayList<>();
    private List<Float> maxWeightList = new ArrayList<>();
    private List<Float> maxRepsList = new ArrayList<>();
    private List<Float> oneRMList = new ArrayList<>();
    private List<GraphData> graphDataList = new ArrayList<>();
    private LineChart chart;
    private ImageButton showFull;
    private List<Entry> entries = new ArrayList<>();
    private List<Entry> newEntries = new ArrayList<>();
    private long reference_timestamp;
    private String lineDataName;
    private TextView graphTitle;
    private Spinner spinner;
    private boolean new_data, isSelected, only_number;

    public static GraphExerciseFragment newInstance(int exercise_id) {
        GraphExerciseFragment fragment = new GraphExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXERCISE_ID, exercise_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentExerciseId = getArguments().getInt(EXERCISE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = view.findViewById(R.id.chart);
        chart.setNoDataText(getString(R.string.chart_text));

        graphTitle = view.findViewById(R.id.graph_title);
        spinner = view.findViewById(R.id.graph_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.graph_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner.getSelectedItemPosition()){
                    case 0:
                        if(isSelected){
                            GetTotalVolume();
                            graphTitle.setText(spinner.getSelectedItem().toString());
                            lineDataName = spinner.getSelectedItem().toString();
                        }
                        isSelected = true;
                        break;
                    case 1:
                        GetTotalReps();
                        graphTitle.setText(spinner.getSelectedItem().toString());
                        lineDataName = spinner.getSelectedItem().toString();
                        break;
                    case 2:
                        GetMaxWeight();
                        graphTitle.setText(spinner.getSelectedItem().toString());
                        lineDataName = spinner.getSelectedItem().toString();
                        break;
                    case 3:
                        GetMaxReps();
                        graphTitle.setText(spinner.getSelectedItem().toString());
                        lineDataName = spinner.getSelectedItem().toString();
                        break;
                    case 4:
                        GetSetWithMaxValues();
                        graphTitle.setText(spinner.getSelectedItem().toString());
                        lineDataName = spinner.getSelectedItem().toString();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showFull = view.findViewById(R.id.show_full);
        showFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphDialog graphDialog = new GraphDialog();
                Bundle args = new Bundle();
                if(new_data){
                    args.putSerializable("entries", (Serializable) newEntries);
                }
                else{
                    args.putSerializable("entries", (Serializable) entries);
                }
                args.putLong("reference_timestamp", reference_timestamp);
                args.putString("line_data_name", lineDataName);
                args.putBoolean("only_number", only_number);
                graphDialog.setArguments(args);
                graphDialog.show(getActivity().getSupportFragmentManager(), "graph_dialog");
            }
        });

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        workoutViewModel.setOnSetsVolumeVMFinishListener(new WorkoutViewModel.OnTaskFinishSetsVolumeVM() {
            @Override
            public void getTotalVolume(Float totalVolume) {
                totalVolumesList.add(totalVolume);
                Log.d(TAG, "getTotalVolume: " + totalVolume);
                if(sessionExercisesList.size() == totalVolumesList.size()){
                    MakeData();
                }
            }

            @Override
            public void getTotalReps(Float totalReps) {
                totalRepsList.add(totalReps);
                Log.d(TAG, "getTotalReps: " + totalReps);
                if(sessionExercisesList.size() == totalRepsList.size()) {
                    MakeNewData(totalRepsList);
                }
            }

            @Override
            public void getMaxWeight(Float maxWeight) {
                maxWeightList.add(maxWeight);
                if(sessionExercisesList.size() == maxWeightList.size()) {
                    MakeNewData(maxWeightList);
                }
            }

            @Override
            public void getMaxReps(Float maxReps) {
                maxRepsList.add(maxReps);
                if(sessionExercisesList.size() == maxRepsList.size()) {
                    MakeNewData(maxRepsList);
                }
            }

            @Override
            public void getSetWithMaxValues(Set set) {
                float oneRM = set.getWeight() * (float)(1 + ((double)set.getReps()/(double)30));

                BigDecimal bd = new BigDecimal(oneRM);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                Log.d(TAG, "getSetWithMaxValues: " + bd.floatValue());

                oneRMList.add(bd.floatValue());
                if(sessionExercisesList.size() == oneRMList.size()) {
                    MakeNewData(oneRMList);
                }
            }
        });

        workoutViewModel.getAllSessionExercisesForGraph(currentExerciseId).observe(this, new Observer<List<SessionExercise>>() {
            @Override
            public void onChanged(List<SessionExercise> sessionExercises) {
                sessionExercisesList = sessionExercises;
                totalVolumesList.clear();
                totalRepsList.clear();
                maxWeightList.clear();
                maxRepsList.clear();
                oneRMList.clear();
                spinner.setSelection(0, false);
                graphTitle.setText(getString(R.string.total_volume));
                lineDataName = getString(R.string.total_volume);
                
                if(sessionExercisesList.isEmpty()){
                    showFull.setVisibility(View.GONE);
                }
                else{
                    showFull.setVisibility(View.VISIBLE);
                    GetTotalVolume();
                }
            }
        });
    }

    private void GetTotalVolume(){
        only_number = false;
        new_data = false;

        if(totalVolumesList.isEmpty()){
            if(!sessionExercisesList.isEmpty()){
                for (SessionExercise sessionExercise : sessionExercisesList) {
                    workoutViewModel.getTotalVolume(sessionExercise.getId());
                }
            }
        }
        else{
            ChangeDateView(entries);
        }
    }

    private void GetTotalReps(){
        only_number = true;
        if(totalRepsList.isEmpty()){
            if(!sessionExercisesList.isEmpty()){
                for (SessionExercise sessionExercise : sessionExercisesList) {
                    workoutViewModel.getTotalReps(sessionExercise.getId());
                }
            }
        }
        else{
            MakeNewData(totalRepsList);
        }
    }

    private void GetMaxWeight(){
        only_number = false;
        if(maxWeightList.isEmpty()){
            if(!sessionExercisesList.isEmpty()){
                for (SessionExercise sessionExercise : sessionExercisesList) {
                    workoutViewModel.getMaxWeight(sessionExercise.getId());
                }
            }
        }
        else{
            MakeNewData(maxWeightList);
        }
    }

    private void GetMaxReps(){
        only_number = true;
        if(maxRepsList.isEmpty()){
            if(!sessionExercisesList.isEmpty()){
                for (SessionExercise sessionExercise : sessionExercisesList) {
                    workoutViewModel.getMaxReps(sessionExercise.getId());
                }
            }
        }
        else{
            MakeNewData(maxRepsList);
        }
    }

    private void GetSetWithMaxValues(){
        only_number = false;
        if(oneRMList.isEmpty()){
            if(!sessionExercisesList.isEmpty()){
                for (SessionExercise sessionExercise : sessionExercisesList) {
                    workoutViewModel.getSetWithMaxValues(sessionExercise.getId());
                }
            }
        }
        else{
            MakeNewData(oneRMList);
        }
    }

    private void MakeNewData(List<Float> dataList){
        new_data = true;
        newEntries.clear();
        int counter = 0;
        for (Entry entry : entries) {
            newEntries.add(new Entry(entry.getX(), dataList.get(counter)));
            counter++;
        }
        ChangeDateView(newEntries);
    }

    private void MakeData(){
        graphDataList.clear();
        int counter = 0;
        for (SessionExercise sessionExercise : sessionExercisesList) {
            GraphData graphData = new GraphData(sessionExercise.getDate(), totalVolumesList.get(counter));
            graphDataList.add(graphData);
            counter++;
        }
        TransformData();
    }

    private void TransformData(){
        entries.clear();
        int counter = 0;
        for (GraphData graphData : graphDataList) {
            long mili = graphData.getDate().getTime();
            if (counter == 0){
                reference_timestamp = mili;
                counter++;
            }
            long new_mili = mili - reference_timestamp;
            float date = Float.parseFloat(String.valueOf(new_mili));

            entries.add(new Entry(date, graphData.getNumber()));
        }
        GenerateGraph();
    }

    private void GenerateGraph(){
        ValueFormatter xAxisFormatter = new DateAxisValueFormatter(reference_timestamp);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);

        ChangeDateView(entries);
    }

    private void ChangeDateView(List<Entry> entriesList){
        Date date = new DateTime().minusMonths(3).toDate();
        List<Entry> monthViewEntries = new ArrayList<>();

        for (Entry entry : entriesList) {
            float entry_date = entry.getX() + reference_timestamp;

            if(entry_date >= date.getTime()){
                monthViewEntries.add(entry);
            }
        }
        SetData(monthViewEntries);
    }

    private void SetData(List<Entry> entriesList){
        LineDataSet dataSet = new LineDataSet(entriesList, lineDataName);
        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        dataSet.setCircleColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(1.5f);

        final LineData lineData = new LineData(dataSet);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chart.clear();
                chart.setData(lineData);
                chart.invalidate();
            }
        });
    }
}
