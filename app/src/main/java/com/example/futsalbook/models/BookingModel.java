package com.example.futsalbook.models;

import java.io.Serializable;

public class BookingModel implements Serializable {
    private String futsal_name, futsal_location, user_name, start_time, end_time, booked_date, booked_on;

    public BookingModel(String futsal_name, String futsal_location, String user_name, String start_time, String end_time, String booked_date, String booked_on) {
        this.futsal_name = futsal_name;
        this.futsal_location = futsal_location;
        this.user_name = user_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.booked_date = booked_date;
        this.booked_on = booked_on;
    }

    public String getFutsal_name() {
        return futsal_name;
    }

    public void setFutsal_name(String futsal_name) {
        this.futsal_name = futsal_name;
    }

    public String getFutsal_location() {
        return futsal_location;
    }

    public void setFutsal_location(String futsal_location) {
        this.futsal_location = futsal_location;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
    }

    public String getBooked_on() {
        return booked_on;
    }

    public void setBooked_on(String booked_on) {
        this.booked_on = booked_on;
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
}
