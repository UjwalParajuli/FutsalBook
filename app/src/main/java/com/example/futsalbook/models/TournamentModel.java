package com.example.futsalbook.models;

import java.io.Serializable;

public class TournamentModel implements Serializable {
    private int tournament_id, futsal_id;
    private String tournament_name, description, start_date, end_date, registration_deadline_date, registration_fee, first_prize, second_prize, organized_by,
    organizer_contact, futsal_name, futsal_location, banner;

    public TournamentModel(int tournament_id, int futsal_id, String tournament_name, String description, String start_date, String end_date, String registration_deadline_date, String registration_fee, String first_prize, String second_prize, String organized_by, String organizer_contact, String futsal_name, String futsal_location, String banner) {
        this.tournament_id = tournament_id;
        this.futsal_id = futsal_id;
        this.tournament_name = tournament_name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.registration_deadline_date = registration_deadline_date;
        this.registration_fee = registration_fee;
        this.first_prize = first_prize;
        this.second_prize = second_prize;
        this.organized_by = organized_by;
        this.organizer_contact = organizer_contact;
        this.futsal_name = futsal_name;
        this.futsal_location = futsal_location;
        this.banner = banner;
    }

    public int getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(int tournament_id) {
        this.tournament_id = tournament_id;
    }

    public int getFutsal_id() {
        return futsal_id;
    }

    public void setFutsal_id(int futsal_id) {
        this.futsal_id = futsal_id;
    }

    public String getTournament_name() {
        return tournament_name;
    }

    public void setTournament_name(String tournament_name) {
        this.tournament_name = tournament_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getRegistration_deadline_date() {
        return registration_deadline_date;
    }

    public void setRegistration_deadline_date(String registration_deadline_date) {
        this.registration_deadline_date = registration_deadline_date;
    }

    public String getRegistration_fee() {
        return registration_fee;
    }

    public void setRegistration_fee(String registration_fee) {
        this.registration_fee = registration_fee;
    }

    public String getFirst_prize() {
        return first_prize;
    }

    public void setFirst_prize(String first_prize) {
        this.first_prize = first_prize;
    }

    public String getSecond_prize() {
        return second_prize;
    }

    public void setSecond_prize(String second_prize) {
        this.second_prize = second_prize;
    }

    public String getOrganized_by() {
        return organized_by;
    }

    public void setOrganized_by(String organized_by) {
        this.organized_by = organized_by;
    }

    public String getOrganizer_contact() {
        return organizer_contact;
    }

    public void setOrganizer_contact(String organizer_contact) {
        this.organizer_contact = organizer_contact;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
