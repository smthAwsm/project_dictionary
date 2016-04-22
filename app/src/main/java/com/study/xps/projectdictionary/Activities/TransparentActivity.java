package com.study.xps.projectdictionary.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.study.xps.projectdictionary.R;

/**
 * Created by XPS on 4/22/2016.
 */
public class TransparentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.transparent_layout);
        finish();
    }
}
