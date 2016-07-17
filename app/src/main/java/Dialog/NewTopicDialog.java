package Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.study.xps.projectdictionary.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Models.Tags;
import activities.TopicsActivity;
import Adapters.TopicsSpinnerAdapter;
import Models.Topic;

/**
 * Created by XPS on 4/18/2016.
 */
public class NewTopicDialog extends DialogFragment {

    private ImageView topicImage;
    private Spinner topicImagesSpinner;
    private Button saveButton;
    private EditText topicName;
    private boolean update;
    private long topicID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final Topic editTopic;

        try {
            update = bundle.getBoolean(Tags.TOPIC_NAME_TAG);
            topicID = bundle.getLong(Tags.TOPIC_TAG);
        } catch (Exception e){
            update = false;
            topicID = 0;
        }

        getDialog().setTitle("New topic");
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final View topicDialog = inflater.inflate(R.layout.dialog_topic_add,null);

        topicImagesSpinner  = (Spinner) topicDialog.findViewById(R.id.imagesSpinner);
        topicImage =  (ImageView) topicDialog.findViewById(R.id.imageView);
        topicImage.setTag(R.drawable.ic_star);

        topicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                          topicImagesSpinner.performClick(); }
        });

        topicName = (EditText) topicDialog.findViewById(R.id.topicNameTextBox);

        if(update){
            editTopic = Topic.findById(Topic.class,topicID);
            topicImage.setImageResource((int)editTopic.getImageRecourceID());
            topicImage.setTag(editTopic.getImageRecourceID());
            topicName.setText(editTopic.getTopicName());
        } else  editTopic = new Topic();

        saveButton = (Button) topicDialog.findViewById(R.id.buttonAddTopic);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicsActivity parent = (TopicsActivity) getActivity();
                if (parent instanceof TopicsActivity) {
                if (!topicName.getText().toString().equals(""))
                {
                    if (topicImage.getTag() != null){
                        if (!update)
                            new Topic(parent.getCurrentDictionaryID(),topicName.getText().toString(),(Integer)topicImage.getTag()).save();
                            else {
                                     editTopic.setTopicName(topicName.getText().toString());
                                     editTopic.setImageRecourceID(Long.parseLong(topicImage.getTag().toString()));
                                     editTopic.save();
                             }
                    } else
                        if (!update)
                        new Topic(parent.getCurrentDictionaryID(),topicName.getText().toString(),(Integer)topicImage.getTag()).save();
                     else {
                        editTopic.setTopicName(topicName.getText().toString());
                        editTopic.setImageRecourceID((Integer)topicImage.getTag());
                        editTopic.save();
                     }
                        parent.updateData();
                    dismiss();
                    }
                }
            }
        });

        ArrayList<String> string = new ArrayList<>();
        string.add(getString(R.string.select_image));

        TopicsSpinnerAdapter spinnerAdapter = new TopicsSpinnerAdapter(getActivity(),this,string);
        topicImagesSpinner.setAdapter(spinnerAdapter);

        return topicDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
     Dialog dialog = getDialog();
        if(dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setTopicImage(int recourceID){
        topicImage.setImageResource(recourceID);
        topicImage.setTag(recourceID);
        hideSpinnerDropDown(topicImagesSpinner);
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
        outState.putInt(Tags.TOPIC_IMAGE_RESOURCE,(Integer)topicImage.getTag());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            Integer imageResource = savedInstanceState.getInt(Tags.TOPIC_IMAGE_RESOURCE);
            topicImage.setImageResource(imageResource);
            topicImage.setTag(imageResource);
        } catch (NullPointerException e){

        }

    }
}
