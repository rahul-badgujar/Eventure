package com.teamsar.eventure.activities.activity_home.fragments_bnb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamsar.eventure.R;

public class TimelineFragment extends Fragment {
    public static final String FRAGMENT_TAG="TIMELINE_FRAGMENT_TAG";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }
}