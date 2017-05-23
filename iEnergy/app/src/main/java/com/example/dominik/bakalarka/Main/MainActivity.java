package com.example.dominik.bakalarka.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.dominik.bakalarka.LoginRegister.Login;
import com.example.dominik.bakalarka.R;
import com.example.dominik.bakalarka.Saving.Saving;
import com.example.dominik.bakalarka.Function.Calculation;
import com.example.dominik.bakalarka.Function.Faktura;
import com.example.dominik.bakalarka.Function.Info;
import com.example.dominik.bakalarka.Graph.PieChartActivity;
import com.example.dominik.bakalarka.Intro.Config;
import com.example.dominik.bakalarka.Intro.DefaultIntro;
import com.github.mikephil.charting.charts.BarChart;
import com.tuyenmonkey.mkloader.MKLoader;
import java.util.HashMap;

/**
 * Created by Dominik on 17.01.2017.
 * Hlavna aktivita, hlavna obrazovka, najdolezitejsie udaje
 */

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToogle;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    int count;
    public static TextView  navUsername;
    private SliderLayout mDemoSlider;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String name = preferences.getString("Name", "");
        /* Zobrazenie snimkov o setreni energie */

        showIntro();

        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToogle = setupDrawerToogle();
        //Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        mDrawer.addDrawerListener(drawerToogle);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        /* Zavolanie vypoctov ohladom setrenia a nastavenie jednotlivzch komponentov podla vypoctu */
        setMainInfo();
        //count = Runtime.getRuntime().availableProcessors();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.nav);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("name", "");
        String surname = preferences.getString("surname", "");
        navUsername.setText("Používateľ: " + name + " " + surname);
        /* Nastavenie udajov pre slider obrazkov */
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Veterná elektráreň",R.drawable.wind);
        file_maps.put("Vodná elektráreň",R.drawable.water);
        file_maps.put("Solárne panely",R.drawable.solar);
        file_maps.put("Geotermálna energia", R.drawable.geo);
        for(String names : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(names)
                    .image(file_maps.get(names))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //textSliderView.bundle(new Bundle());
            //textSliderView.getBundle()
            //        .putString("extra",names);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

    }

    public static TextView naklady, rozdiel, najProgram, odporucanyProgram, najTarifa, odporucanaTarifa, spotreba, odporucanaSpotreba;

    /* Zavolanie vypoctu a nastavenie jednotlivych komponentov */
    public void setMainInfo() {
        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        naklady = (TextView) findViewById(R.id.naklady);
        rozdiel = (TextView) findViewById(R.id.rozdiel);
        najProgram = (TextView) findViewById(R.id.najProgram);
        odporucanyProgram = (TextView) findViewById(R.id.odporucanyProgram);
        najTarifa = (TextView) findViewById(R.id.tarifa);
        odporucanaTarifa = (TextView) findViewById(R.id.odporucanaTarifa);
        spotreba = (TextView) findViewById(R.id.spotreba);
        odporucanaSpotreba = (TextView) findViewById(R.id.odporucanaSpotreba);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        /* Ziskanie udajov ulozenych v programe */
        float payment = preferences.getFloat("payment", 0);
        float usageNT = preferences.getFloat("usageNT", 0);
        float usageVT = preferences.getFloat("usageVT", 0);
        najProgram.setText(preferences.getString("distributor", ""));
        naklady.setText(payment * 12 + " €");
        spotreba.setText(usageNT + usageVT + " kWh");
        najTarifa.setText(preferences.getString("tariff", ""));

        Calculation c = new Calculation(this, preferences.getString("distributor", ""), preferences.getString("tariff", ""), preferences.getString("house", ""), usageNT,
                usageVT, payment);
        float cena = c.comparePrice();

        if (cena < payment * 12) {
            float rozdielCien = payment*12 -cena;
            rozdiel.setText("↑ " + rozdielCien + " €");
            rozdiel.setTextColor(getResources().getColor(R.color.red));
        }
        else {
            float rozdielCien = cena - payment*12;
            rozdiel.setText("↓ " + rozdielCien + " €");
            rozdiel.setTextColor(getResources().getColor(R.color.green));
        }

        float usage = c.compareUsage();
        if (usage < (usageNT+usageVT)) {
            float rozdielSpotreby = (usageNT+usageVT) -usage;
            odporucanaSpotreba.setText("↑ " + rozdielSpotreby + " kWh");
            odporucanaSpotreba.setTextColor(getResources().getColor(R.color.red));
        }
        else {
            float rozdielSpotreby = usage - (usageNT+usageVT);
            odporucanaSpotreba.setText("↓ " + rozdielSpotreby + " kWh");
            odporucanaSpotreba.setTextColor(getResources().getColor(R.color.green));
        }

        c.findBestDistributor();

        odporucanyProgram.setText(Calculation.najDistributor);
        odporucanaTarifa.setText(Calculation.najTarifa);


        najTarifa.setSelected(true);
        odporucanaTarifa.setSelected(true);
    }

    private  ActionBarDrawerToggle setupDrawerToogle() {
        return new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.drawer_open,R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }
    /* Vyber jednotlivych poloziek z menu a zavolanie odpovedajucich aktivit */
    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;

        switch(item.getItemId()) {

            case R.id.ukazGraf:
                fragmentClass = PieChartActivity.class;
                startActivity(new Intent(MainActivity.this, PieChartActivity.class));
                break;
            case R.id.Setrenie:
                fragmentClass = DefaultIntro.class;
                startActivity(new Intent(MainActivity.this,DefaultIntro.class));
                break;
            case R.id.polozkyFaktury:
                fragmentClass= Faktura.class;
                startActivity(new Intent(MainActivity.this, Faktura.class));
                break;
            case R.id.saving:
                fragmentClass= Saving.class;
                startActivity(new Intent(MainActivity.this, Saving.class));
                break;
            case R.id.logout:
                fragmentClass= Login.class;
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                break;
            case R.id.info:
                fragmentClass= Login.class;
                startActivity(new Intent(MainActivity.this, Info.class));
                break;
            default:
                fragmentClass = MainActivity.class;
        }
        try {

            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {

            e.printStackTrace();

        }

        item.setChecked(true);
        mDrawer.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToogle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //pass configuration to change drawer toggles
        drawerToogle.onConfigurationChanged(newConfig);
    }
    public void setVisibility(View v) {

        setMainInfo();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Zavolanie aktivit s radami o setreni */
    public void showRady(View v) {
        startActivity(new Intent(MainActivity.this, Saving.class));
    }
    /* Ukazanie hlavnych rad o setreni */
    public void showIntro() {
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(Config.FLAG, Context.MODE_APPEND);
                if(sharedPreferences.getBoolean(Config.FLAG,true)){
                    startActivity(new Intent(MainActivity.this,DefaultIntro.class));
                    SharedPreferences.Editor e=sharedPreferences.edit();
                    e.putBoolean(Config.FLAG,false);
                    e.apply();
                }
            }
        });
        t.start();
    }
}
