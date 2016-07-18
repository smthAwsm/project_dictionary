package fragments;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.xps.projectdictionary.R;

import java.util.List;

import adapters.WordsRecyclingViewAdapter;
import dialogs.NewWordDialog;
import models.Tags;
import models.Word;
import activities.WordsActivity;

/**
 * Created by XPS on 4/26/2016.
 */
public class WordsListFragment extends Fragment {

    private RecyclerView wordsList;
    private WordsActivity parent;
    List<Word> wordData;
    private WordsRecyclingViewAdapter viewAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_words_list, container, false);
        parent = (WordsActivity) getActivity();
        addWordViewListeners(view);
        wordData = parent.getActivityData();

        wordsList = (RecyclerView) view.findViewById(R.id.wordsRecyclerView);

        viewAdapter = new WordsRecyclingViewAdapter(wordData,parent);
        viewAdapter.setHasStableIds(false);
        wordsList.setAdapter(viewAdapter);
        wordsList.setLayoutManager(new LinearLayoutManager(parent));

        return view;
    }

    private void addWordViewListeners(View view){

        FloatingActionButton addDictionaryButton = (FloatingActionButton) view.findViewById(R.id.addNewWordFAB);
        addDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                //wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
                wordDialog.show(getFragmentManager(),Tags.NEW_WORD_DIALOG);
            }
        });
    }


    public WordsRecyclingViewAdapter getAdapter() {
        return viewAdapter;
    }
}
