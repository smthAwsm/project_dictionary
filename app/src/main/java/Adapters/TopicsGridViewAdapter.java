package Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.Activities.TopicsActivity;
import com.study.xps.projectdictionary.R;

import java.util.List;

import Models.Dictionary;

/**
 * Created by XPS on 4/12/2016.
 */
public class TopicsGridViewAdapter extends BaseAdapter {

    Context context;
    List<Integer> topicsList;
    LayoutInflater inflater;

    public TopicsGridViewAdapter(TopicsActivity topicsActivity, List<Integer> integers){
        context = topicsActivity;
        topicsList = integers;
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
        View gridView = inflater.inflate(R.layout.dictionary_item_layout,null);

        holder.topicImageView = (ImageView) gridView.findViewById(R.id.topicImage);
        holder.topicTextView = (TextView) gridView.findViewById(R.id.topicText);

        holder.topicImageView.setImageResource(R.drawable.ic_add);
        holder.topicTextView.setText("Created: " + position);

        return gridView;
    }

    private class Holder
    {
        ImageView topicImageView;
        TextView  topicTextView;

    }

}
