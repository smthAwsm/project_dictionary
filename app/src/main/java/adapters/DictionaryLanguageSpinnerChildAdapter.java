package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by XPS on 07/24/2016.
 */
public class DictionaryLanguageSpinnerChildAdapter extends BaseAdapter{

    private Context mContext;
    //private Holder mHolder;
    private List<String> mLanguages;


    public DictionaryLanguageSpinnerChildAdapter(Context context,List<String> languages) {
        this.mContext = context;
        this.mLanguages = languages;
    }

    @Override
    public int getCount() {
        return mLanguages.size();
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
        holder.languageTextView.setText(mLanguages.get(position));
        holder.countryFlag.setBackgroundColor(Color.BLACK);

        return holder.childView;
    }

    private class Holder
    {
        View childView;
        TextView languageTextView ;
        ImageView countryFlag;

        public Holder() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            childView = inflater.inflate(R.layout.dialog_dictionary_spinner_item,null);
            languageTextView = (TextView) childView.findViewById(R.id.languageTextView);
            countryFlag = (ImageView) childView.findViewById(R.id.flagImageView);
        }
    }
}
