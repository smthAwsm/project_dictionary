package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.List;

import models.Topic;
import models.Dictionary;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListViewAdapter extends ArrayAdapter<Dictionary> {

    private Context context;
    private List<Dictionary> dictionariesList;
    private LayoutInflater inflater;

    public DictionariesListViewAdapter(Context context, List<Dictionary> dictionariesList) {
        super(context,0, dictionariesList);
        this.dictionariesList = dictionariesList;
        this.context = context;
        inflater = (LayoutInflater)context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dictionariesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Holder holder = new Holder();
       View listView = inflater.inflate(R.layout.fragment_vocabulary_list_item,null);

       holder.nameTextView = (TextView) listView.findViewById(R.id.dictionaryNameText);
       holder.dateTextView = (TextView) listView.findViewById(R.id.dateText);
       holder.topicsNumberTextView = (TextView) listView.findViewById(R.id.numberTopicsText);

       Dictionary dictionary = dictionariesList.get(position);
       holder.nameTextView.setText(dictionary.getName());
       holder.dateTextView.setText(context.getString(R.string.created) +" "+ dictionary.getCreationDateString());
       holder.topicsNumberTextView.setText(context.getString(R.string.topics) +"  "+ calculateTopics(dictionary.getId()));

        return listView;
    }


    private long calculateTopics(long ID){
              return Topic.count(Topic.class,"dictionary_ID = " + ID,null );
    }

    private class Holder
    {
        TextView nameTextView;
        TextView dateTextView;
        TextView topicsNumberTextView;
    }
}
