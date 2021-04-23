package com.example.futsalbook.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.futsalbook.R;
import com.example.futsalbook.models.NotificationModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<NotificationModel> notificationModelArrayList;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModelArrayList, Context context) {
        this.notificationModelArrayList = notificationModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_notification, parent, false);

        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotificationModel notificationModel = notificationModelArrayList.get(position);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date past = sdf.parse(notificationModel.getStart_time());
            Date date = sdf2.parse(notificationModel.getBooked_date());

            String _date = sdf2.format(date);
            String _time = sdf.format(past);

            LocalDate datePart = LocalDate.parse(_date);
            LocalTime timePart = LocalTime.parse(_time);
            LocalDateTime dt = LocalDateTime.of(datePart, timePart);

            ZonedDateTime zdt = dt.atZone(ZoneId.of("Asia/Kathmandu"));
            long millis = zdt.toInstant().toEpochMilli();

            Date now = new Date();

            long seconds=TimeUnit.MILLISECONDS.toSeconds(millis - now.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(millis - now.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(millis - now.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(millis - now.getTime());


            if(seconds<60)
            {
                holder.text_notification.setText("You have booked " + notificationModel.getFutsal_name() + "," + " " + notificationModel.getFutsal_location() + "." + " " + seconds + " " + "seconds remaining");
            }
            else if(minutes<60)
            {
                holder.text_notification.setText("You have booked " + notificationModel.getFutsal_name() + "," + " " + notificationModel.getFutsal_location() + "." + " " + minutes + " " + "minutes remaining");
            }
            else if(hours<24)
            {
                holder.text_notification.setText("You have booked " + notificationModel.getFutsal_name() + "," + " " + notificationModel.getFutsal_location() + "." + " " + hours + " " + "hour remaining");
            }
            else
            {
                holder.text_notification.setText("You have booked " + notificationModel.getFutsal_name() + "," + " " + notificationModel.getFutsal_location() + "." + " " + days + " " + "day remaining");
            }

            Glide.with(context).load(notificationModel.getFutsal_image()).into(holder.image_futsal);
        } catch (ParseException e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text_notification;
        public ImageView image_futsal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_notification = itemView.findViewById(R.id.text_view_notification_message);
            image_futsal = itemView.findViewById(R.id.image_view_futsal_notification);
        }
    }
}
