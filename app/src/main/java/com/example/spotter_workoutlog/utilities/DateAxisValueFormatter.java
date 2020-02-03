package com.example.spotter_workoutlog.utilities;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAxisValueFormatter extends ValueFormatter {

    private long referenceTimestamp;
    private DateFormat mDataFormat;

    public DateAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("MMM yyyy.", Locale.getDefault());
    }


    @Override
    public String getFormattedValue(float value) {
        long convertedTimestamp = (long) value;

        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        return mDataFormat.format(originalTimestamp);
    }
}
