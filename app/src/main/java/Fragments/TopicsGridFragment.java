package Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Dialog.NewTopicDialog;
import Dialog.RUDTopicDialog;
import Models.Tags;
import Models.Topic;
import activities.DictionariesActivity;
import activities.TopicsActivity;
import activities.WordsActivity;


/**
 * Created by XPS on 4/17/2016.
 */
public class TopicsGridFragment extends Fragment {

    GridView topicsGrid;
    TopicsActivity parent;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);
        addTopicListListeners(view);
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
}
