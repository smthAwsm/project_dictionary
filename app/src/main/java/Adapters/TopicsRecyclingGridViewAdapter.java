package adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;
import dialogs.UpdateTopicDialog;
import models.Tags;
import models.Topic;
import activities.TopicsActivity;
import activities.WordsActivity;

import java.util.List;

/**
 * Created by XPS on 5/8/2016.
 */
public class TopicsRecyclingGridViewAdapter extends
        RecyclerView.Adapter<TopicsRecyclingGridViewAdapter.ViewHolder> {

    private List<Topic> mTopicList;
    private TopicsActivity mContextActivity;


    public TopicsRecyclingGridViewAdapter(List<Topic> data, TopicsActivity parent){
        mTopicList = data;
        this.mContextActivity = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContextActivity);
        View topicsView = inflater.inflate(R.layout.fragment_topics_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(topicsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Topic topic = mTopicList.get(position);

        holder.topicImage.setImageResource((int) topic.getImageRecourceID());
        holder.topicName.setText(topic.getTopicName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topicWordsIntent = new Intent(mContextActivity, WordsActivity.class);
                topicWordsIntent.putExtra(Tags.TOPIC_TAG,topic.getId());
                topicWordsIntent.putExtra(Tags.TOPIC_NAME_TAG,topic.getTopicName());
                mContextActivity.startActivity(topicWordsIntent);
                Toast.makeText(mContextActivity,topic.getTopicName(), Toast.LENGTH_SHORT);
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
    }

    @Override
    public int getItemCount() {
        return mTopicList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView topicImage;
        private TextView topicName;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            topicImage = (ImageView) itemView.findViewById(R.id.topicImage);
            topicName = (TextView) itemView.findViewById(R.id.topicText);
        }
    }
}
