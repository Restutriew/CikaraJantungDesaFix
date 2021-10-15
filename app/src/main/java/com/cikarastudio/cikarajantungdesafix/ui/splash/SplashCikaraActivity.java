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

public class SplashCikaraActivity extends AppCompatActivity {
    private int loadingTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_cikara);

        ImageView logo_cikara = findViewById(R.id.logo_cikara);
        TextView supported_by = findViewById(R.id.supported_by);
        TextView copyright = findViewById(R.id.copyright);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke intro activity
                Intent home = new Intent(SplashCikaraActivity.this, SplashActivity.class);
                startActivity(home);
                finish();

            }
        }, loadingTime);

        Animation myanim = AnimationUtils.loadAnimation(SplashCikaraActivity.this, R.anim.splashanimation);
        Animation textanim = AnimationUtils.loadAnimation(SplashCikaraActivity.this, R.anim.splashtextanimation);
        supported_by.startAnimation(textanim);
        copyright.startAnimation(textanim);
        logo_cikara.startAnimation(myanim);
    }
}