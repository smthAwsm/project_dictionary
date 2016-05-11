package activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.WordsRecyclingViewAdapter;
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
    private RecyclerView wordsList;
    private int[] shapesColor;
    public static List<Word> wordsInfo;

    public static long currentTopicId;
    private String currentTopicName;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        currentTopicId = getIntent().getLongExtra(Tags.TOPIC_TAG,0);
        currentTopicName = getIntent().getStringExtra(Tags.TOPIC_NAME_TAG);
        supportActionBar.setTitle(currentTopicName);
        Log.e("QWER",currentTopicId + "");

        fragmentManager = getFragmentManager();

        //wordsInfo = new ArrayList<Word>();
        //for (int i = 0; i < 15; i++ )
        //wordsInfo.add(new Word(,"ггг","gtgt"));

        //Word.deleteAll(Word.class);

        updateData();
        loadAppropriateFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);

        //if(f != null && f instanceof WordsListFragment)
        //     updateViewData();
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
                testDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);}
                else Toast.makeText(getApplicationContext(),getString(R.string.more_words),Toast.LENGTH_SHORT).show();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void loadAppropriateFragment(){

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
                WordsListFragment wordsFragment = new WordsListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,wordsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                WordsListFragment wordsFragment = new WordsListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,wordsFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            startActivity(new Intent(WordsActivity.this, TransparentActivity.class));
        }
    }

    @Override
    public void updateData() {
        try {
            wordsInfo = Word.find(Word.class, "topic_ID = "+currentTopicId +"");
        } catch (Exception e){
            wordsInfo = new ArrayList<Word>();
        }
    }
    @Override
    public void updateViewData() {
        shapesColor = getMatColor();
        wordsList = (RecyclerView) findViewById(R.id.wordsRecyclerView);
        WordsRecyclingViewAdapter viewAdapter = new WordsRecyclingViewAdapter(wordsInfo,this,shapesColor);
        wordsList.setAdapter(viewAdapter);
        wordsList.setLayoutManager(new LinearLayoutManager(this));
        //WordsListViewAdapter wordsListAdapter = new WordsListViewAdapter(this, wordsInfo,shapesColor);
        //((BaseAdapter) wordsList.getAdapter()).notifyDataSetChanged();
        //wordsListAdapter.setMatColor();
    }

    @Override
    public List<Word> getActivityData() {
        return wordsInfo;
    }
    @Override
    public FragmentManager getActivityFragmentManager() {
        return fragmentManager;
    }

    private int[] getMatColor()
    {
        int[] returnColor = new int[]{ Color.BLACK };
        int arrayId = getResources().getIdentifier("mdcolors" , "array", getPackageName());

        if (arrayId != 0)
        {
            //if (shapesColor == null){
            returnColor = new int[wordsInfo.size()+1];
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            for (int i = 0; i < wordsInfo.size()+1; i++){
                int index = (int) (Math.random() * colors.length());
                returnColor[i] = colors.getColor(index, Color.BLACK);
            //}
            //colors.recycle();
       }
// else {
//                returnColor = new int[wordsInfo.size()];
//                TypedArray colors = getResources().obtainTypedArray(arrayId);
//                for (int i = 0; i < wordsInfo.size() - shapesColor.length; i++){
//                    int index = (int) (Math.random() * colors.length());
//                    returnColor[shapesColor.length + i] = colors.getColor(index, Color.BLACK);
//                }
//            }
        }
        return returnColor;
    }
}
