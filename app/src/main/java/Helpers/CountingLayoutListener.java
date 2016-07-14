package Helpers;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by XPS on 07/14/2016.
 */
public class CountingLayoutListener implements View.OnLayoutChangeListener{

    private int mLineCount;
    private TextView mCountedTextView;
    private CardView mCardView;
    Integer[] values;

    public CountingLayoutListener(TextView textViewValue) {
        this.mCountedTextView = textViewValue;
    }

    public CountingLayoutListener(TextView textViewValue, Integer[] values, CardView cardView) {
        this.mCountedTextView = textViewValue;
        this.values = values;
        this.mCardView = cardView;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (values[0] != -1 && values[1] != -1){
            values[0] = -1;
            values[1] = -1;
        }

        mLineCount =  mCountedTextView.getLineCount();//(translationValue.getLineCount() > 1) ? translationValue.getLineCount() : 0;


        if (values[0] == -1){
            values[0] = mLineCount;
        } else if(values[1] == -1){
            values[1] = mLineCount;
        }

        if (values[0] != -1 && values[1] != -1){
            Log.d("************* COUNTED","VALUE 1 count " + values[0]+  " VALUE 2 count " + values[1]);
            fitCard(mCardView);
        }
    }

    public void fitCard(CardView cardView) {

        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();

        int additionalValue = (400 / 3) * (values[0] + values[1]);
        int height = 100 + additionalValue;
        layoutParams.height = 0;
        layoutParams.height = height;
        Log.e("RESULT",layoutParams.height+"");
        cardView.setLayoutParams(layoutParams);
        Log.e("&&&&&&&&&&&&&&&","CARD FITTED!!!");
    }
}
