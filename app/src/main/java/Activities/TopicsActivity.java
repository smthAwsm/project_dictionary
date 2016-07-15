package activities;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Fragments.EmptyFragment;
import Fragments.TopicsGridFragment;
import Helpers.ActivityDataInterface;
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
    private TopicsGridFragment topicsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));

        topicsInfo = new ArrayList<Topic>();
        currentDictionaryID = getIntent().getLongExtra(Tags.DICTIONARY_TAG,0);
        fragmentManager = getSupportFragmentManager();
        updateData();

    }

    public void loadAppropriateFragment(List<Topic> dbData){
        topicsInfo = dbData;
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
                if (topicsFragment == null)
                    topicsFragment = new TopicsGridFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,topicsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                topicsFragment = new TopicsGridFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,topicsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            topicsView = (RecyclerView)findViewById(R.id.topicsRecyclerView);
        }
    }

    public void updateData(){
        new LoadTopics().execute(currentDictionaryID);
    }

    public void updateViewData(){
        if(topicsView != null ){
            topicsView.getAdapter().notifyDataSetChanged();
        }
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


    class LoadTopics extends AsyncTask<Long,Void,List<Topic>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Topic> doInBackground(Long... params) {

            try {
                List <Topic> topicsFound = Topic.find(Topic.class, "dictionary_ID = " + currentDictionaryID +"");
                topicsInfo.clear();
                topicsInfo.addAll(topicsFound);
            } catch (Exception e){
                topicsInfo = new ArrayList<Topic>();
            }
            return topicsInfo;
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);

            loadAppropriateFragment(topics);
            updateViewData();
        }
    }
}
