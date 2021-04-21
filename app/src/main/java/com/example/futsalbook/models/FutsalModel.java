package com.example.futsalbook.models;

import java.io.Serializable;

public class FutsalModel implements Serializable {
    private int futsal_id, price_per_hour;
    private String futsal_name, location, description, phone, image;

    public FutsalModel(int futsal_id, int price_per_hour, String futsal_name, String location, String description, String phone, String image) {
        this.futsal_id = futsal_id;
        this.price_per_hour = price_per_hour;
        this.futsal_name = futsal_name;
        this.location = location;
        this.description = description;
        this.phone = phone;
        this.image = image;
    }

    public int getFutsal_id() {
        return futsal_id;
    }

    public void setFutsal_id(int futsal_id) {
        this.futsal_id = futsal_id;
    }

    public int getPrice_per_hour() {
        return price_per_hour;
    }

    public void setPrice_per_hour(int price_per_hour) {
        this.price_per_hour = price_per_hour;
    }

    public String getFutsal_name() {
        return futsal_name;
    }

    public void setFutsal_name(String futsal_name) {
        this.futsal_name = futsal_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
