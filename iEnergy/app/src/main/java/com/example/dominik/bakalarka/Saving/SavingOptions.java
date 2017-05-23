package com.example.dominik.bakalarka.Saving;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.example.dominik.bakalarka.R;
import in.myinnos.customimagetablayout.ChangeColorTab;

/**
 * Created by Dominik on 06.03.2017.
 * Zobrazenie rad na setrenie enegrie
 */

public class SavingOptions extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_options);

        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ChangeColorTab changeColorTab = (ChangeColorTab) findViewById(R.id.tabChangeColorTab);
        changeColorTab.setViewpager(mViewPager);
        // Attach page Adapter
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        Fragment fragment;

        /* Vratenie fragmentu obrazovky pre zobrazenie */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragment = new AFragment();
                    return fragment;
                case 1:
                    fragment = new BFragment();
                    return fragment;
                case 2:
                    fragment = new CFragment();
                    return fragment;
                case 3:
                    fragment = new DFragment();
                    return fragment;
                default:
                    return null;
            }
        }

        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    /* Ukoncenie aktivity v pripade stlacenia tlacidla spat */
    public void onBackPressed() {
        this.finish();
    }
}
