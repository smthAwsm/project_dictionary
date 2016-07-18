package activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.study.xps.projectdictionary.R;
import adapters.DictionariesListViewAdapter;
import fragments.DictionariesListFragment;
import fragments.EmptyFragment;
import helpers.ActivityDataInterface;
import models.Dictionary;
import models.Tags;

import java.util.List;

public class DictionariesActivity extends AppCompatActivity
        implements ActivityDataInterface {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private DictionariesListViewAdapter mListViewAdapter;
    private static List<Dictionary> sDictionariesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.dictionaries_title);

        mFragmentManager = getSupportFragmentManager();
        updateData();
        loadAppropriateFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mListViewAdapter != null){
            mListViewAdapter.notifyDataSetChanged();
        }
    }

    public void loadAppropriateFragment(){
        if(sDictionariesInfo.isEmpty()) {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof DictionariesListFragment){
                EmptyFragment noDictionariesFragments = new EmptyFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        noDictionariesFragments, Tags.EMPTY_LIST_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                EmptyFragment noDictionariesFragments = new EmptyFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        noDictionariesFragments, Tags.EMPTY_LIST_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        } else {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof EmptyFragment){
                DictionariesListFragment dictionariesFragment = new DictionariesListFragment();
                dictionariesFragment.setRetainInstance(true);
                mListViewAdapter = new DictionariesListViewAdapter(this, sDictionariesInfo);
                dictionariesFragment.setListAdapter(mListViewAdapter);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                DictionariesListFragment dictionariesFragment = new DictionariesListFragment();
                dictionariesFragment.setRetainInstance(true);
                mListViewAdapter = new DictionariesListViewAdapter(this, sDictionariesInfo);
                dictionariesFragment.setListAdapter(mListViewAdapter);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
    }

    @Override
    public  void updateViewData() {
        mListViewAdapter.clear();
        mListViewAdapter.addAll(sDictionariesInfo);
        mListViewAdapter.notifyDataSetChanged();
    }

    public void updateData(){
        sDictionariesInfo = Dictionary.listAll(Dictionary.class);
    }

    public List<Dictionary> getActivityData(){
            return sDictionariesInfo;
        }

    public FragmentManager getActivityFragmentManager() {
        return mFragmentManager;
    }
}
