package com.example.dominik.bakalarka.Saving;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.eralp.circleprogressview.CircleProgressView;
import com.eralp.circleprogressview.ProgressAnimationListener;
import com.example.dominik.bakalarka.R;

/**
 * Created by Dominik on 06.03.2017.
 * Aktivita na vytvorenie efektu vypocitavania
 */

public class Saving extends AppCompatActivity {

    private CircleProgressView mCircleProgressView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);

        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
        mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView.setStartAngle(100);
        /* Nastavenie a spustenie animacie nacitavania */
        mCircleProgressView.setProgressWithAnimation(100, 1000);
        mCircleProgressView.addAnimationListener(new ProgressAnimationListener() {
            @Override
            public void onValueChanged(float value) {
            }

            @Override
            public void onAnimationEnd() {
                startActivity(new Intent(getApplicationContext(), SavingOptions.class));
                finish();
            }
        });
    }
}
