package activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Adapters.DictionariesListViewAdapter;
import Dialog.RUDDictionaryDialog;
import Fragments.DictionaryListFragment;
import Fragments.EmptyFragment;
import Models.Dictionary;
import Models.Tags;

public class DictionariesActivity extends AppCompatActivity {

    private  FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListView dictionariesList;
    private static List<Dictionary> dictionariesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.vocabularies_title);


        fragmentManager = getFragmentManager();
        //Dictionary.deleteAll(Dictionary.class);
        dictionariesInfo = Dictionary.listAll(Dictionary.class);
        loadAppropriateFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);

        if(f != null && f instanceof EmptyFragment)
            addEmptyListListeners();

        if(f != null && f instanceof DictionaryListFragment){
         addDictionaryListListeners();
        updateListData();}
    }


    public void loadAppropriateFragment(){

        if(dictionariesInfo.isEmpty()) {

            Fragment f = fragmentManager.findFragmentById(R.id.mainFragmentContainer);
            if(f != null && f instanceof DictionaryListFragment){
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
                DictionaryListFragment dictionariesFragment = new DictionaryListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer,dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            if(f == null ){
                DictionaryListFragment dictionariesFragment = new DictionaryListFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainFragmentContainer,dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
                }
            startActivity(new Intent(DictionariesActivity.this, TransparentActivity.class));
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

                dictionariesInfo = Dictionary.listAll(Dictionary.class);
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

    private void addDictionaryListListeners(){

            dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
            dictionariesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DictionariesListViewAdapter adapter = (DictionariesListViewAdapter) parent.getAdapter();
                    Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),TopicsActivity.class);
                    dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,dictionariesInfo.get(position).getId());
                    startActivity(dictionaryTopicsIntent);
                }
            });

            dictionariesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                RUDDictionaryDialog rudDialog = new RUDDictionaryDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.DICTIONARY_TAG, dictionariesInfo.get(pos).getId());
                rudDialog.setArguments(bundle);
                rudDialog.show(fragmentManager,"RUD");
                Log.d("DIALOG", "TOUCHED" );
                return true;
            }
        });


            FloatingActionButton addDictionaryButton = (FloatingActionButton) findViewById(R.id.addDictionaryFAB);
            addDictionaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDictionaryAlert();
                }
            });
    }
    private void addEmptyListListeners(){

        ImageView addDictionaryImage = (ImageView) findViewById(R.id.addVocabularyImage);
        TextView addDictionaryLabel = (TextView) findViewById(R.id.noVocabulariesText);

        addDictionaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDictionaryAlert();
            }
        });
        addDictionaryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDictionaryAlert();
            }
        });
    }
    public void updateListData(){
        dictionariesList = (ListView) findViewById(R.id.dictionariesListView);
        dictionariesList.setAdapter(new DictionariesListViewAdapter(this, dictionariesInfo));
        ((BaseAdapter)dictionariesList.getAdapter()).notifyDataSetChanged();
    }

        public void updateData(){
            dictionariesInfo = Dictionary.listAll(Dictionary.class);
        }

}
