package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.futsalbook.models.TournamentModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TournamentDetailsActivity extends AppCompatActivity {
    private Bundle bundle;
    private TournamentModel tournamentModel;
    private Toolbar toolbar;
    private ImageView tournament_image_full;
    private TextView text_view_tournament_name, text_view_tournament_location, text_view_organizer_phone, text_view_tournament_fee, text_view_tournament_description,
            text_view_first_prize, text_view_second_prize, text_view_tournament_date, text_view_deadline_date, text_view_organized_by;
    private Button button_share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        bundle = getIntent().getExtras();
        tournamentModel = (TournamentModel) bundle.getSerializable("tournament_details");

        toolbar = findViewById(R.id.toolbar_tournament_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tournament_image_full = findViewById(R.id.tournament_image_full);
        text_view_tournament_name = findViewById(R.id.text_view_tournament_name);
        text_view_tournament_location = findViewById(R.id.text_view_tournament_location);
        text_view_organizer_phone = findViewById(R.id.text_view_organizer_phone);
        text_view_tournament_fee = findViewById(R.id.text_view_tournament_fee);
        text_view_tournament_description = findViewById(R.id.text_view_tournament_description);
        text_view_first_prize = findViewById(R.id.text_view_first_prize);
        text_view_second_prize = findViewById(R.id.text_view_second_prize);
        text_view_tournament_date = findViewById(R.id.text_view_tournament_date);
        text_view_deadline_date = findViewById(R.id.text_view_deadline_date);
        text_view_organized_by = findViewById(R.id.text_view_organized_by);
        button_share = findViewById(R.id.button_share);

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTournament();
            }
        });

        setData();

    }

    private void setData(){
        this.setTitle(tournamentModel.getTournament_name());
        Glide.with(this).load(tournamentModel.getBanner()).into(tournament_image_full);
        text_view_tournament_name.setText(tournamentModel.getTournament_name());
        text_view_tournament_location.setText(tournamentModel.getFutsal_name() + "," + " " + tournamentModel.getFutsal_location());
        text_view_organizer_phone.setText(tournamentModel.getOrganizer_contact());
        text_view_tournament_fee.setText("Rs. " + tournamentModel.getRegistration_fee() + "/team");
        text_view_tournament_description.setText(tournamentModel.getDescription());
        text_view_first_prize.setText(tournamentModel.getFirst_prize());
        text_view_second_prize.setText(tournamentModel.getSecond_prize());
        text_view_deadline_date.setText(tournamentModel.getRegistration_deadline_date());
        text_view_organized_by.setText(tournamentModel.getOrganized_by());

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
            text_view_tournament_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day);
        }
        else {
            String dayOfTheWeek2 = (String) DateFormat.format("EEE", finalDateEnd); // Thursday
            String day2          = (String) DateFormat.format("dd",   finalDateEnd); // 20
            String monthString2  = (String) DateFormat.format("MMM",  finalDateEnd); // Jun
            text_view_tournament_date.setText(dayOfTheWeek + "," + " " + monthString + " " + day + " " + "-" + " " + dayOfTheWeek2 + "," + " " + monthString2 + " " + day2 );
        }
    }

    private void shareTournament(){
        Bitmap bitmap = getBitmapFromView(tournament_image_full);
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //to access inside the directory
            StrictMode.setVmPolicy(builder.build());
            File file = new File(this.getExternalCacheDir(), "tournament_banner.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Invitation for Futsal Tournament");
            intent.putExtra(Intent.EXTRA_TEXT,tournamentModel.getTournament_name() + "\n" + "\n" + tournamentModel.getDescription() + "\n" + "\n" +
                    "Date: " + tournamentModel.getStart_date() + "\n" + "\n" +
                    "To know more about the futsal tournament, download Futsal Book app from Google Play Store." + "\n" + "\n" +
                    "If already downloaded, login to your account and view the detailed information.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView (View view){
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }


}