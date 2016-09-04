package adapters;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import helpers.GlobalStorage;
import models.Language;
import models.Topic;
import models.Dictionary;

import java.util.List;

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
       for(Language lan : holder.languagesList){
           String dictionaryLanguage = lan.getLanguage().name();
           if(dictionaryLanguage.equals(dictionary.getLanguageFrom())){
               holder.languageFromImageView.setImageResource(lan.getFlagRecourceId());
           }
           if(dictionaryLanguage.equals(dictionary.getTranslationTo())){
               holder.languageToImageView.setImageResource(lan.getFlagRecourceId());
           }
       }
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
        ImageView languageFromImageView;
        ImageView languageToImageView;
        List<Language> languagesList;

        public Holder() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            this.listView = inflater.inflate(R.layout.fragment_vocabulary_list_item,null);
            this.nameTextView = (TextView) listView.findViewById(R.id.dictionaryNameText);
            this.dateTextView = (TextView) listView.findViewById(R.id.dateText);
            this.languageFromImageView = (ImageView) listView.findViewById(R.id.languageFromImageView);
            this.languageToImageView = (ImageView) listView.findViewById(R.id.languageToImageView);
            this.topicsNumberTextView = (TextView) listView.findViewById(R.id.numberTopicsText);
            this.languagesList =  GlobalStorage.getStorage().getUsedLanguagesData(mContext);
        }
    }
}
