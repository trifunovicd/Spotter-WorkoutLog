package com.example.spotter_workoutlog.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import androidx.room.Query;

import com.example.spotter_workoutlog.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomMarkerView extends MarkerView {
    private TextView number, date;
    private MPPointF mOffset;
    private long reference_timestamp;
    private Boolean only_number;

    public CustomMarkerView(Context context, int layoutResource, long timestamp, Boolean only_number) {
        super(context, layoutResource);
        reference_timestamp = timestamp;
        number = findViewById(R.id.number);
        date = findViewById(R.id.date);
        this.only_number = only_number;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if(only_number){
            number.setText(Integer.toString((int)e.getY()));
        }
        else{
            float result = e.getY() - (int)e.getY();
            if (result != 0){
                number.setText(getResources().getString(R.string.number, Float.toString(e.getY())));
            }
            else{
                number.setText(getResources().getString(R.string.number, Integer.toString((int)e.getY())));
            }
        }

        long mili = (long) e.getX();
        mili = mili + reference_timestamp;
        DateFormat dateFormat = new SimpleDateFormat("d MMM. yyyy", Locale.getDefault());

        date.setText(dateFormat.format(mili));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(int)((double)getWidth() / (double)2), -getHeight());
        }
        return mOffset;
    }
}
