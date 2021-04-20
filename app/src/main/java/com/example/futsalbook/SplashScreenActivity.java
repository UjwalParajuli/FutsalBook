package com.example.futsalbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futsalbook.utils.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {
    private Animation topAnim, bottomAnim;
    private ImageView image_view_logo_splash_screen;
    private TextView text_view_app_title_splash_screen;
    private static int SPLASH_TIME_OUT = 5000;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image_view_logo_splash_screen = findViewById(R.id.image_view_logo_splash_screen);
        text_view_app_title_splash_screen = findViewById(R.id.text_view_app_title_splash_screen);

        image_view_logo_splash_screen.setAnimation(topAnim);
        text_view_app_title_splash_screen.setAnimation(bottomAnim);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
}