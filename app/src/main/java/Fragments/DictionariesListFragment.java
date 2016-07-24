package fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
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
import models.Tags;
import activities.DictionariesActivity;
import activities.TopicsActivity;

import java.util.List;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListFragment extends ListFragment {
    private ListView mDictionariesListView;
    private DictionariesActivity mContextActivity;
    private List<Dictionary> mDictionariesList;

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
                new DictionaryLanguageSpinnerAdapter(mContextActivity);
        final Spinner languageFromSpinner = (Spinner) dialogView.
                findViewById(R.id.languageFromSpinner);
        languageFromSpinner.setAdapter(spinnerFromAdapter);

        DictionaryLanguageSpinnerAdapter spinnerToAdapter =
                new DictionaryLanguageSpinnerAdapter(mContextActivity);
        final Spinner languageToSpinner = (Spinner) dialogView.
                findViewById(R.id.languageToSpinner);
        languageToSpinner.setAdapter(spinnerToAdapter);

        builder.setView(dialogView);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.save();

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
}
