package activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.TopicsGridViewAdapter;
import Dialog.RUDTopicDialog;
import Fragments.EmptyFragment;
import Fragments.TopicsGridFragment;
import Models.ActivityDataInterface;
import Models.Tags;
import Models.Topic;
import Dialog.NewTopicDialog;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsActivity extends AppCompatActivity implements ActivityDataInterface{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GridView topicsGrid;
    private List<Topic> topicsInfo;
    private long currentDictionaryID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));
        currentDictionaryID = getIntent().getLongExtra(Tags.DICTIONARY_TAG,0);

        fragmentManager = getFragmentManager();
        updateData();
        //topicsInfo = Topic.listAll(Topic.class);
        loadAppropriateFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
        if(f != null && f instanceof TopicsGridFragment)
              updateViewData();
    }
    public void loadAppropriateFragment(){

        if(topicsInfo.isEmpty()) {
            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof TopicsGridFragment){
                EmptyFragment noTopicsFragments = new EmptyFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,noTopicsFragments,Tags.EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                EmptyFragment noTopicsFragments = new EmptyFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,noTopicsFragments,Tags.EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        else {
            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof EmptyFragment){
                TopicsGridFragment topicsFragment = new TopicsGridFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,topicsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                TopicsGridFragment topicsFragment = new TopicsGridFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,topicsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        startActivity(new Intent(TopicsActivity.this, TransparentActivity.class));
    }

    public void updateData(){
        try {
        topicsInfo = Topic.find(Topic.class, "dictionary_ID = "+currentDictionaryID +"");
    } catch (Exception e){
        topicsInfo = new ArrayList<Topic>();
        }
    }

    public void updateViewData(){
        topicsGrid = (GridView) findViewById(R.id.topicsGridView);
        topicsGrid.setAdapter(new TopicsGridViewAdapter(this, topicsInfo));
        ((BaseAdapter)topicsGrid.getAdapter()).notifyDataSetChanged();
    }

    public long getCurrentDictionaryID() {
        return currentDictionaryID;
    }
    public List<Topic> getActivityData() {
        return topicsInfo;
    }
    public FragmentManager getActivityFragmentManager() {
        return fragmentManager;
    }
}
