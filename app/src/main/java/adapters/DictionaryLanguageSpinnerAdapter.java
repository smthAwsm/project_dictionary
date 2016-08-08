package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;
import helpers.DropCheckingSpinner;
import helpers.GlobalStorage;
import helpers.OnSpinnerEventsListener;
import helpers.RoundImageCornersHelper;
import models.Language;

import java.lang.reflect.Method;

/**
 * Created by XPS on 07/23/2016.
 */
public class DictionaryLanguageSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private DropCheckingSpinner mParent;
    private GlobalStorage mGlobalStorage;
    private final int ROUNDING_VALUE = 360;

    public DictionaryLanguageSpinnerAdapter(Context context) {
        this.mContext = context;
        mGlobalStorage = GlobalStorage.getStorage();
    }

    @Override
    public int getCount() {
        return mGlobalStorage.getLanguagesList().size();
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
        Language language = mGlobalStorage.getLanguagesList().get(position);

        final View childView = inflater.inflate(R.layout.dialog_dictionary_spinner_item,null);
        TextView languageTextView = (TextView) childView.findViewById(R.id.languageTextView);
        languageTextView.setText(language.getLanguage().name());
        ImageView countryFlag = (ImageView) childView.findViewById(R.id.flagImageView);

        Bitmap flag = BitmapFactory.decodeResource(
                mContext.getResources(),language.getFlagRecourceId());

        countryFlag.setImageBitmap(RoundImageCornersHelper.
                getRoundedCornerBitmap(flag, ROUNDING_VALUE));

        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParent.hasBeenOpened()){
                    selectSpinnerItem(viewPosition);
                } else mParent.performClick();
            }
        });

        mParent.setSpinnerEventsListener(new OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {

            }

            @Override
            public void onSpinnerClosed() {

            }
        });
        return childView;
    }

    public void selectSpinnerItem(int position){
        switch (mParent.getId()){
            case R.id.languageFromSpinner:
                mParent.setSelection(position);
                hideSpinnerDropDown(mParent);
                mParent.performClosedEvent();
                break;
            case R.id.languageToSpinner:
                mParent.setSelection(position);
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
