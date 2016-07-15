package activities;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Dialog.TestStartDialog;
import Fragments.EmptyFragment;
import Fragments.WordsListFragment;
import Helpers.ActivityDataInterface;
import Models.Tags;
import Models.Word;

/**
 * Created by XPS on 4/25/2016.
 */
public class WordsActivity extends AppCompatActivity implements ActivityDataInterface {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private WordsListFragment wordsFragment;
    private RecyclerView wordsView;
    private List<Integer> shapeColors;

    public static List<Word> wordsInfo = new ArrayList<Word>();;

    public static long currentTopicId;
    private String currentTopicName;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(currentTopicName);

        currentTopicId = getIntent().getLongExtra(Tags.TOPIC_TAG,0);
        currentTopicName = getIntent().getStringExtra(Tags.TOPIC_NAME_TAG);
        Log.e("TOPIC ID ",currentTopicId + "");
        fragmentManager = getSupportFragmentManager();
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
                if (wordsInfo.size() > 8){
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
         outState.putSerializable(Tags.MATERIAL_COLORS, (Serializable) shapeColors);
         getSupportFragmentManager().putFragment(outState,Tags.WORDS_FRAGMENT,wordsFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        try{
            shapeColors = (ArrayList<Integer>) savedInstanceState
                                    .getSerializable(Tags.MATERIAL_COLORS);
            wordsFragment = (WordsListFragment) getSupportFragmentManager()
                                .getFragment(savedInstanceState,Tags.WORDS_FRAGMENT);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    //@Override //TODO
    public void loadAppropriateFragment(List<Word> dbWords){
    wordsInfo = dbWords;
        if(wordsInfo.isEmpty()) {

            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof WordsListFragment){
                EmptyFragment emptyFragment = new EmptyFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,emptyFragment, Tags.EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                EmptyFragment emptyFragment = new EmptyFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer, emptyFragment, Tags.EMPTY_LIST_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        else {
            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof EmptyFragment){
                if (wordsFragment == null)
                    wordsFragment = new WordsListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,wordsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                wordsFragment = new WordsListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,wordsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            wordsView = (RecyclerView)findViewById(R.id.wordsRecyclerView);
        }
    }

    @Override
    public void updateData() {
      new LoadWords().execute(currentTopicId);
    }

    @Override
    public void updateViewData() {
        if(wordsView != null)

        wordsFragment.getAdapter().notifyDataSetChanged();
    }

    @Override
    public List<Word> getActivityData() {
        return wordsInfo;
    }
    @Override
    public FragmentManager getActivityFragmentManager() {
        return fragmentManager;
    }

    public int getMaterialColor(int position){

        if(shapeColors != null && wordsInfo.size() <= shapeColors.size()) {
            return shapeColors.get(position);
        } else {
            if(wordsInfo.size() > 0){
                if(shapeColors == null)
                    shapeColors = new ArrayList<>();
                addMatColor(shapeColors,wordsInfo.size());
                return shapeColors.get(position);
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
                List<Word> wordsFound = Word.find(Word.class, "topic_ID = "+currentTopicId +"");
                wordsInfo.clear();
                wordsInfo.addAll(wordsFound);
            } catch (Exception e){
                wordsInfo = new ArrayList<Word>();
            }
            return wordsInfo;
        }

        @Override
        protected void onPostExecute(List<Word> wordList) {
            super.onPostExecute(wordList);

            loadAppropriateFragment(wordList);
            updateViewData();
        }
    }




}
