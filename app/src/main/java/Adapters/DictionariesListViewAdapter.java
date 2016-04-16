package Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.study.xps.projectdictionary.Activities.MainActivity;
import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.List;

import Models.Dictionary;

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListViewAdapter extends BaseAdapter {

    Context context;
    List<Dictionary> dictionariesList;
    LayoutInflater inflater;

    public DictionariesListViewAdapter(MainActivity dictionariesActivity, List<Dictionary> data){
        dictionariesList = data;
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
        return position +"";//dictionariesList.get(position).getName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
       View gridView = inflater.inflate(R.layout.dictionary_item_layout,null);

       holder.nameTextView = (TextView) gridView.findViewById(R.id.dictionaryNameText);
       holder.dateTextView = (TextView) gridView.findViewById(R.id.dateText);
       holder.topicsNumberTextView = (TextView) gridView.findViewById(R.id.numberTopicsText);

       holder.nameTextView.setText(dictionariesList.get(position).getName());
       holder.dateTextView.setText(context.getString(R.string.created) +" "+ dictionariesList.get(position).getCreationDateString());
       holder.topicsNumberTextView.setText(context.getString(R.string.topics) +" "+ position);

        return gridView;
    }

    private class Holder
    {
        TextView nameTextView;
        TextView dateTextView;
        TextView topicsNumberTextView;
    }
}
