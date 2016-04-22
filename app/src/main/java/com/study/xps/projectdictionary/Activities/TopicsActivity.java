package com.study.xps.projectdictionary.Activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.DictionariesListViewAdapter;
import Adapters.TopicsGridViewAdapter;
import Fragments.DictionaryListFragment;
import Fragments.NoDictionariesFragments;
import Fragments.TopicsGridFragment;
import Models.Topic;
import Dialog.NewTopicDialog;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsActivity extends AppCompatActivity {

    private static final String EMPTY_LIST_TAG = "EMPTY";
    private static final String SUCCESS_QUERY_TAG = "GOT";
    private static final String LOG_TAG = "DATABASE LOGING";
    private static final String TOPIC_TAG = "TOPIC";
    private static final String DICTIONARY_TAG = "DICTIONARY";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String dictionaryID; //TODO change to long
    private GridView topicsGrid;
    public List<Topic> topicsInfo;
    public static long currentDictionaryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularies_layout);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));
        currentDictionaryID = getIntent().getLongExtra(DICTIONARY_TAG,0);

        fragmentManager = getFragmentManager();

        try {
            topicsInfo = Topic.find(Topic.class, "dictionary_ID = "+currentDictionaryID +"");
        } catch (Exception e){
          topicsInfo = new ArrayList<Topic>();
        }

        //topicsInfo = Topic.listAll(Topic.class);

        //topicsInfo = new ArrayList<Topic>();
        //for (int i = 0; i< 15; i++)
        //topicsInfo.add(new Topic(1,"asdasd",4));

        loadAppropriateFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Fragment f = fragmentManager.findFragmentById(R.id.vocabulariesFragmentContainer);

        if(f != null && f instanceof NoDictionariesFragments)
            addEmptyListListeners();

        if(f != null && f instanceof TopicsGridFragment){
            addTopicListListeners();
            updateGridData();}
    }

    public void loadAppropriateFragment(){

        if(topicsInfo.isEmpty()) {
            Fragment f = fragmentManager.findFragmentById(R.id.vocabulariesFragmentContainer);
            if(f != null && f instanceof TopicsGridFragment){
                NoDictionariesFragments noDictionariesFragments = new NoDictionariesFragments();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.vocabulariesFragmentContainer,noDictionariesFragments, EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                NoDictionariesFragments noTopicsFragments = new NoDictionariesFragments();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.vocabulariesFragmentContainer,noTopicsFragments, EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        else {
            Fragment f = fragmentManager.findFragmentById(R.id.vocabulariesFragmentContainer);
            if(f != null && f instanceof NoDictionariesFragments){
                TopicsGridFragment topicsFragment = new TopicsGridFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.vocabulariesFragmentContainer,topicsFragment, SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                TopicsGridFragment topicsFragment = new TopicsGridFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.vocabulariesFragmentContainer,topicsFragment, SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        startActivity(new Intent(TopicsActivity.this, TransparentActivity.class));
    }

    private void addTopicListListeners(){

        topicsGrid = (GridView) findViewById(R.id.topicsGridView);
        topicsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //DictionariesListViewAdapter adapter = (DictionariesListViewAdapter) parent.getAdapter();
                //Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),TopicsActivity.class);
                //dictionaryTopicsIntent.putExtra(DICTIONARY_TAG,adapter.getDictionaryName(position) );
                //startActivity(dictionaryTopicsIntent);
                Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT);
            }
        });

        FloatingActionButton addTopicButton = (FloatingActionButton) findViewById(R.id.addTopicFAB);
        addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTopicDialog();
            }
        });
    }

    private void addEmptyListListeners(){
        ImageView addDictionaryImage = (ImageView) findViewById(R.id.addVocabularyImage);
        TextView addDictionaryLabel = (TextView) findViewById(R.id.noVocabulariesText);

        addDictionaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTopicDialog();
            }
        });
        addDictionaryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTopicDialog();
            }
        });
    }

    public void updateGridData(){
        topicsGrid = (GridView) findViewById(R.id.topicsGridView);
        topicsGrid.setAdapter(new TopicsGridViewAdapter(this, topicsInfo));
        ((BaseAdapter)topicsGrid.getAdapter()).notifyDataSetChanged();
    }

    private void createTopicDialog() {
        DialogFragment topicDialog = new NewTopicDialog();
        topicDialog.show(getFragmentManager(), "ADD_TOPIC");
    }

}
