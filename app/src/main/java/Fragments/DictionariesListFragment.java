package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.study.xps.projectdictionary.R;

import dialogs.UpdateDictionaryDialog;
import helpers.GlobalStorage;
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
        GlobalStorage globalStorage = GlobalStorage.getStorage();
        mDictionariesList = globalStorage.getDictionariesData();
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
                mContextActivity.createDictionaryAlert();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SharedPreferences.Editor prefEditor = getActivity().
                getSharedPreferences(Tags.APP_DATA,Context.MODE_PRIVATE).edit();
        Long dictionaryId = mDictionariesList.get(position).getId();
        prefEditor.putLong(Tags.APP_DATA, dictionaryId);
        prefEditor.commit();

        Intent dictionaryTopicsIntent = new Intent(mContextActivity, TopicsActivity.class);
        dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,
                mDictionariesList.get(position).getId());
        mContextActivity.finish();
        startActivity(dictionaryTopicsIntent);
    }
}
