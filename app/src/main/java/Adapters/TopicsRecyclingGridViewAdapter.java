package adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import dialogs.TestStartDialog;
import dialogs.UpdateTopicDialog;
import helpers.GlobalStorage;
import models.Tags;
import models.Topic;
import activities.TopicsActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 5/8/2016.
 */
public class TopicsRecyclingGridViewAdapter extends
        RecyclerView.Adapter<TopicsRecyclingGridViewAdapter.ViewHolder> {

    final GlobalStorage mGlobalStorage;
    private TopicsActivity mContextActivity;

    public TopicsRecyclingGridViewAdapter(TopicsActivity parent){
        this.mContextActivity = parent;
        mGlobalStorage = GlobalStorage.getStorage();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContextActivity);
        View topicsView = inflater.inflate(R.layout.fragment_topics_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(topicsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Topic topic = mGlobalStorage.getTopicsData().get(position);

        holder.topicImage.setImageResource((int) topic.getImageRecourceID());
        holder.topicName.setText(topic.getTopicName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGlobalStorage.setCurrentTopicId(topic.getId());
                Intent topicWordsIntent = new Intent(mContextActivity, WordsActivity.class);
                topicWordsIntent.putExtra(Tags.TOPIC_NAME_TAG,topic.getTopicName());
                mContextActivity.startActivity(topicWordsIntent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UpdateTopicDialog updateTopicDialog = new UpdateTopicDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.TOPIC_TAG, topic.getId());
                updateTopicDialog.setArguments(bundle);
                updateTopicDialog.show(mContextActivity.getActivityFragmentManager(),
                        Tags.UPDATE_TOPIC_DIALOG);
                return true;
            }
        });

        holder.testingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGlobalStorage.getCurrentTopicId() == 0){
                    mGlobalStorage.setCurrentTopicId(topic.getId());
                }

                int wordsListSize = mGlobalStorage.getWordsData().size();
                if (wordsListSize > 8){
                    DialogFragment testDialog = new TestStartDialog();
                    testDialog.show(mContextActivity.getSupportFragmentManager(),
                            Tags.NEW_WORD_DIALOG);}
                else Toast.makeText(mContextActivity,
                        mContextActivity.getString(R.string.more_words),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGlobalStorage.getTopicsData().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView topicImage;
        private TextView topicName;
        private ImageView testingButton;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            topicImage = (ImageView) itemView.findViewById(R.id.topicImage);
            topicName = (TextView) itemView.findViewById(R.id.topicText);
            testingButton = (ImageView) itemView.findViewById(R.id.beginTestingImageView);
        }
    }
}
