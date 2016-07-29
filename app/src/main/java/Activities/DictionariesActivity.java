package activities;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.study.xps.projectdictionary.R;
import adapters.DictionariesListViewAdapter;
import adapters.DictionaryLanguageSpinnerAdapter;
import fragments.DictionariesListFragment;
import fragments.EmptyFragment;
import helpers.ActivityDataInterface;
import helpers.GlobalStorage;
import models.Dictionary;
import models.Language;
import models.Tags;

import java.util.List;

public class DictionariesActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private DictionariesListViewAdapter mListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_layout);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(R.string.dictionaries_title);

        mFragmentManager = getSupportFragmentManager();
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
        GlobalStorage mGlobalStorage = GlobalStorage.getStorage();
        mGlobalStorage.updateDictionariesData();
        if(mGlobalStorage.getDictionariesData().isEmpty()) {
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
                mListViewAdapter = new DictionariesListViewAdapter(this,
                        mGlobalStorage.getDictionariesData());
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
                mListViewAdapter = new DictionariesListViewAdapter(this,
                        mGlobalStorage.getDictionariesData());
                dictionariesFragment.setListAdapter(mListViewAdapter);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.mainFragmentContainer,
                        dictionariesFragment, Tags.SUCCESS_QUERY_TAG);
                mFragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
    }

    public  void updateViewData() {
        GlobalStorage mGlobalStorage = GlobalStorage.getStorage();

        if (mListViewAdapter != null) {
            mListViewAdapter.clear();
            mGlobalStorage.updateDictionariesData();
            mListViewAdapter.addAll(mGlobalStorage.getDictionariesData());
            mListViewAdapter.notifyDataSetChanged();
        }
    }

    public FragmentManager getActivityFragmentManager() {
        return mFragmentManager;
    }

    public void createDictionaryAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.new_dict_title));
        View dialogView = LayoutInflater.from(this).
                inflate(R.layout.dialog_dictionary_add,null);
        final EditText input = (EditText) dialogView.findViewById(R.id.dictionaryNameText);

        final List<Language> languageList = GlobalStorage.getStorage().getLanguagesList(this);
        DictionaryLanguageSpinnerAdapter spinnerFromAdapter =
                new DictionaryLanguageSpinnerAdapter(this,languageList);
        final Spinner languageFromSpinner = (Spinner) dialogView.
                findViewById(R.id.languageFromSpinner);
        languageFromSpinner.setAdapter(spinnerFromAdapter);

        DictionaryLanguageSpinnerAdapter spinnerToAdapter =
                new DictionaryLanguageSpinnerAdapter(this,languageList);
        final Spinner translationToSpinner = (Spinner) dialogView.
                findViewById(R.id.languageToSpinner);
        translationToSpinner.setAdapter(spinnerToAdapter);

        builder.setView(dialogView);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                int from = languageFromSpinner.getSelectedItemPosition();
                int to = translationToSpinner.getSelectedItemPosition();
                if (!dictionaryName.equals("") && to >= 0) {
                    Dictionary newDictionary = new Dictionary(dictionaryName,
                            languageList.get(from).getLanguage().toString(),
                            languageList.get(to).getLanguage().toString());
                    newDictionary.save();
                }
                loadAppropriateFragment();
                updateViewData();
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
