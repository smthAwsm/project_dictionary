package activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.study.xps.projectdictionary.R;

import adapters.DictionariesListViewAdapter;
import adapters.WordsRecyclingViewAdapter;
import fragments.DictionariesListFragment;
import fragments.EmptyFragment;
import fragments.WordsRecyclerListFragment;
import helpers.GlobalStorage;
import models.Tags;
import models.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 4/25/2016.
 */
public class WordsActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private WordsRecyclerListFragment mWordsFragment;
    private List<Integer> mShapeColors;
    private String mCurrentTopicName;
    private GlobalStorage mGlobalStorage;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);

        mCurrentTopicName = getIntent().getStringExtra(Tags.TOPIC_NAME_TAG);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(mCurrentTopicName);

        mGlobalStorage = GlobalStorage.getStorage();
        mFragmentManager = getSupportFragmentManager();
        updateData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mWordsFragment.updateWordsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.words_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_word_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mWordsFragment.getAdapter().filter(query,mGlobalStorage);
                mWordsFragment.getWordsRecyclerView().scrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mWordsFragment.getAdapter().filter(newText,mGlobalStorage);
                mWordsFragment.getWordsRecyclerView().scrollToPosition(0);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(settingsIntent);
                    return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
         outState.putSerializable(Tags.MATERIAL_COLORS, (Serializable) mShapeColors);
         getSupportFragmentManager().putFragment(outState,Tags.WORDS_FRAGMENT, mWordsFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        try{
            mShapeColors = (ArrayList<Integer>) savedInstanceState
                                    .getSerializable(Tags.MATERIAL_COLORS);
            mWordsFragment = (WordsRecyclerListFragment) getSupportFragmentManager()
                                .getFragment(savedInstanceState,Tags.WORDS_FRAGMENT);
        } catch(Exception e){
            e.printStackTrace();
        }
    }*/

    public void loadAppropriateFragment(){
        if(mGlobalStorage.getWordsData().isEmpty()) {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof WordsRecyclerListFragment){
                EmptyFragment emptyFragment = new EmptyFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        emptyFragment, Tags.EMPTY_LIST_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                EmptyFragment emptyFragment = new EmptyFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        emptyFragment, Tags.EMPTY_LIST_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        } else {
            Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof EmptyFragment){
                if (mWordsFragment == null)
                    mWordsFragment = new WordsRecyclerListFragment();
                    mWordsFragment.setRetainInstance(true);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        mWordsFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                mWordsFragment = new WordsRecyclerListFragment();
                mWordsFragment.setRetainInstance(true);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        mWordsFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
    }

    public void updateData() {
        new LoadWords().execute(mGlobalStorage.getCurrentTopicId());
    }

    public void updateViewData() {
        Fragment f = mFragmentManager.findFragmentById(R.id.mainFragmentContainer);
        if( f != null && f instanceof WordsRecyclerListFragment){
            WordsRecyclingViewAdapter adapter = ((WordsRecyclerListFragment)f).getAdapter();
            if(adapter != null){
                adapter.notifyDataSetChanged();
            } else try {
                if(f != null && f instanceof WordsRecyclerListFragment){
                    ((WordsRecyclerListFragment)f).setupWordsRecycler(f.getView());
                    updateViewData();
                }
            } catch (NullPointerException e) {e.printStackTrace();}
        }
    }


    public FragmentManager getActivityFragmentManager() { return mFragmentManager; }

    public int getMaterialColor(int position){
        int wordsListSize = mGlobalStorage.getWordsData().size();
        if(mShapeColors != null && wordsListSize <= mShapeColors.size()) {
            return mShapeColors.get(position);
        } else {
            if(wordsListSize > 0){
                if(mShapeColors == null)
                    mShapeColors = new ArrayList<>();
                addMatColor(mShapeColors, wordsListSize);
                return mShapeColors.get(position);
            }
        }
        return Color.WHITE;
    }

    private void addMatColor(List<Integer> resultArray, int colorsNumber)
    {
        int arrayId = getResources().getIdentifier("mdcolors" , "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);

            if (resultArray.size() ==  0){
                for (int i = 0; i < colorsNumber; i++){
                    int index = (int) (Math.random() * colors.length());
                    resultArray.add(colors.getColor(index, Color.BLACK));
                }
            }
            else {
                int colorsToGenerate = colorsNumber - resultArray.size();
                if(colorsToGenerate !=0 ) {
                    List<Integer> temp  = new ArrayList<Integer>();

                    for (int i = 0; i < colorsToGenerate; i++){
                        int index = (int) (Math.random() * colors.length());
                        temp.add(colors.getColor(index, Color.BLACK));
                    }
                    resultArray.addAll(temp);
                }
            }
            colors.recycle();
        }
    }

    class LoadWords extends AsyncTask<Long,Void,List<Word>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Word> doInBackground(Long... params) {
            mGlobalStorage.updateWordsData();
            List<Word> wordsData = mGlobalStorage.getWordsData();
            return wordsData;
        }

        @Override
        protected void onPostExecute(List<Word> wordList) {
            super.onPostExecute(wordList);
            loadAppropriateFragment();
            WordsRecyclingViewAdapter adapter = mWordsFragment.getAdapter();
            if (adapter != null) {
                adapter.updateWordAdapterData();
            }
            updateViewData();
        }
    }
}
