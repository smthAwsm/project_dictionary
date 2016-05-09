package activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.TopicsRecyclingGridViewAdapter;
import Fragments.EmptyFragment;
import Fragments.TopicsGridFragment;
import Helpers.ActivityDataInterface;
import Helpers.GridAutoFitLayoutManager;
import Models.Tags;
import Models.Topic;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsActivity extends AppCompatActivity implements ActivityDataInterface{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RecyclerView topicsView;
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
        topicsView = (RecyclerView) findViewById(R.id.topicsRecyclerView);
        topicsView.setAdapter(new TopicsRecyclingGridViewAdapter(topicsInfo,this));
        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(this,400);
        topicsView.setLayoutManager(layoutManager);
        // ((BaseAdapter) topicsView.getAdapter()).notifyDataSetChanged();
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
