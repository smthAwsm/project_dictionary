package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

/**
 * Created by XPS on 07/18/2016.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    ImageView img;

    private int[] mIcons = new int[]{R.drawable.ic_flight_24dp, R.drawable.ic_mail_24dp, R.drawable.ic_explore_24dp};
    private int[] mLabel = new int[]{R.string.intro_feature_label_1, R.string.intro_feature_label_2, R.string.intro_feature_label_3};
    private int[] mDescription = new int[]{R.string.intro_feature_description_1, R.string.intro_feature_description_2, R.string.intro_feature_description_3};

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
        TextView featureLabelTextView = (TextView) rootView.findViewById(R.id.section_label);
        featureLabelTextView.setText(getString(mLabel[getArguments().getInt(ARG_SECTION_NUMBER) -1]));

        TextView featureDescTextView = (TextView) rootView.findViewById(R.id.selection_description);
        featureDescTextView.setText(getString(mDescription[getArguments().getInt(ARG_SECTION_NUMBER) -1]));

        img = (ImageView) rootView.findViewById(R.id.section_img);
        img.setBackgroundResource(mIcons[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

        return rootView;
    }
}
