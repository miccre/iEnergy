package com.example.dominik.bakalarka.Intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.example.dominik.bakalarka.R;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by Dominik on 08.10.2016.
 * Zobrazenie par snimkov o setreni energie
 */

public final class DefaultIntro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Pridanie snimkov ktore sa maju zobrazit */
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
        setDoneText("Štart");
        setSkipText("Preskoč");
        showStatusBar(true);

        setDepthAnimation();
    }
    /* Posluchac na tlacidlo start */
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }
    /* Nacitanie hlavnej aktivity */
    private void loadMainActivity(){
        this.finish();
    }
    /* Posluchac na tlacidlo preskocit */
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}