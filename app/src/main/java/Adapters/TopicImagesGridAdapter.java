package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.Activities.TopicsActivity;
import com.study.xps.projectdictionary.R;

import java.util.List;

import Models.Topic;

/**
 * Created by XPS on 4/18/2016.
 */
public class TopicImagesGridAdapter extends BaseAdapter {

    Context context;
    List<Integer> imagesList;
    LayoutInflater inflater;

    public TopicImagesGridAdapter(Context context, List<Integer> imagesID){
        this.context = context;
        imagesList = imagesID;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imagesList.size();
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
        View gridViewItem = inflater.inflate(R.layout.dialog_topic_child_grid_item,null);

        holder.topicImage = (ImageView) gridViewItem.findViewById(R.id.childGridItemImage);
        holder.topicImage.setImageResource(imagesList.get(position));

        return gridViewItem;
    }

    private class Holder
    {
        ImageView topicImage;
    }

}
