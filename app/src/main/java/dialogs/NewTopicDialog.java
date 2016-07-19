package dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.study.xps.projectdictionary.R;
import models.Tags;
import activities.TopicsActivity;
import adapters.TopicsSpinnerAdapter;
import models.Topic;

import java.lang.reflect.Method;


/**
 * Created by XPS on 4/18/2016.
 */
public class NewTopicDialog extends AppCompatDialogFragment {

    private EditText mTopicNameEdit;
    private ImageView mTopicIconView;
    private Spinner mTopicImagesSpinner;
    private Button mSaveTopicButton;
    private Topic mEditTopic;
    private long mTopicId;
    private boolean mIsUpdate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        try {
            mIsUpdate = bundle.getBoolean(Tags.TOPIC_NAME_TAG);
            mTopicId = bundle.getLong(Tags.TOPIC_TAG);
        } catch (Exception e){
            mIsUpdate = false;
            mTopicId = 0;
        }

       return prepareDialog(inflater);
    }

    private View prepareDialog(LayoutInflater inflater){
        getDialog().setTitle(getString(R.string.new_topic_title));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        View updateTopicDialog = inflater.inflate(R.layout.dialog_topic_add,null);
        mTopicImagesSpinner = (Spinner) updateTopicDialog.findViewById(R.id.imagesSpinner);

        mTopicIconView =  (ImageView) updateTopicDialog.findViewById(R.id.imageView);
        mTopicIconView.setTag(R.drawable.ic_star);
        mTopicIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopicImagesSpinner.performClick(); }
        });

        mTopicNameEdit = (EditText) updateTopicDialog.findViewById(R.id.topicNameTextBox);

        if(mIsUpdate){
            mEditTopic = Topic.findById(Topic.class, mTopicId);
            mTopicIconView.setImageResource((int) mEditTopic.getImageRecourceID());
            mTopicIconView.setTag(mEditTopic.getImageRecourceID());
            mTopicNameEdit.setText(mEditTopic.getTopicName());
        } else mEditTopic = new Topic();

        mSaveTopicButton = (Button) updateTopicDialog.findViewById(R.id.buttonAddTopic);
        mSaveTopicButton.setOnClickListener(saveTopicListener);

        TopicsSpinnerAdapter spinnerAdapter = new TopicsSpinnerAdapter(getActivity(),this);
        mTopicImagesSpinner.setAdapter(spinnerAdapter);

        return updateTopicDialog;
    }

    View.OnClickListener saveTopicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TopicsActivity contextActivity;

            try {
                contextActivity = (TopicsActivity) getActivity();
            } catch (ClassCastException e){
                throw new ClassCastException();
            }

            if (!mTopicNameEdit.getText().toString().equals("")) {
                if (mTopicIconView.getTag() != null){
                    if (!mIsUpdate)
                        new Topic(contextActivity.getCurrentDictionaryID(),
                                mTopicNameEdit.getText().toString(),
                                (Integer) mTopicIconView.getTag()).save();
                    else {
                        mEditTopic.setTopicName(mTopicNameEdit.getText().toString());
                        mEditTopic.setImageRecourceID(
                                Long.parseLong(mTopicIconView.getTag().toString()));
                        mEditTopic.save();
                    }
                } else if (!mIsUpdate) {
                    new Topic(contextActivity.getCurrentDictionaryID(),
                            mTopicNameEdit.getText().toString(),
                            (Integer) mTopicIconView.getTag()).save();
                } else {
                        mEditTopic.setTopicName(mTopicNameEdit.getText().toString());
                        mEditTopic.setImageRecourceID((Integer) mTopicIconView.getTag());
                        mEditTopic.save();
                }
                    contextActivity.updateData();
                    dismiss();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setTopicImage(int recourceID){
        mTopicIconView.setImageResource(recourceID);
        mTopicIconView.setTag(recourceID);
        hideSpinnerDropDown(mTopicImagesSpinner);
    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
        e.printStackTrace();
    }
}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Tags.TOPIC_IMAGE_RESOURCE,(Integer) mTopicIconView.getTag());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            Integer imageResource = savedInstanceState.getInt(Tags.TOPIC_IMAGE_RESOURCE);
            mTopicIconView.setImageResource(imageResource);
            mTopicIconView.setTag(imageResource);
        } catch (NullPointerException e){
            mTopicIconView.setImageResource(R.drawable.ic_star);
            mTopicIconView.setTag(R.drawable.ic_star);
        }
    }
}
