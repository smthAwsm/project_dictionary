package Adapters;

import android.content.Context;
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

/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> dictionariesList;
    LayoutInflater inflater;

    public DictionariesListViewAdapter(MainActivity dictionariesActivity,ArrayList<Integer> data){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
       View rowView = inflater.inflate(R.layout.dictionary_item_layout,null);

        holder.dictionaryName = (TextView) rowView.findViewById(R.id.dictionaryNameText);
        holder.creationDate = (TextView) rowView.findViewById(R.id.dateText);
        holder.numberOfTopics =(TextView) rowView.findViewById(R.id.numberTopicsText);

        holder.dictionaryName.setText(dictionariesList.get(position) + "");
        holder.creationDate.setText(dictionariesList.get(position)+"");
        holder.numberOfTopics.setText(position+"");
        return rowView;
    }

    private class Holder
    {
        TextView dictionaryName;
        TextView creationDate;
        TextView numberOfTopics;
    }
}
