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
import com.example.futsalbook.models.TournamentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.ViewHolder> {
    ArrayList<TournamentModel> tournamentModelArrayList;
    Context context;

    public TournamentAdapter(ArrayList<TournamentModel> tournamentModelArrayList, Context context) {
        this.tournamentModelArrayList = tournamentModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TournamentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_tournament, parent, false);
        TournamentAdapter.ViewHolder viewHolder =new TournamentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentAdapter.ViewHolder holder, int position) {
        TournamentModel tournamentModel = tournamentModelArrayList.get(position);

        Glide.with(context).load(tournamentModel.getBanner()).into(holder.img_tournament_banner);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = null, dateEnd = null;
        long finalDateStart = 0, finalDateEnd = 0;
        try {
            dateStart = format.parse(tournamentModel.getStart_date());
            finalDateStart = dateStart.getTime();
            dateEnd = format.parse(tournamentModel.getEnd_date());
            finalDateEnd = dateEnd.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = (String) DateFormat.format("EEE", finalDateStart); // Thursday
        String day          = (String) DateFormat.format("dd",   finalDateStart); // 20
        String monthString  = (String) DateFormat.format("MMM",  finalDateStart); // Jun
        if (finalDateStart == finalDateEnd){
            holder.tournament_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day);
        }
        else {
            String dayOfTheWeek2 = (String) DateFormat.format("EEE", finalDateEnd); // Thursday
            String day2          = (String) DateFormat.format("dd",   finalDateEnd); // 20
            String monthString2  = (String) DateFormat.format("MMM",  finalDateEnd); // Jun
            holder.tournament_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + " " + "-" + " " + dayOfTheWeek2 + "," + " " + monthString2 + " " + day2 );
        }

        holder.tournament_title.setText(tournamentModel.getTournament_name());
        holder.tournament_venue.setText(tournamentModel.getFutsal_name());
        holder.tournament_deadline_date.setText(tournamentModel.getFutsal_location());

        holder.btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TournamentModel tournamentModel1 = tournamentModelArrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tournament_details", tournamentModel1);
                Intent intent = new Intent(context, TournamentDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tournamentModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tournament_date, tournament_title, tournament_venue, tournament_deadline_date;
        public Button btn_view_more;
        public ImageView img_tournament_banner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tournament_date = itemView.findViewById(R.id.tournament_date);
            tournament_title = itemView.findViewById(R.id.tournament_title);
            tournament_venue = itemView.findViewById(R.id.tournament_venue);
            tournament_deadline_date = itemView.findViewById(R.id.tournament_deadline_date);
            btn_view_more = itemView.findViewById(R.id.btn_view_more);
            img_tournament_banner = itemView.findViewById(R.id.img_tournament_banner);
        }
    }
}
