package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.study.xps.projectdictionary.R;

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

    }
}
