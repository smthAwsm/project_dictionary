package Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.study.xps.projectdictionary.R;

import Models.Dictionary;
import Models.Tags;
import Models.Topic;
import activities.DictionariesActivity;
import activities.TopicsActivity;

/**
 * Created by XPS on 4/29/2016.
 */
public class RUDTopicDialog extends DialogFragment {

    private long topicID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        topicID = bundle.getLong(Tags.TOPIC_TAG);

        builder.setItems(R.array.rud, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0)
                {
                    DialogFragment topicDialog = new NewTopicDialog();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Tags.TOPIC_NAME_TAG,true);
                    bundle.putLong(Tags.TOPIC_TAG,topicID);
                    topicDialog.setArguments(bundle);

                    topicDialog.show(getFragmentManager(), Tags.NEW_TOPIC_DIALOG);
                }

                if( which == 1) {

                    Topic editTopic = Topic.findById(Topic.class, topicID);
                    editTopic.delete();
                    //TODO implement word deletting

                    Activity parent = getActivity();
                    if (parent instanceof TopicsActivity) {
                        TopicsActivity _parent = (TopicsActivity) parent;
                        _parent.loadAppropriateFragment();
                        _parent.updateData();
                        dismiss();
                    }
                }
            }
        });
        return  builder.create();
    }
}
