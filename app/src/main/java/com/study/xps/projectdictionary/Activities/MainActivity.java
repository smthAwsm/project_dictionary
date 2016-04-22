package com.study.xps.projectdictionary.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Adapters.DictionariesListViewAdapter;
import Adapters.TopicsGridViewAdapter;
import Fragments.DictionaryListFragment;
import Fragments.NoDictionariesFragments;
import Models.Dictionary;

public class MainActivity extends AppCompatActivity {

    private static final String EMPTY_LIST_TAG = "EMPTY";
    private static final String SUCCESS_QUERY_TAG = "GOT";
    private static final String LOG_TAG = "DATABASE LOGING";
    private static final String DICTIONARY_TAG = "DICTIONARY";


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListView dictionariesList;
    private List<Dictionary> dictionariesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularies_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.vocabularies_title);

        fragmentManager = getFragmentManager();
       // Dictionary.deleteAll(Dictionary.class);
        dictionariesInfo = Dictionary.listAll(Dictionary.class);
        loadAppropriateFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = fragmentManager.findFragmentById(R.id.vocabulariesFragmentContainer);

        if(f != null && f instanceof NoDictionariesFragments)
            addEmptyListListeners();

        if(f != null && f instanceof DictionaryListFragment){
         addDictionaryListListeners();
        updateListData();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictionaries_menu, menu);
        return true;
    }

    private void loadAppropriateFragment(){

        if(dictionariesInfo.isEmpty()) {

            Fragment f = fragmentManager.findFragmentById(R.id.vocabulariesFragmentContainer);
            if(f != null && f instanceof DictionaryListFragment){
                NoDictionariesFragments noDictionariesFragments = new NoDictionariesFragments();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.vocabulariesFragmentContainer,noDictionariesFragments, EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                NoDictionariesFragments noDictionariesFragments = new NoDictionariesFragments();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.vocabulariesFragmentContainer,noDictionariesFragments, EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        else {
            Fragment f = fragmentManager.findFragmentById(R.id.vocabulariesFragmentContainer);
            if(f != null && f instanceof NoDictionariesFragments){
                DictionaryListFragment dictionariesFragment = new DictionaryListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.vocabulariesFragmentContainer,dictionariesFragment, SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                DictionaryListFragment dictionariesFragment = new DictionaryListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.vocabulariesFragmentContainer,dictionariesFragment, SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
                }
            startActivity(new Intent(MainActivity.this, TransparentActivity.class));
        }

    }

    private void createDictionaryAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_dic_title));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.save();

                dictionariesInfo = Dictionary.listAll(Dictionary.class);
                loadAppropriateFragment();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void addDictionaryListListeners(){

            dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
            dictionariesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DictionariesListViewAdapter adapter = (DictionariesListViewAdapter) parent.getAdapter();
                    Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),TopicsActivity.class);
                    dictionaryTopicsIntent.putExtra(DICTIONARY_TAG,dictionariesInfo.get(position).getId());
                    startActivity(dictionaryTopicsIntent);
                }
            });

            FloatingActionButton addDictionaryButton = (FloatingActionButton) findViewById(R.id.addDictionaryFAB);
            addDictionaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDictionaryAlert();
                }
            });
    }
    private void addEmptyListListeners(){

        ImageView addDictionaryImage = (ImageView) findViewById(R.id.addVocabularyImage);
        TextView addDictionaryLabel = (TextView) findViewById(R.id.noVocabulariesText);

        addDictionaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDictionaryAlert();
            }
        });
        addDictionaryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDictionaryAlert();
            }
        });
    }
    public void updateListData(){
        dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
        dictionariesList.setAdapter(new DictionariesListViewAdapter(this, dictionariesInfo));
        ((BaseAdapter)dictionariesList.getAdapter()).notifyDataSetChanged();
    }

}
