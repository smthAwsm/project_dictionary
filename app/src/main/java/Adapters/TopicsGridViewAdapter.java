package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.study.xps.projectdictionary.R;

import java.util.List;

import activities.TopicsActivity;
import models.Topic;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsGridViewAdapter extends BaseAdapter {

    Context context;
    List<Topic> topicsList;
    LayoutInflater inflater;

    public TopicsGridViewAdapter(TopicsActivity topicsActivity, List<Topic> topics){
        context = topicsActivity;
        topicsList = topics;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return topicsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View gridView = inflater.inflate(R.layout.fragment_topics_list_item,null);

        holder.topicImageView = (ImageView) gridView.findViewById(R.id.topicImage);
        holder.topicTextView = (TextView) gridView.findViewById(R.id.topicText);

        holder.topicImageView.setImageResource((int) topicsList.get(position).getImageRecourceID());
        holder.topicTextView.setText(topicsList.get(position).getTopicName());

        return gridView;
    }

    private class Holder
    {
        ImageView topicImageView;
        TextView  topicTextView;

    }

}
