package helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by XPS on 07/24/2016.
 */
public class MeasurableListView extends ListView {
    public MeasurableListView(Context context) {
        super(context);
    }

    public MeasurableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasurableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE/2, MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
