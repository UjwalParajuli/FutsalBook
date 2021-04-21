package com.example.futsalbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.futsalbook.R;
import com.example.futsalbook.models.TimeModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    ArrayList<TimeModel> timeModelArrayList;
    Context context;

    public TimeAdapter(ArrayList<TimeModel> timeModelArrayList, Context context) {
        this.timeModelArrayList = timeModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_time, parent, false);
        TimeAdapter.ViewHolder viewHolder =new TimeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.ViewHolder holder, int position) {
        TimeModel timeModel = timeModelArrayList.get(position);

        SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = inFormat.parse(timeModel.getStart_time());
            date2 = inFormat.parse(timeModel.getEnd_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
        String goal = outFormat.format(date);
        String goal2 = outFormat.format(date2);
        holder.time.setText(goal + " " + "-" + " " + goal2);

    }

    @Override
    public int getItemCount() {
        return timeModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.text_view_time_table);
        }
    }
}
