package com.teamsar.eventure.activities.activity_search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.teamsar.eventure.R;

public class SearchActivity extends AppCompatActivity {

    //Tag for testing purpose
    private static final String LOG_TAG = "activity-search-context";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}