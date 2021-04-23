package com.example.futsalbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.futsalbook.R;
import com.example.futsalbook.TournamentDetailsActivity;
import com.example.futsalbook.models.BookingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    ArrayList<BookingModel> bookingModelArrayList;
    Context context;

    public BookingAdapter(ArrayList<BookingModel> bookingModelArrayList, Context context) {
        this.bookingModelArrayList = bookingModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_booking, parent, false);
        BookingAdapter.ViewHolder viewHolder =new BookingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        BookingModel bookingModel = bookingModelArrayList.get(position);

        holder.text_view_booking_futsal_name.setText(bookingModel.getFutsal_name());
        holder.text_view_booking_futsal_venue.setText(bookingModel.getFutsal_location());
        holder.text_view_full_name.setText(bookingModel.getUser_name());

        SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = inFormat.parse(bookingModel.getStart_time());
            date2 = inFormat.parse(bookingModel.getEnd_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
        String goal = outFormat.format(date);
        String goal2 = outFormat.format(date2);
        holder.text_view_booking_time.setText(goal + " " + "-" + " " + goal2);

        holder.text_view_booking_booked_date.setText(bookingModel.getBooked_date());
        holder.text_view_booking_booked_on.setText(bookingModel.getBooked_on());


    }

    @Override
    public int getItemCount() {
        return bookingModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text_view_booking_futsal_name, text_view_booking_futsal_venue, text_view_full_name, text_view_booking_time,
                text_view_booking_booked_date, text_view_booking_booked_on;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_booking_futsal_name = itemView.findViewById(R.id.text_view_booking_futsal_name);
            text_view_booking_futsal_venue = itemView.findViewById(R.id.text_view_booking_futsal_venue);
            text_view_full_name = itemView.findViewById(R.id.text_view_full_name);
            text_view_booking_time = itemView.findViewById(R.id.text_view_booking_time);
            text_view_booking_booked_date = itemView.findViewById(R.id.text_view_booking_booked_date);
            text_view_booking_booked_on = itemView.findViewById(R.id.text_view_booking_booked_on);

        }
    }
}
