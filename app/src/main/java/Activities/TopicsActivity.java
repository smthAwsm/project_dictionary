package activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.study.xps.projectdictionary.R;
import fragments.EmptyFragment;
import fragments.TopicsGridFragment;
import helpers.ActivityDataInterface;
import models.Tags;
import models.Topic;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsActivity extends AppCompatActivity implements ActivityDataInterface{

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private TopicsGridFragment mTopicsGridFragment;
    private RecyclerView mTopicsView;
    private List<Topic> mTopicList;
    private long mCurrentDictionaryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));

        mTopicList = new ArrayList<>();
        mCurrentDictionaryID = getIntent().getLongExtra(Tags.DICTIONARY_TAG,0);
        mFragmentManager = getSupportFragmentManager();
        updateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_current_dictionary:
                finish();
                startActivity(new Intent(getApplicationContext(),DictionariesActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadAppropriateFragment(List<Topic> dbData){
        mTopicList = dbData;
        if(mTopicList.isEmpty()) {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof TopicsGridFragment){
                EmptyFragment noTopicsFragments = new EmptyFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        noTopicsFragments,Tags.EMPTY_LIST_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                EmptyFragment noTopicsFragments = new EmptyFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        noTopicsFragments,Tags.EMPTY_LIST_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        } else {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof EmptyFragment){
                if (mTopicsGridFragment == null)
                    mTopicsGridFragment = new TopicsGridFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        mTopicsGridFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                mTopicsGridFragment = new TopicsGridFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        mTopicsGridFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            mTopicsView = (RecyclerView)findViewById(R.id.topicsRecyclerView);
        }
    }

    public void updateData(){
        new LoadTopics().execute(mCurrentDictionaryID);
    }

    public void updateViewData(){
        if(mTopicsView != null ){
            mTopicsView.getAdapter().notifyDataSetChanged();
        }
    }

    public long getCurrentDictionaryID() {
        return mCurrentDictionaryID;
    }
    public List<Topic> getActivityData() {
        return mTopicList;
    }
    public FragmentManager getActivityFragmentManager() {
        return mFragmentManager;
    }

    class LoadTopics extends AsyncTask<Long,Void,List<Topic>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Topic> doInBackground(Long... params) {

            try {
                List <Topic> topicsFound = Topic.find(Topic.class,
                        "dictionary_ID = " + mCurrentDictionaryID +"");
                mTopicList.clear();
                mTopicList.addAll(topicsFound);
            } catch (Exception e){
                mTopicList = new ArrayList<Topic>();
            }
            return mTopicList;
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);

            loadAppropriateFragment(topics);
            updateViewData();
        }
    }
}
