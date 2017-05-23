package com.example.dominik.bakalarka.Function;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.dominik.bakalarka.LoginRegister.UpdateData;
import com.example.dominik.bakalarka.Main.MainActivity;
import com.example.dominik.bakalarka.R;

/**
 * Created by Dominik on 19.03.2017.
 * Zobrazenie udajov pouzivatela
 */

public class Info extends AppCompatActivity {

    public static TextView  name, house, program, total_area, payment, upravene, setUsageNT, setUsageVT, setTariff;
    Button edit;
    String sname, ssurname, shouse, sprogram, supravene, sTariff;
    float stotal_area, spayment, sUsageNT, sUsageVT;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        /* Nastavenie titulku */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout_info);
        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

         /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        name = (TextView) findViewById(R.id.setName);
        house = (TextView) findViewById(R.id.setHouse);
        program = (TextView) findViewById(R.id.setProgram);
        total_area = (TextView) findViewById(R.id.setTotal_area);
        payment = (TextView) findViewById(R.id.setPayment);
        setUsageNT = (TextView) findViewById(R.id.setUsageNT);
        setUsageVT = (TextView) findViewById(R.id.setUsageVT);
        setTariff = (TextView) findViewById(R.id.setTariff);
        edit = (Button) findViewById(R.id.uprav);
        /* Nastavenie jednotlivych dat */
        updateData();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UpdateData.class));
            }
        });
    }

    /* Ziskanie a nastavenie jednotlivych udajov pouzivatela */
    public void updateData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sname = preferences.getString("name", "");
        ssurname = preferences.getString("surname", "");
        shouse = preferences.getString("house", "");
        sprogram = preferences.getString("distributor", "");
        stotal_area = preferences.getFloat("total_area", 0);
        spayment = preferences.getFloat("payment", 0);
        sUsageNT = preferences.getFloat("usageNT", 0);
        sUsageVT = preferences.getFloat("usageVT", 0);
        sTariff = preferences.getString("tariff", "");
        name.setText(sname + " " + ssurname);
        house.setText(shouse);
        program.setText(sprogram);
        total_area.setText(String.valueOf(stotal_area));
        payment.setText(String.valueOf(spayment));
        setUsageVT.setText(String.valueOf(sUsageVT));
        setUsageNT.setText(String.valueOf(sUsageNT));
        setTariff.setText(sTariff);
    }

    /* Ukoncenie aktivity v pripade stlacenia tlacidla spat a nastavenie jednotlivych komponentov v hlavnej aktivite */
    public void onBackPressed() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sname = preferences.getString("name", "");
        ssurname = preferences.getString("surname", "");
        MainActivity.navUsername.setText("Používateľ: " + sname + " " + ssurname);
        this.finish();
    }
}
