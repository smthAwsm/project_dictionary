package activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Adapters.DictionariesListViewAdapter;
import Fragments.DictionariesListFragment;
import Fragments.EmptyFragment;
import Helpers.ActivityDataInterface;
import Models.Dictionary;
import Models.Tags;

public class DictionariesActivity extends AppCompatActivity implements ActivityDataInterface {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DictionariesListViewAdapter adapter;
    private static List<Dictionary> dictionariesInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.vocabularies_title);

        fragmentManager = getSupportFragmentManager();
        updateData();
        loadAppropriateFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    if(adapter != null)
        adapter.notifyDataSetChanged();
    }

    public void loadAppropriateFragment(){

        if(dictionariesInfo.isEmpty()) {
            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof DictionariesListFragment){
                EmptyFragment noDictionariesFragments = new EmptyFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,noDictionariesFragments, Tags.EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                EmptyFragment noDictionariesFragments = new EmptyFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,noDictionariesFragments, Tags.EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        else {
            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof EmptyFragment){
                DictionariesListFragment dictionariesFragment = new DictionariesListFragment();
                dictionariesFragment.setRetainInstance(true);
                adapter = new DictionariesListViewAdapter(this, dictionariesInfo);
                dictionariesFragment.setListAdapter(adapter);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                DictionariesListFragment dictionariesFragment = new DictionariesListFragment();
                dictionariesFragment.setRetainInstance(true);
                adapter = new DictionariesListViewAdapter(this, dictionariesInfo);
                dictionariesFragment.setListAdapter(adapter);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
                }
        }
    }

    public void updateData(){
        dictionariesInfo = Dictionary.listAll(Dictionary.class);
        }

    @Override
    public  void updateViewData() {
        adapter.clear();
        adapter.addAll(dictionariesInfo);
        adapter.notifyDataSetChanged();
    }

    public List<Dictionary> getActivityData(){
            return dictionariesInfo;
        }
    public FragmentManager getActivityFragmentManager() {
        return fragmentManager;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable(Tags.VOCABULARY_ADAPTER, adapter);
        //getSupportFragmentManager().putFragment(outState,Tags.WORDS_FRAGMENT,wordsFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //dictionariesInfo.size();

        try{
            adapter = (DictionariesListViewAdapter) savedInstanceState
                    .getSerializable(Tags.VOCABULARY_ADAPTER);
            int i = 0;
//            wordsFragment = (WordsListFragment) getSupportFragmentManager()
//                    .getFragment(savedInstanceState,Tags.WORDS_FRAGMENT);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
