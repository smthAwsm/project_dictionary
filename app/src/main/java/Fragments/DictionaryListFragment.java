package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.DictionariesListViewAdapter;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionaryListFragment extends Fragment {

    ArrayList<Integer> dictionariesList;
    ListView dictionariesListView;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vocabulary_list_fragment, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();




    }
}
