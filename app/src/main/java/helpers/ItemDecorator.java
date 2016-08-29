package helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import adapters.SectionedSettingsAdapter;

/**
 * Created by XPS on 08/28/2016.
 */
public class ItemDecorator extends RecyclerView.ItemDecoration{

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    private Drawable mDivider;
    private final SectionedSettingsAdapter.Section[] mSections;

    public ItemDecorator(Context context, SectionedSettingsAdapter.Section[] sections) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mSections = sections;
        this.mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
            drawHorizontal(c, parent);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < mSections.length; i++) {
            final View child = parent.getChildAt(mSections[i].getSectionedPosition());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            final int top = child.getTop() + params.topMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
}
