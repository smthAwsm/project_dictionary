package activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.WordsListViewAdapter;
import Dialog.NewWordDialog;
import Dialog.TestStartDialog;
import Fragments.EmptyFragment;
import Fragments.WordsListFragment;
import Models.Dictionary;
import Models.Tags;
import Models.Word;

/**
 * Created by XPS on 4/25/2016.
 */
public class WordsActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListView wordsList;
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

        try {
            wordsInfo = Word.find(Word.class, "topic_ID = "+currentTopicId +"");
        } catch (Exception e){
            wordsInfo = new ArrayList<Word>();
        }

        //wordsInfo = Word.listAll(Word.class);
        loadAppropriateFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);

        if(f != null && f instanceof EmptyFragment)
            addEmptyListListeners();

        if(f != null && f instanceof WordsListFragment){
            addWordListListeners();

            updateListData();}
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
                DialogFragment testDialog = new TestStartDialog();
                testDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



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

    private void createDictionaryAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_dic_title));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.save();

                wordsInfo = Dictionary.listAll(Word.class);
                loadAppropriateFragment();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void addWordListListeners(){

        wordsList = (ListView) findViewById(R.id.wordsListView);
        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("asd",position+"");


                Toast.makeText(getApplicationContext(),position +"", Toast.LENGTH_SHORT);
                //WordsListViewAdapter adapter = (WordsListViewAdapter) parent.getAdapter();
                //Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),TopicsActivity.class);
                //dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,wordsInfo.get(position).getId());
                //startActivity(dictionaryTopicsIntent);
            }
        });



        FloatingActionButton addDictionaryButton = (FloatingActionButton) findViewById(R.id.addNewWordFAB);
        addDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
            }
        });
    }
    private void addEmptyListListeners(){

        ImageView addWordImage = (ImageView) findViewById(R.id.addVocabularyImage);
        TextView addDictionaryLabel = (TextView) findViewById(R.id.noVocabulariesText);
        addDictionaryLabel.setText(getString(R.string.no_words));

        addWordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
            }
        });
        addDictionaryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
            }
        });
    }

    public void updateListData() {
        shapesColor = getMatColor();
        wordsList = (ListView) findViewById(R.id.wordsListView);
        WordsListViewAdapter wordsListAdapter = new WordsListViewAdapter(this, wordsInfo,shapesColor);
        //wordsListAdapter.setMatColor();
        wordsList.setAdapter(wordsListAdapter);
        ((BaseAdapter) wordsList.getAdapter()).notifyDataSetChanged();
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
