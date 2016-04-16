package com.study.xps.projectdictionary.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.study.xps.projectdictionary.R;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics_layout);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
