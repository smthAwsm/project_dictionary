package com.study.xps.projectdictionary.Activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;

import Adapters.DictionariesListViewAdapter;
import Fragments.DictionaryListFragment;
import Fragments.NoVocabulariesFragments;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_EMPTY = "EMPTY";
    private static final String TAG = "GOT";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    ListView dictionariesList;
    ArrayList<Integer> dictionariesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularies_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.vocabularies_title);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if(false) {
            NoVocabulariesFragments noVocabulariesFragments = new NoVocabulariesFragments();
            fragmentTransaction.add(R.id.vocabulariesFragmentContainer, noVocabulariesFragments, TAG_EMPTY);
            fragmentTransaction.commit();
        } else {
            dictionariesInfo = new ArrayList<Integer>(){{
                add(23);
                add(89);
                add(110);
                add(9999);
            }};
            DictionaryListFragment dictionaryFragment = new DictionaryListFragment();
            fragmentTransaction.add(R.id.vocabulariesFragmentContainer,dictionaryFragment,TAG);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(fragmentManager.findFragmentByTag(TAG)!= null){
        dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
        dictionariesList.setAdapter(new DictionariesListViewAdapter(this, dictionariesInfo));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictionaries_menu, menu);
        return true;
    }


}
