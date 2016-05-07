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
        parent = (TopicsActivity)getActivity();
        topicsGrid = (GridView) view.findViewById(R.id.topicsGridView);
        final List<Topic> topicsInfo = parent.getActivityData();

        topicsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent topicWordsIntent = new Intent(parent.getContext(), WordsActivity.class);
                topicWordsIntent.putExtra(Tags.TOPIC_TAG,topicsInfo.get(position).getId() );
                topicWordsIntent.putExtra(Tags.TOPIC_NAME_TAG,topicsInfo.get(position).getTopicName() );
                startActivity(topicWordsIntent);
                Toast.makeText(parent.getContext(),topicsInfo.get(position).getTopicName(),Toast.LENGTH_SHORT);
            }
        });

        topicsGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                RUDTopicDialog rudDialog = new RUDTopicDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.TOPIC_TAG, topicsInfo.get(pos).getId());
                rudDialog.setArguments(bundle);
                rudDialog.show(parent.getActivityFragmentManager(),"RUD");
                Log.d("DIALOG", "TOUCHED" );
                return true;
            }
        });

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
