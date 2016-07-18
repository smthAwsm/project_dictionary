package adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.List;

import dialogs.RUDTopicDialog;
import models.Tags;
import models.Topic;
import activities.TopicsActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 5/8/2016.
 */
public class TopicsRecyclingGridViewAdapter extends RecyclerView.Adapter<TopicsRecyclingGridViewAdapter.ViewHolder> {

    private List<Topic> topicsInfo;
    TopicsActivity parent;
    Context context;

    public TopicsRecyclingGridViewAdapter(List<Topic> data, TopicsActivity parent){
        topicsInfo = data;
        this.parent = parent;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View topicsView = inflater.inflate(R.layout.fragment_topics_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(topicsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Topic topic = topicsInfo.get(position);

        holder.topicImage.setImageResource((int) topic.getImageRecourceID());
        holder.topicName.setText(topic.getTopicName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topicWordsIntent = new Intent(parent, WordsActivity.class);
                topicWordsIntent.putExtra(Tags.TOPIC_TAG,topic.getId());
                topicWordsIntent.putExtra(Tags.TOPIC_NAME_TAG,topic.getTopicName());
                parent.startActivity(topicWordsIntent);
                Toast.makeText(parent,topic.getTopicName(), Toast.LENGTH_SHORT);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RUDTopicDialog rudDialog = new RUDTopicDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.TOPIC_TAG, topic.getId());
                rudDialog.setArguments(bundle);
                rudDialog.show(parent.getActivityFragmentManager(),"RUD");
                Log.d("DIALOG", "TOUCHED" );
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicsInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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
