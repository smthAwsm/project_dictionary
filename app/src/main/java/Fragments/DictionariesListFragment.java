package fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.study.xps.projectdictionary.R;
import adapters.DictionaryLanguageSpinnerAdapter;
import dialogs.UpdateDictionaryDialog;
import models.Dictionary;
import models.Language;
import models.Languages;
import models.Tags;
import activities.DictionariesActivity;
import activities.TopicsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListFragment extends ListFragment {
    private ListView mDictionariesListView;
    private DictionariesActivity mContextActivity;
    private List<Dictionary> mDictionariesList;
    private List<Language> mLanguages;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_list, container, false);

        mContextActivity = (DictionariesActivity)getActivity();
        mDictionariesList = mContextActivity.getActivityData();
        addDictionaryListListeners(view);
        mLanguages = new ArrayList<>();
        fillLanguagesData(mLanguages);
        return view;
    }

    private void addDictionaryListListeners(View view){
        mDictionariesListView = (ListView) view.findViewById(android.R.id.list);
        mDictionariesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                UpdateDictionaryDialog rudDialog = new UpdateDictionaryDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.DICTIONARY_TAG, mDictionariesList.get(pos).getId());
                rudDialog.setArguments(bundle);
                rudDialog.show(mContextActivity.getActivityFragmentManager(),
                        Tags.UPDATE_DICTIONARY_DIALOG);
                return true;
            }
        });

        FloatingActionButton addDictionaryButton = (FloatingActionButton)
                                        view.findViewById(R.id.addDictionaryFAB);
        addDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDictionaryAlert();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SharedPreferences.Editor prefEditor = getActivity().
                getSharedPreferences(Tags.APP_SETTINGS,Context.MODE_PRIVATE).edit();
        Long dictionaryId = mDictionariesList.get(position).getId();
        prefEditor.putLong(Tags.APP_SETTINGS, dictionaryId);
        prefEditor.commit();

        Intent dictionaryTopicsIntent = new Intent(mContextActivity, TopicsActivity.class);
        dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,
                mDictionariesList.get(position).getId());
        mContextActivity.finish();
        startActivity(dictionaryTopicsIntent);
    }

    private void createDictionaryAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContextActivity,
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.new_dict_title));
        View dialogView = LayoutInflater.from(mContextActivity).
                inflate(R.layout.dialog_dictionary_add,null);
        final EditText input = (EditText) dialogView.findViewById(R.id.dictionaryNameText);

        DictionaryLanguageSpinnerAdapter spinnerFromAdapter =
                new DictionaryLanguageSpinnerAdapter(mContextActivity,mLanguages);
        final Spinner languageFromSpinner = (Spinner) dialogView.
                findViewById(R.id.languageFromSpinner);
        languageFromSpinner.setAdapter(spinnerFromAdapter);

        DictionaryLanguageSpinnerAdapter spinnerToAdapter =
                new DictionaryLanguageSpinnerAdapter(mContextActivity,mLanguages);
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
                if (!dictionaryName.equals("") && to > 0) {
                    Dictionary newDictionary = new Dictionary(dictionaryName,
                            mLanguages.get(from).getLanguage().toString(),
                            mLanguages.get(to).getLanguage().toString());
                    newDictionary.save();
                }
                mContextActivity.updateData();
                mContextActivity.updateViewData();
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

    private void fillLanguagesData(List<Language> languagesList){
        int arrayLanguagesId = mContextActivity.getResources().
                getIdentifier("languages" , "array", mContextActivity.getPackageName());
        int arrayFlagsId = mContextActivity.getResources().
                getIdentifier("flags" , "array", mContextActivity.getPackageName());

        if (arrayLanguagesId != 0 && arrayFlagsId !=0)
        {
            TypedArray languageRecources = mContextActivity.getResources().
                    obtainTypedArray(arrayLanguagesId);
            String[] languageStrings = languageRecources.getResources().
                    getStringArray(arrayLanguagesId);
            TypedArray flagRecources = mContextActivity.getResources().
                    obtainTypedArray(arrayFlagsId);

            for (int i = 0; i < languageRecources.length(); i++ ){
                Languages language = Languages.fromString(languageStrings[i]);
                Integer flagImageId = flagRecources.getResourceId(i,0);
                languagesList.add(new Language(language,flagImageId));
            }
            languageRecources.recycle();
            flagRecources.recycle();
        }
    }
}
