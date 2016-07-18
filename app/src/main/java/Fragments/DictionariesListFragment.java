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

import com.study.xps.projectdictionary.R;
import java.util.List;

import dialogs.RUDDictionaryDialog;
import models.Dictionary;
import models.Tags;
import activities.DictionariesActivity;
import activities.TopicsActivity;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListFragment extends ListFragment {
    ListView dictionariesList;
    DictionariesActivity parent;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_list, container, false);
        addDictionaryListListeners(view);
        return view;
    }

    private void addDictionaryListListeners(View view){
        parent = (DictionariesActivity)getActivity();
        final List<Dictionary> dictionariesInfo = parent.getActivityData();

        dictionariesList = (ListView) view.findViewById(android.R.id.list);

        dictionariesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                RUDDictionaryDialog rudDialog = new RUDDictionaryDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.DICTIONARY_TAG, dictionariesInfo.get(pos).getId());
                rudDialog.setArguments(bundle);
                rudDialog.show(parent.getActivityFragmentManager(),"RUD");
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
        parent = (DictionariesActivity)getActivity();
        final List<Dictionary> dictionariesInfo = parent.getActivityData();

        SharedPreferences.Editor prefEditor = getActivity().
                getSharedPreferences(Tags.APP_SETTINGS,Context.MODE_PRIVATE).edit();
        Long dictionaryId = dictionariesInfo.get(position).getId();
        prefEditor.putLong(Tags.VOCABULARY_SHARED, dictionaryId);
        prefEditor.commit();

        Intent dictionaryTopicsIntent = new Intent(parent, TopicsActivity.class);
        dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,dictionariesInfo.get(position).getId());
        parent.finish();
        startActivity(dictionaryTopicsIntent);
    }

    private void createDictionaryAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(parent, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.new_dict_title));
        View dictionaryName = LayoutInflater.from(parent).inflate(R.layout.dialog_dictionary_add,null);
        final EditText input = (EditText) dictionaryName.findViewById(R.id.dictionaryNameText);

        builder.setView(dictionaryName);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.save();

                parent.updateData();
                parent.loadAppropriateFragment();
                parent.updateViewData();
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
