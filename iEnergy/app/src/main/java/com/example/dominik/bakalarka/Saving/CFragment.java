package com.example.dominik.bakalarka.Saving;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dominik.bakalarka.R;

/**
 * Created by Dominik on 06.03.2017.
 * Fragment pre jednotlivu radu k setreniu
 */

public class CFragment extends Fragment {
    private String mTitle = "C";

    public static final String TITLE = "title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
        View v = inflater.inflate(R.layout.fragment_c, container, false);

        TextView tv = new TextView(getActivity());
        tv.setTextSize(50);
        tv.setTextColor(Color.parseColor("#ff000000"));
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);
        return v;

    }
}