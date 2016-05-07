package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.List;

import activities.DictionariesActivity;
import Models.Dictionary;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListViewAdapter extends BaseAdapter {

    Context context;
    List<Dictionary> dictionariesList;
    List<Long> topicsCounted;
    LayoutInflater inflater;

    public DictionariesListViewAdapter(DictionariesActivity dictionariesActivity, List<Dictionary> data,List<Long> topicsCount){
        dictionariesList = data;
        topicsCounted = topicsCount;
        context = dictionariesActivity;
        inflater = (LayoutInflater)context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dictionariesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getDictionaryName(int position) {
        return dictionariesList.get(position).getName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
       View listView = inflater.inflate(R.layout.fragment_vocabulary_list_item,null);

       holder.nameTextView = (TextView) listView.findViewById(R.id.dictionaryNameText);
       holder.dateTextView = (TextView) listView.findViewById(R.id.dateText);
       holder.topicsNumberTextView = (TextView) listView.findViewById(R.id.numberTopicsText);

       holder.nameTextView.setText(dictionariesList.get(position).getName());
       holder.dateTextView.setText(context.getString(R.string.created) +" "+ dictionariesList.get(position).getCreationDateString());
       holder.topicsNumberTextView.setText(context.getString(R.string.topics) +"  "+ topicsCounted.get(position));

        return listView;
    }

    private class Holder
    {
        TextView nameTextView;
        TextView dateTextView;
        TextView topicsNumberTextView;
    }
}
