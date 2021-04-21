package com.example.futsalbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.futsalbook.R;
import com.example.futsalbook.models.FutsalModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FutsalAdapter extends RecyclerView.Adapter<FutsalAdapter.ViewHolder> {
    ArrayList<FutsalModel> futsalModelArrayList;
    Context context;

    public FutsalAdapter(ArrayList<FutsalModel> FutsalModelArrayList, Context context) {
        this.futsalModelArrayList = FutsalModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FutsalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_futsal, parent, false);
        FutsalAdapter.ViewHolder viewHolder =new FutsalAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FutsalAdapter.ViewHolder holder, int position) {
        FutsalModel futsalModel = futsalModelArrayList.get(position);

        Glide.with(context).load(futsalModel.getImage()).into(holder.img_futsal_banner);
        holder.futsal_name.setText(futsalModel.getFutsal_name());
        holder.futsal_price.setText("Rs. " + futsalModel.getPrice_per_hour() + "/hr");
        holder.futsal_phone.setText(futsalModel.getPhone());
        holder.futsal_venue.setText(futsalModel.getLocation());

    }

    @Override
    public int getItemCount() {
        return futsalModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView futsal_name, futsal_venue, futsal_phone, futsal_price;
        public ImageView img_futsal_banner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            futsal_name = itemView.findViewById(R.id.futsal_name);
            futsal_venue = itemView.findViewById(R.id.futsal_venue);
            futsal_phone = itemView.findViewById(R.id.futsal_phone);
            futsal_price = itemView.findViewById(R.id.futsal_price);
            img_futsal_banner = itemView.findViewById(R.id.img_futsal_banner);

        }
    }
}
