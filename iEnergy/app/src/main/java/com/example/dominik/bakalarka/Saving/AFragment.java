package com.example.dominik.bakalarka.Saving;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dominik.bakalarka.R;

/**
 * Created by Dominik on 06.03.2017.
 * Fragment pre jednotlivu radu k setreniu
 */

public class AFragment extends Fragment {
    private String mTitle = "A";

    public static final String TITLE = "title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
        View v = inflater.inflate(R.layout.fragment_a, container, false);


        return v;

    }
}