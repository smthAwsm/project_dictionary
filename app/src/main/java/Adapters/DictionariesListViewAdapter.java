package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;
import models.Topic;
import models.Dictionary;

import java.util.List;
import java.util.zip.Inflater;


/**
 * Created by XPS on 4/9/2016.
 */
public class DictionariesListViewAdapter extends ArrayAdapter<Dictionary> {

    private Context mContext;
    private List<Dictionary> mDictionariesList;

    public DictionariesListViewAdapter(Context context, List<Dictionary> dictionariesList) {
        super(context,0, dictionariesList);
        this.mDictionariesList = dictionariesList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDictionariesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Dictionary dictionary = mDictionariesList.get(position);

       Holder holder = new Holder();
       holder.nameTextView.setText(dictionary.getName());
       holder.dateTextView.setText(mContext.getString(R.string.created) + " "
               + dictionary.getCreationDateString());
       holder.topicsNumberTextView.setText(mContext.getString(R.string.topics) +"  "
               + calculateTopics(dictionary.getId()));
       return holder.listView;
    }

    private long calculateTopics(long ID){
              return Topic.count(Topic.class,"dictionary_ID = " + ID,null );
    }

    private class Holder
    {
        View listView;
        TextView nameTextView;
        TextView dateTextView;
        TextView topicsNumberTextView;

        public Holder() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            this.listView = inflater.inflate(R.layout.fragment_vocabulary_list_item,null);
            this.nameTextView = (TextView) listView.findViewById(R.id.dictionaryNameText);
            this.dateTextView = (TextView) listView.findViewById(R.id.dateText);
            this.topicsNumberTextView = (TextView) listView.findViewById(R.id.numberTopicsText);
        }
    }
}
