package com.example.dominik.bakalarka.Function;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.example.dominik.bakalarka.R;
import com.example.dominik.bakalarka.Graph.PieChartActivity;
import cn.refactor.lib.colordialog.PromptDialog;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Dominik on 17.02.2017.
 * Vysvetlenie jednotlivych poloziek faktury
 */

public class Faktura extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faktura);

        ImageView iv = (ImageView) findViewById(R.id.faktura);

        PhotoViewAttacher photoView = new PhotoViewAttacher(iv);
        photoView.update();

        /* Rozdelenie udajov na fotke na jednotlive casti(1) a zavolanie zobrazenia dialogoveho okna */
        PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {

                if( MotionEvent.ACTION_DOWN == 0){
                    if(v > 0 && v1 > .4 && v < .48 && v1 < .5)
                        showDialog("Finančné vyrovnanie", R.string.vyrovnanie);
                    if(v > .50 && v1 > .51 && v < 1.00 && v1 < .56)
                        showDialog("Celková spotreba za vyučtovacie obdobie", R.string.spotreba);
                    if(v > 0 && v1 > .57 && v < 1 && v1 < .65)
                        showDialog("Mesačné platby", R.string.harmonogram);
                    if(v > 0 && v1 > .76 && v < 1 && v1 < 1)
                        showDialog("Kontaktné údaje",R.string.reklama);
                    if(v > 0 && v1 > .13 && v < .4 && v1 < .3)
                        showDialog("Udaje zákazníka", R.string.udaje);

                }
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        };

        photoView.setOnPhotoTapListener(listener);

        ImageView iv2 = (ImageView) findViewById(R.id.faktura2);

        PhotoViewAttacher photoView2 = new PhotoViewAttacher(iv2);
        photoView2.update();

        /* Rozdelenie udajov na fotke na jednotlive casti(2) a zavolanie zobrazenia dialogoveho okna */
        PhotoViewAttacher.OnPhotoTapListener listener2 = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {

                if( MotionEvent.ACTION_DOWN == 0){
                    if(v > 0 && v1 > .12 && v < 1.00 && v1 < .24)
                        showDialog("Dph", R.string.dph);
                    if(v > 0 && v1 > .25 && v < 1 && v1 < .40)
                        showDialog("Namerané hodnoty", R.string.hodnoty);
                    if(v > 0 && v1 > .41 && v < 1 && v1 < .56)
                        showDialog("Dodávka", R.string.ceny);
                    if(v > 0 && v1 > .57 && v < 1 && v1 < .79)
                        showDialog2("Distribúcia", R.string.distribucia);
                    if(v > 0 && v1 > .80 && v < 1 && v1 < 1)
                        showDialog("Platby", R.string.platby);

                }
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        };

        photoView2.setOnPhotoTapListener(listener2);
    }

    /* Dialogove okno s vysvetlenim polozky */
    public void showDialog(String msg, int popis) {

        new PromptDialog(this)
                .setDialogType(PromptDialog.BUTTON_NEUTRAL)
                .setAnimationEnable(true)
                .setTitleText(msg)
                .setContentText(popis)
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /* Dialogove okno s vysvetlenim polozky a moznostou prejst na graf */
    public void showDialog2(String msg, int popis) {

        new PromptDialog(this)
                .setDialogType(PromptDialog.BUTTON_NEUTRAL)
                .setAnimationEnable(true)
                .setTitleText(msg)
                .setContentText(popis)
                .setPositiveListener("Graf", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        startActivity(new Intent(getApplicationContext(), PieChartActivity.class));
                        dialog.dismiss();
                    }
                }).show();
    }

    /* Ukoncenie aktivity v pripade stlacenia tlacidla spat */
    public void onBackPressed() {
        this.finish();
    }
}
