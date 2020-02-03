package com.example.spotter_workoutlog.database.models;

import java.util.Date;

public class GraphData {

    private Date date;
    private Float number;

    public GraphData(Date date, Float number) {
        this.date = date;
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public Float getNumber() {
        return number;
    }
}
