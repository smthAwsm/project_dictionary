package Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Dialog.NewWordDialog;
import Models.Dictionary;
import Models.Tags;
import Models.Word;
import activities.WordsActivity;

/**
 * Created by XPS on 4/26/2016.
 */
public class WordsListFragment extends Fragment {

    RecyclerView wordsView;
    WordsActivity parent;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_words_list, container, false);
        addWordViewListeners(view);
        return view;
    }


    private void addWordViewListeners(View view){
        parent = (WordsActivity) getActivity();
        final List<Word> wordsInfo = parent.getActivityData();

        // wordsList = (ListView) findViewById(R.id.wordsListView);
//        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("asd",position+"");


        //  Toast.makeText(getApplicationContext(),position +"", Toast.LENGTH_SHORT);  //TODO RECYCLERVIEW Click
        //WordsListViewAdapter adapter = (WordsListViewAdapter) parent.getAdapter();
        //Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),TopicsActivity.class);
        //dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,wordsInfo.get(position).getId());
        //startActivity(dictionaryTopicsIntent);
        //  }
        //  });

//        wordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int pos, long id) {
//                RUDWordDialog rudDialog = new RUDWordDialog();
//                Bundle bundle = new Bundle();
//                bundle.putLong(Tags.WORD_TAG, wordsInfo.get(pos).getId());
//                rudDialog.setArguments(bundle);
//                rudDialog.show(fragmentManager,"RUD");
//                Log.d("DIALOG", "TOUCHED" );
//                return true;
//            }
        //});

        FloatingActionButton addDictionaryButton = (FloatingActionButton) view.findViewById(R.id.addNewWordFAB);
        addDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
            }
        });
    }
}
