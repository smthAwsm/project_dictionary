package com.study.xps.projectdictionary.Activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.study.xps.projectdictionary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Adapters.DictionariesListViewAdapter;
import Fragments.DictionaryListFragment;
import Fragments.NoVocabulariesFragments;
import Models.Dictionary;
import Models.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_EMPTY = "EMPTY";
    private static final String TAG = "GOT";
    private static final String LOG_TAG = "DATABASE LOGING";


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListView dictionariesList;
    private ArrayList<Integer> dictionariesInfo;

    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularies_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.vocabularies_title);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
//
//
//
//
//        Log.d(LOG_TAG,"############ Table inserting ############");
//
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = df.format(c.getTime());
//
//        contentValues.put("name","Dictionary 1");
//        contentValues.put("created_date", formattedDate);
//
//        long rowID = db.insert("Dictionaries",null,contentValues);
//
//        Log.d(LOG_TAG,"############ ROW " + rowID+" inserted ############");
//
        Cursor cursorDictionaries = db.query("Dictionaries",null,null,null,null,null,null);
        if(cursorDictionaries.moveToFirst()){
         int idColIndex = cursorDictionaries.getColumnIndex("id");
         int nameColIndex = cursorDictionaries.getColumnIndex("name");
         int dateColIndex = cursorDictionaries.getColumnIndex("created_date");

            do{
                Log.d(LOG_TAG,"ID " + cursorDictionaries.getInt(idColIndex) +
                        ", Name " + cursorDictionaries.getString(nameColIndex) +
                        ", DATE " + cursorDictionaries.getString(dateColIndex));

            }while (cursorDictionaries.moveToNext());

            //int clearedCount = db.delete("Dictionaries",null,null);
           // Log.e(LOG_TAG,"Deleted Rows " + clearedCount);
        } else Log.e(LOG_TAG,"0 Rows ");
        db.close();


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if(false) {
            NoVocabulariesFragments noVocabulariesFragments = new NoVocabulariesFragments();
            fragmentTransaction.add(R.id.vocabulariesFragmentContainer, noVocabulariesFragments, TAG_EMPTY);
            fragmentTransaction.commit();
        } else {
            dictionariesInfo = new ArrayList<Integer>(){{
                add(23);
                add(89);
                add(110);
                add(9999);
            }};
            DictionaryListFragment dictionaryFragment = new DictionaryListFragment();
            fragmentTransaction.add(R.id.vocabulariesFragmentContainer,dictionaryFragment,TAG);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(fragmentManager.findFragmentByTag(TAG)!= null){
        dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
        dictionariesList.setAdapter(new DictionariesListViewAdapter(this, dictionariesInfo));

            FloatingActionButton addDictionaryButton = (FloatingActionButton) findViewById(R.id.addDictionaryFAB);
            addDictionaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDictionaryAlert();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictionaries_menu, menu);
        return true;
    }




    private void createDictionaryAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_dic_title));

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.writeToDB(getApplicationContext());
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


}
