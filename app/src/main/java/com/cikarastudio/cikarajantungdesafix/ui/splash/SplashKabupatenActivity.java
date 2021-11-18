package com.cikarastudio.cikarajantungdesafix.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ui.intro.IntroActivity;

public class SplashKabupatenActivity extends AppCompatActivity {

    private int loadingTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_kabupaten);

        ImageView logo = findViewById(R.id.logo);
        TextView aplikasi = findViewById(R.id.textView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke intro activity
                Intent home = new Intent(SplashKabupatenActivity.this, IntroActivity.class);
                startActivity(home);
                finish();

            }
        }, loadingTime);

        Animation myanim = AnimationUtils.loadAnimation(SplashKabupatenActivity.this, R.anim.splashanimation);
        Animation textanim = AnimationUtils.loadAnimation(SplashKabupatenActivity.this, R.anim.splashtextanimation);
        aplikasi.startAnimation(textanim);
        logo.startAnimation(myanim);

    }
}