package Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.xps.projectdictionary.R;

import Adapters.TopicsRecyclingGridViewAdapter;
import Dialog.NewTopicDialog;
import Dialog.RUDTopicDialog;
import Helpers.GridAutoFitLayoutManager;
import Models.Tags;
import Models.Topic;
import activities.DictionariesActivity;
import activities.TopicsActivity;
import activities.WordsActivity;


/**
 * Created by XPS on 4/17/2016.
 */
public class TopicsGridFragment extends Fragment {

    private TopicsActivity parent;
    private TopicsRecyclingGridViewAdapter adapter;
    private RecyclerView topicsView;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);
        parent = (TopicsActivity) getActivity();
        addTopicListListeners(view);

        adapter = new TopicsRecyclingGridViewAdapter(parent.getActivityData(), parent);
        topicsView = (RecyclerView) view.findViewById(R.id.topicsRecyclerView);
        topicsView.setAdapter(adapter);
        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(parent,200 );
        topicsView.setLayoutManager(layoutManager);

        return view;
    }

    private void addTopicListListeners(View view){
        FloatingActionButton addTopicButton = (FloatingActionButton) view.findViewById(R.id.addTopicFAB);
        addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTopicDialog();
            }
        });
    }

    private void createTopicDialog() {
        DialogFragment topicDialog = new NewTopicDialog();
        topicDialog.show(getFragmentManager(), Tags.NEW_TOPIC_DIALOG);
    }

    public TopicsRecyclingGridViewAdapter getAdapter() {
        return adapter;
    }
}
