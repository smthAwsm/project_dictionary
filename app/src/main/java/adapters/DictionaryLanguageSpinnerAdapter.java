package adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import helpers.DropCheckingSpinner;
import helpers.OnSpinnerEventsListener;
import models.Language;
import models.Languages;

/**
 * Created by XPS on 07/23/2016.
 */
public class DictionaryLanguageSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Language> mLanguages;
    private DropCheckingSpinner mParent;

    private Integer mLanguageFromPosition;
    private Integer mLanguageToPosition;

    public DictionaryLanguageSpinnerAdapter(Context context) {
        this.mContext = context;
        mLanguages = new ArrayList<>();
        fillLanguagesData(mLanguages);
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
        mParent = (DropCheckingSpinner) parent;

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent){
        final int viewPosition = position;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Language language = mLanguages.get(position);

        mParent.setSpinnerEventsListener(new OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {

            }

            @Override
            public void onSpinnerClosed() {

            }
        });
//        View.OnClickListener dropSpinner = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("AHAHAHAHA",mParent.isShown()+"");
//                mParent.performClick();
//            }
//        };

        final View childView = inflater.inflate(R.layout.dialog_dictionary_spinner_item,null);
        //childView.setOnClickListener(dropSpinner);

        TextView languageTextView = (TextView) childView.findViewById(R.id.languageTextView);
        languageTextView.setText(language.getLanguage().toString());
        //languageTextView.setOnClickListener(dropSpinner);
        ImageView countryFlag = (ImageView) childView.findViewById(R.id.flagImageView);
        countryFlag.setBackgroundResource(language.getFlagRecourceId());
        //countryFlag.setOnClickListener(dropSpinner);

        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParent.hasBeenOpened()){
                    selectSpinnerItem(viewPosition);
                } else mParent.performClick();
            }
        });
        return childView;
    }

    private void fillLanguagesData(List<Language> languagesList){
        int arrayLanguagesId = mContext.getResources().
                getIdentifier("languages" , "array", mContext.getPackageName());
        int arrayFlagsId = mContext.getResources().
                getIdentifier("flags" , "array", mContext.getPackageName());

        if (arrayLanguagesId != 0 && arrayFlagsId !=0)
        {
            TypedArray languageRecources = mContext.getResources().
                    obtainTypedArray(arrayLanguagesId);
            String[] languageStrings = languageRecources.getResources().
                    getStringArray(arrayLanguagesId);
            TypedArray flagRecources = mContext.getResources().
                    obtainTypedArray(arrayFlagsId);

            for (int i = 0; i < languageRecources.length(); i++ ){
                Languages language = Languages.fromString(languageStrings[i]);
                Integer flagImageId = flagRecources.getResourceId(i,0);
                languagesList.add(new Language(language,flagImageId));
            }
            languageRecources.recycle();
            flagRecources.recycle();
        }
    }

    public void selectSpinnerItem(int position){
        switch (mParent.getId()){
            case R.id.languageFromSpinner:
                mParent.setSelection(position);
                mLanguageFromPosition = position;
                hideSpinnerDropDown(mParent);
                mParent.performClosedEvent();
                break;
            case R.id.languageToSpinner:
                mParent.setSelection(position);
                mLanguageToPosition = position;
                hideSpinnerDropDown(mParent);
                mParent.performClosedEvent();
                break;
        }
    }

    public static void hideSpinnerDropDown(View spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
