package fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.study.xps.projectdictionary.R;

import activities.DriveOperationsActivity;
import helpers.DriveTasksGenerator;

/**
 * Created by XPS on 07/18/2016.
 */
public class IntroPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "SELECTION_NUMBER";
    private ImageView mFragmentDrawable;

    private int[] mIcons = new int[]{
            R.drawable.ic_accessibility,
            R.drawable.ic_cloud_done,
            R.drawable.ic_verified_user};

    private int[] mLabel = new int[]{
            R.string.intro_feature_label_1,
            R.string.intro_feature_label_2,
            R.string.intro_feature_label_3};

    private int[] mDescription = new int[]{
            R.string.intro_feature_description_1,
            R.string.intro_feature_description_2,
            R.string.intro_feature_description_3};

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static IntroPlaceholderFragment newInstance(int sectionNumber) {
        IntroPlaceholderFragment fragment = new IntroPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int fragmentIndex = getArguments().getInt(ARG_SECTION_NUMBER);
        if(fragmentIndex < 3){
            return setupIntroFeatureFragment(inflater,container,null);
        }
        if (fragmentIndex == 3){
            return setupIntroFeatureFragment(inflater,container,true);
        }
        return null;
    }

    private View setupIntroFeatureFragment(LayoutInflater inflater,ViewGroup container,
                                           Boolean login){
        View rootView;
        if(login != null && login){
            rootView = inflater.inflate(R.layout.fragment_pager_login_layout, container, false);
            final DriveOperationsActivity contextActivity;
            try {
                contextActivity = (DriveOperationsActivity) getActivity();
            } catch (ClassCastException e) {
                e.printStackTrace();
                return setupIntroFeatureFragment(inflater,container,null);
            }

            SignInButton signInAccount = (SignInButton) rootView.findViewById(R.id.sign_in_button);
            signInAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contextActivity.executeDriveTask(
                            DriveTasksGenerator.DriveTask.APP_FOLDER_RESTORE_TASK);
                    }
                });
        } else {
            rootView = inflater.inflate(R.layout.fragment_pager_text_layout, container, false);
        }

        TextView featureLabelTextView = (TextView) rootView.findViewById(R.id.section_label);
        int labelResId = mLabel[getArguments().getInt(ARG_SECTION_NUMBER) - 1];
        featureLabelTextView.setText(getString(labelResId));

        TextView featureDescTextView = (TextView) rootView.findViewById(R.id.selection_description);
        int descriptionResId = mDescription[getArguments().getInt(ARG_SECTION_NUMBER) - 1];
        featureDescTextView.setText(getString(descriptionResId));

        mFragmentDrawable = (ImageView) rootView.findViewById(R.id.section_img);
        int iconResId = mIcons[getArguments().getInt(ARG_SECTION_NUMBER) - 1];
        //mFragmentDrawable.setBackgroundResource(iconResId);

        Drawable d = VectorDrawableCompat.create(getResources(), iconResId, null);
        //d = DrawableCompat.wrap(d);
        //DrawableCompat.setTint(d, );
        mFragmentDrawable.setImageDrawable(d);



        return rootView;
    }
}
