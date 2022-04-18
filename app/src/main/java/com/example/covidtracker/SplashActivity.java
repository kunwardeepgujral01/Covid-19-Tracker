package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    Animation topAnimation,bottomAnimation;
    View topAnimView;
    TextView bottomAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topAnimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        topAnimView=findViewById(R.id.animation_view);
        bottomAnimView=findViewById(R.id.splashText);

        topAnimView.setAnimation(topAnimation);
        bottomAnimView.setAnimation(bottomAnimation);

        Thread td = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception ex) {
                    Toast.makeText(SplashActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();

                } finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };td.start();
    }
}