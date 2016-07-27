package helpers;

import android.content.Context;
import android.widget.Spinner;

/**
 * Created by XPS on 07/27/2016.
 */
public class DropCheckingSpinner extends Spinner {
    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;

    public DropCheckingSpinner(Context context) {
        super(context);
    }

    @Override
    public boolean performClick() {
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened();
        }
        return super.performClick();
    }

    public void setSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed();
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }
}
