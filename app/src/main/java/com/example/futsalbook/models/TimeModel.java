package com.example.futsalbook.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeModel implements Serializable {
    private String start_time, end_time;
    private int time_table_id;

    public TimeModel(String start_time, String end_time, int time_table_id) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.time_table_id = time_table_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTime_table_id() {
        return time_table_id;
    }

    public void setTime_table_id(int time_table_id) {
        this.time_table_id = time_table_id;
    }

    @Override
    public String toString() {
        String _start_time = start_time;
        String _end_time = end_time;
        SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = inFormat.parse(_start_time);
            date2 = inFormat.parse(_end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
        String goal = outFormat.format(date);
        String goal2 = outFormat.format(date2);
        return goal + " " + "-" + " " + goal2;
    }
}
