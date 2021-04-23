package com.example.futsalbook.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String start_time, end_time, booked_date, futsal_name, futsal_location, futsal_image;

    public NotificationModel(String start_time, String end_time, String booked_date, String futsal_name, String futsal_location, String futsal_image) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.booked_date = booked_date;
        this.futsal_name = futsal_name;
        this.futsal_location = futsal_location;
        this.futsal_image = futsal_image;
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

    public String getBooked_date() {
        return booked_date;
    }

    public void setBooked_date(String booked_date) {
        this.booked_date = booked_date;
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

    public String getFutsal_image() {
        return futsal_image;
    }

    public void setFutsal_image(String futsal_image) {
        this.futsal_image = futsal_image;
    }
}
