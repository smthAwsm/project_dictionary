package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.orm.SugarTransactionHelper;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.study.xps.projectdictionary.R;

import java.util.List;

import models.Tags;
import models.Topic;
import models.Word;
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

                    final Topic editTopic = Topic.findById(Topic.class, topicID);

                    SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                        @Override
                        public void manipulateInTransaction() {

                                List<Word> words = Select.from(Word.class)
                                        .where(Condition.prop("topic_ID").eq(editTopic.getId()))
                                        .list();
                                for (Word word : words)
                                    word.delete();

                            editTopic.delete();
                        }
                    });

                    TopicsActivity parent = (TopicsActivity) getActivity();
                        parent.updateData();
                        //parent.loadAppropriateFragment();
                        parent.updateViewData();
                        dismiss();
                    }
                }
        });
        return  builder.create();
    }
}
