package com.example.futsalbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.futsalbook.R;
import com.example.futsalbook.models.RatingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    ArrayList<RatingModel> ratingModelArrayList;
    Context context;

    public RatingAdapter(ArrayList<RatingModel> ratingModelArrayList, Context context) {
        this.ratingModelArrayList = ratingModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_ratings, parent, false);
        RatingAdapter.ViewHolder viewHolder =new RatingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        RatingModel ratingModel = ratingModelArrayList.get(position);

        holder.user_name.setText(ratingModel.getUser_name());
        holder.ratingBar.setRating(ratingModel.getRating());

    }

    @Override
    public int getItemCount() {
        return ratingModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView user_name;
        public RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.rating_user_name);
            ratingBar = itemView.findViewById(R.id.all_rating_bars);
        }
    }
}
