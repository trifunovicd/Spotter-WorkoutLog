package com.example.spotter_workoutlog.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.res.ResourcesCompat;

import com.example.spotter_workoutlog.R;
import com.example.spotter_workoutlog.utilities.CustomMarkerView;
import com.example.spotter_workoutlog.utilities.DateAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.DateTime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphDialog extends AppCompatDialogFragment{
    private static final String TAG = "MyActivity";
    private LineChart dialog_chart;
    private List<Entry> entries = new ArrayList<>();
    private List<Entry> new_entries = new ArrayList<>();
    private long reference_timestamp;
    private ImageButton hide_full;
    private String lineDataName;
    private TextView graphTitle;
    private Boolean only_number;
    private ImageButton popup;
    private PopupMenu popupMenu;
    private int height;
    private DisplayMetrics metrics = new DisplayMetrics();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(getArguments() != null){
            entries = (List<Entry>) getArguments().getSerializable("entries");
            reference_timestamp = getArguments().getLong("reference_timestamp");
            lineDataName = getArguments().getString("line_data_name");
            only_number = getArguments().getBoolean("only_number");
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        display.getMetrics(metrics);

        int rotation = display.getRotation();

        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            height = size.y;
            Log.d(TAG, "onViewCreated: HORIZONTAL");
        }
        else{
            height = size.x;
        }

        if(rotation == Surface.ROTATION_270){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
        else{
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.graph_dialog_layout, null);

        graphTitle = view.findViewById(R.id.dialog_graph_title);
        graphTitle.setText(lineDataName);

        hide_full = view.findViewById(R.id.hide_full);
        hide_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        popup = view.findViewById(R.id.month_select);
        popupMenu = new PopupMenu(getActivity(), popup);
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.popup_1:
                        ChangeDateView(1);
                        return true;
                    case R.id.popup_2:
                        ChangeDateView(3);
                        return true;
                    case R.id.popup_3:
                        ChangeDateView(6);
                        return true;
                    case R.id.popup_4:
                        ChangeDateView(12);
                        return true;
                    case R.id.popup_5:
                        SetData(entries);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        dialog_chart = view.findViewById(R.id.dialog_chart);

        GenerateGraph();

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    private void GenerateGraph(){
        ValueFormatter xAxisFormatter = new DateAxisValueFormatter(reference_timestamp);
        XAxis xAxis = dialog_chart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        dialog_chart.getDescription().setEnabled(false);
        dialog_chart.getLegend().setEnabled(false);

        int dpi = metrics.densityDpi;

        if(dpi > 480){
            dialog_chart.setMinimumHeight(height - 260);
        }
        else if(dpi > 320){
            dialog_chart.setMinimumHeight(height - 220);
        }
        else if(dpi > 240){
            dialog_chart.setMinimumHeight(height - 150);
        }
        else if(dpi > 160){
            dialog_chart.setMinimumHeight(height - 100);
        }
        else if(dpi > 120){
            dialog_chart.setMinimumHeight(height - 65);
        }
        else{
            dialog_chart.setMinimumHeight(height - 50);
        }

        CustomMarkerView markerView = new CustomMarkerView (getActivity(), R.layout.marker_view_layout, reference_timestamp, only_number);
        markerView.setChartView(dialog_chart);
        dialog_chart.setMarker(markerView);

        SetData(entries);
    }

    private void ChangeDateView(int months){
        new_entries.clear();
        Date date = new DateTime().minusMonths(months).toDate();

        for (Entry entry : entries) {
            float entry_date = entry.getX() + reference_timestamp;

            if(entry_date >= date.getTime()){
                new_entries.add(entry);
            }
        }
        SetData(new_entries);
    }

    private void SetData(List<Entry> entriesList){
        LineDataSet dataSet = new LineDataSet(entriesList, lineDataName);
        dataSet.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        dataSet.setCircleColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(1.5f);

        LineData lineData = new LineData(dataSet);

        dialog_chart.clear();
        dialog_chart.setData(lineData);
        dialog_chart.invalidate();
    }
}
