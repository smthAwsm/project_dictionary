package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.study.xps.projectdictionary.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XPS on 07/23/2016.
 */
public class DictionaryLanguageSpinnerAdapter extends BaseAdapter {

    private Context mContext;

    public DictionaryLanguageSpinnerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 1;
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
        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.dialog_dictionary_spinner_item,null);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }


    private View getCustomView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mContext);

        List<String> mLanguages = Arrays.asList("UKR","RUS","ENG","TIP","AZA","KOP","ROP");

        View languagesList = inflater.inflate(R.layout.dialog_dictionary_spinner,null);
        ListView listView = (ListView)
                languagesList.findViewById(R.id.new_dictionary_spinner_child);
        DictionaryLanguageSpinnerChildAdapter languageSpinnerAdapter =
                new DictionaryLanguageSpinnerChildAdapter(mContext,mLanguages);
        listView.setAdapter(languageSpinnerAdapter);

//        mTopicIconsGrid.setLayoutParams(
//                new AbsListView.LayoutParams(mTopicIconsGrid.getLayoutParams()));
        return listView;
    }
}
