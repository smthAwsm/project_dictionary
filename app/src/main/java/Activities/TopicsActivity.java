package activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import fragments.TopicsRecyclerGridFragment;
import helpers.GlobalStorage;
import models.Language;
import models.Tags;
import models.Topic;

import java.util.List;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private TopicsRecyclerGridFragment mTopicsRecyclerGridFragment;
    private RecyclerView mTopicsView;
    private GlobalStorage mGlobalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.topics_title));

        mGlobalStorage = GlobalStorage.getStorage();
        if(mGlobalStorage.getLanguagesList().size() == 0){
            mGlobalStorage.loadSupportedLanguagesList(this);
        }

        long currentDictionaryID = getIntent().getLongExtra(Tags.DICTIONARY_TAG,0);
        mGlobalStorage.setCurrentDictionaryID(currentDictionaryID);
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
        if(dbData.isEmpty()) {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof TopicsRecyclerGridFragment){
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
                if (mTopicsRecyclerGridFragment == null)
                    mTopicsRecyclerGridFragment = new TopicsRecyclerGridFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        mTopicsRecyclerGridFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                mTopicsRecyclerGridFragment = new TopicsRecyclerGridFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        mTopicsRecyclerGridFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            mTopicsView = (RecyclerView)findViewById(R.id.topicsRecyclerView);
        }
    }

    public void updateData(){
        new LoadTopics().execute(mGlobalStorage.getCurrentDictionaryID());
    }

    public void updateViewData(){
        if(mTopicsView != null ){
            mTopicsView.getAdapter().notifyDataSetChanged();
        }
    }

    public FragmentManager getActivityFragmentManager() {
        return mFragmentManager;
    }

    class LoadTopics extends AsyncTask<Long,Void,List<Topic>> {
        @Override
        protected List<Topic> doInBackground(Long... params) {
            mGlobalStorage.updateTopicsData(mGlobalStorage.getCurrentDictionaryID());
            return mGlobalStorage.getTopicsData();
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);
            loadAppropriateFragment(topics);
            updateViewData();
        }
    }
}
