package helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by XPS on 07/17/2016.
 */

public class MeasurableGridView extends GridView {
    public MeasurableGridView(Context context) {
        super(context);
    }

    public MeasurableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasurableGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
