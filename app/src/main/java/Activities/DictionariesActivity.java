package activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.DictionariesListViewAdapter;
import Fragments.DictionariesListFragment;
import Fragments.EmptyFragment;
import Helpers.ActivityDataInterface;
import Models.Dictionary;
import Models.Tags;
import Models.Topic;

public class DictionariesActivity extends AppCompatActivity implements ActivityDataInterface {

    private  FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListView dictionariesList;
    private DictionariesListViewAdapter adapter;
    private static List<Dictionary> dictionariesInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.vocabularies_title);

        //Dictionary.deleteAll(Dictionary.class);
        fragmentManager = getFragmentManager();
        updateData();
        loadAppropriateFragment();
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
                adapter = new DictionariesListViewAdapter(this, dictionariesInfo);
                dictionariesFragment.setListAdapter(adapter);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();

//                if(f != null && f instanceof DictionariesListFragment){
//                    dictionariesList = (ListView) findViewById(android.R.id.list);
//                    adapter = new DictionariesListViewAdapter(this, dictionariesInfo);
//                    dictionariesList.setAdapter(adapter);}
            }
            if(f == null ){
                DictionariesListFragment dictionariesFragment = new DictionariesListFragment();
                adapter = new DictionariesListViewAdapter(this, dictionariesInfo);
                dictionariesFragment.setListAdapter(adapter);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
                }
            //startActivity(new Intent(DictionariesActivity.this, TransparentActivity.class));
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
}
