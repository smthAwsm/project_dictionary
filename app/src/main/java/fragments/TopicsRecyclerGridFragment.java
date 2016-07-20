package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.study.xps.projectdictionary.R;
import adapters.TopicsRecyclingGridViewAdapter;
import dialogs.NewTopicDialog;
import models.Tags;
import activities.TopicsActivity;


/**
 * Created by XPS on 4/17/2016.
 */
public class TopicsRecyclerGridFragment extends Fragment {

    private TopicsActivity mContexActivity;
    private TopicsRecyclingGridViewAdapter mTopicsGridRecycler;
    private RecyclerView mRecyclerView;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, 
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);
        mContexActivity = (TopicsActivity) getActivity();
        addTopicListListeners(view);

        mTopicsGridRecycler = new TopicsRecyclingGridViewAdapter(
                mContexActivity.getActivityData(), mContexActivity);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.topicsRecyclerView);
        mRecyclerView.setAdapter(mTopicsGridRecycler);

        final GridLayoutManager layoutManager = new GridLayoutManager(mContexActivity,5);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int viewWidth = mRecyclerView.getMeasuredWidth();
                        float cardViewWidth = getActivity().getResources().getDimension(R.dimen.card_width);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);

                        layoutManager.setSpanCount(newSpanCount);
                        layoutManager.requestLayout();
                    }
                });
        return view;
    }

    private void addTopicListListeners(View view){
        FloatingActionButton addTopicButton = (FloatingActionButton)
                view.findViewById(R.id.addTopicFAB);
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
