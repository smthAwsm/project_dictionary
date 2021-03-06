package activities;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;
import dialogs.TestStartDialog;
import fragments.EmptyFragment;
import fragments.WordsRecyclerListFragment;
import helpers.ActivityDataInterface;
import models.Tags;
import models.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by XPS on 4/25/2016.
 */
public class WordsActivity extends AppCompatActivity implements ActivityDataInterface {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private WordsRecyclerListFragment mWordsFragment;
    private RecyclerView mWordsRecyclerViewView;
    private List<Integer> mShapeColors;
    private String mCurrentTopicName;

    public static List<Word> sWordList = new ArrayList<>();;
    public static long sCurrentTopicId;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);

        mCurrentTopicName = getIntent().getStringExtra(Tags.TOPIC_NAME_TAG);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(mCurrentTopicName);

        sCurrentTopicId = getIntent().getLongExtra(Tags.TOPIC_TAG,0);
        mFragmentManager = getSupportFragmentManager();
        updateData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.words_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_test:
                if (sWordList.size() > 8){
                DialogFragment testDialog = new TestStartDialog();
                testDialog.show(getSupportFragmentManager(), Tags.NEW_WORD_DIALOG);}
                else Toast.makeText(getApplicationContext(),
                                    getString(R.string.more_words),Toast.LENGTH_SHORT).show();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
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
    }

    public void loadAppropriateFragment(List<Word> dbWords){
    sWordList = dbWords;
        if(sWordList.isEmpty()) {

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
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.mainFragmentContainer,
                        mWordsFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                mWordsFragment = new WordsRecyclerListFragment();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        mWordsFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            mWordsRecyclerViewView = (RecyclerView)findViewById(R.id.wordsRecyclerView);
        }
    }

    @Override
    public void updateData() {
      new LoadWords().execute(sCurrentTopicId);
    }

    @Override
    public void updateViewData() {
        if(mWordsRecyclerViewView != null){
            mWordsFragment.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public List<Word> getActivityData() { return sWordList; }

    @Override
    public FragmentManager getActivityFragmentManager() { return mFragmentManager; }

    public int getMaterialColor(int position){
        if(mShapeColors != null && sWordList.size() <= mShapeColors.size()) {
            return mShapeColors.get(position);
        } else {
            if(sWordList.size() > 0){
                if(mShapeColors == null)
                    mShapeColors = new ArrayList<>();
                addMatColor(mShapeColors, sWordList.size());
                return mShapeColors.get(position);
            }
        }
        return Color.WHITE;
    }

    private void addMatColor(List<Integer> resultArray, int colorsNumber)
    {
        int arrayId = getResources().getIdentifier("mdcolors" , "array", getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = getResources().obtainTypedArray(arrayId);

            if (resultArray.size() ==  0){
                //resultArray = new ArrayList<>();
                for (int i = 0; i < colorsNumber; i++){
                    int index = (int) (Math.random() * colors.length());
                    resultArray.add(colors.getColor(index, Color.BLACK));
                }
            }
            else {
                int diff = colorsNumber - resultArray.size();
                if(diff !=0 ) {
                    List<Integer> temp  = new ArrayList<Integer>();

                    for (int i = 0; i < diff; i++){
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
            try {
                List<Word> wordsFound = Word.find(Word.class, "topic_ID = "+ sCurrentTopicId +"");
                sWordList.clear();
                sWordList.addAll(wordsFound);
            } catch (Exception e){
                sWordList = new ArrayList<Word>();
            }
            return sWordList;
        }

        @Override
        protected void onPostExecute(List<Word> wordList) {
            super.onPostExecute(wordList);

            loadAppropriateFragment(wordList);
            updateViewData();
        }
    }
}
