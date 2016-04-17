package com.study.xps.projectdictionary.Activities;

import android.app.Dialog;
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
    private List<Topic> topicsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularies_layout);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));
        getIntent().getStringExtra(DICTIONARY_TAG);

        fragmentManager = getFragmentManager();
        //topicsInfo = Topic.listAll(Topic.class);

        topicsInfo = new ArrayList<Topic>();
        for (int i = 0; i< 15; i++)
       // topicsInfo.add(new Topic(1,"asdasd",4));

        loadAppropriateFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragmentManager.findFragmentByTag(SUCCESS_QUERY_TAG)!= null)
            addDictionaryListListeners();
        else addEmptyListListeners();

     }

    private void loadAppropriateFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();

        if(topicsInfo.isEmpty()) {
            NoDictionariesFragments noDictionariesFragments = new NoDictionariesFragments();
            if(fragmentManager.findFragmentByTag(SUCCESS_QUERY_TAG)== null)
                fragmentTransaction.add(R.id.vocabulariesFragmentContainer, noDictionariesFragments, EMPTY_LIST_TAG);
            else fragmentTransaction.replace(R.id.vocabulariesFragmentContainer, noDictionariesFragments, EMPTY_LIST_TAG);
            fragmentTransaction.commit();
        } else {
            TopicsGridFragment topicsGridFragment = new TopicsGridFragment();
            if(fragmentManager.findFragmentByTag(EMPTY_LIST_TAG)== null)
                fragmentTransaction.add(R.id.vocabulariesFragmentContainer,topicsGridFragment, SUCCESS_QUERY_TAG);
                    else fragmentTransaction.replace(R.id.vocabulariesFragmentContainer,topicsGridFragment, SUCCESS_QUERY_TAG);
            fragmentTransaction.commit();
        }
    }


    private void addDictionaryListListeners(){

        topicsGrid = (GridView) findViewById(R.id.topicsGridView);
        topicsGrid.setAdapter(new TopicsGridViewAdapter(this, topicsInfo));
        ((BaseAdapter)topicsGrid.getAdapter()).notifyDataSetChanged();

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

    private void createTopicDialog(){
        Dialog addTopicDialog = new Dialog(TopicsActivity.this);
        addTopicDialog.setTitle("Add new topic");
        addTopicDialog.setContentView(R.layout.dialog_topic_add);
        
        addTopicDialog.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.new_dic_title));
//
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String topicName = input.getText().toString();
//                Topic newTopic = new Topic(228,topicName,R.drawable.btn_google_signin_dark_pressed); //TODO fix dicID
//                newTopic.save();
//
//                topicsInfo = Topic.listAll(Topic.class);
//
//                loadAppropriateFragment();
//
//                if(fragmentManager.findFragmentByTag(SUCCESS_QUERY_TAG)!= null)
//                    addDictionaryListListeners();
//
//            }
//        });
//        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
    }
//
//    private void addDictionaryListListeners(){
//
//        dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
//        dictionariesList.setAdapter(new DictionariesListViewAdapter(this, dictionariesInfo));
//        ((BaseAdapter)dictionariesList.getAdapter()).notifyDataSetChanged();
//
//        dictionariesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DictionariesListViewAdapter adapter = (DictionariesListViewAdapter) parent.getAdapter();
//                Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),TopicsActivity.class);
//                dictionaryTopicsIntent.putExtra(DICTIONARY_TAG,adapter.getDictionaryName(position) );
//                startActivity(dictionaryTopicsIntent);
//            }
//        });
//
//        FloatingActionButton addDictionaryButton = (FloatingActionButton) findViewById(R.id.addDictionaryFAB);
//        addDictionaryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createDictionaryAlert();
//            }
//        });
//    }
//
//    private void addEmptyListListeners(){
//
//        ImageView addDictionaryImage = (ImageView) findViewById(R.id.addVocabularyImage);
//        TextView addDictionaryLabel = (TextView) findViewById(R.id.noVocabulariesText);
//
//        addDictionaryImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createDictionaryAlert();
//            }
//        });
//        addDictionaryLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createDictionaryAlert();
//            }
//        });
//    }
//
}
