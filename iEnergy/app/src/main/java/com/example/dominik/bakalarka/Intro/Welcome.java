package com.example.dominik.bakalarka.Intro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.dominik.bakalarka.LoginRegister.Login;
import com.example.dominik.bakalarka.R;

/**
 * Created by Dominik on 16.08.2016.
 * Uvodna obrazovka s logom aplikacie
 */

public class Welcome extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        /* Prepojenie komponentov a spustenie animacie */
        ImageView Imgvw_spsoa= (ImageView)findViewById(R.id.imageView);
        ImageView Imgvw_spsoa2= (ImageView)findViewById(R.id.imageView3);
        ImageView nazov = (ImageView) findViewById(R.id.nazov);
        Animation Anim_spsoalogo = AnimationUtils.loadAnimation(this, R.anim.welcome_sr);
        Animation Anim_spsoalogo2 = AnimationUtils.loadAnimation(this, R.anim.welcome_sr2);
        Animation Anim_spsoalogo3 = AnimationUtils.loadAnimation(this, R.anim.animate_nazov);
        Imgvw_spsoa.startAnimation(Anim_spsoalogo);
        Imgvw_spsoa2.startAnimation(Anim_spsoalogo2);
        nazov.startAnimation(Anim_spsoalogo3);

        /* Uspanie vlakna na 4.5 sekundy kvoli animacii */
        Thread Thrd_wlcmscrndelay = new Thread() {
            public void run() {
                try {
                    sleep(4500);
                    Intent myIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(myIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                }
            }
        };
        Thrd_wlcmscrndelay.start();
    }
}
