package com.example.futsalbook.models;

import java.io.Serializable;

public class RatingModel implements Serializable {
    private float rating;
    private String user_name;

    public RatingModel(float rating, String user_name) {
        this.rating = rating;
        this.user_name = user_name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
