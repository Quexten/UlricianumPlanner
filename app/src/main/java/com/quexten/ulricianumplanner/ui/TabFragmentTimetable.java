package com.quexten.ulricianumplanner.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quexten.ulricianumplanner.R;

/**
 * Created by Quexten on 04-Mar-17.
 */

public class TabFragmentTimetable extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_fragment_timetable, container, false);
    }

}