package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.study.xps.projectdictionary.R;
import models.Tags;
import models.Topic;
import models.Word;
import activities.TopicsActivity;

import com.orm.SugarTransactionHelper;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;


/**
 * Created by XPS on 4/29/2016.
 */
public class UpdateTopicDialog extends DialogFragment {

    private long mTopicId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            Bundle bundle = getArguments();
            mTopicId = bundle.getLong(Tags.TOPIC_TAG);
        } catch (Exception e){
            mTopicId = -1;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.rud, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    DialogFragment topicUpdateDialog = new NewTopicDialog();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Tags.TOPIC_NAME_TAG,true);
                    bundle.putLong(Tags.TOPIC_TAG, mTopicId);
                    topicUpdateDialog.setArguments(bundle);
                    topicUpdateDialog.show(getFragmentManager(), Tags.NEW_TOPIC_DIALOG);
                }

                if( which == 1) {
                    final Topic editTopic = Topic.findById(Topic.class, mTopicId);
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

                    TopicsActivity contextActivity = (TopicsActivity) getActivity();
                    contextActivity.updateData();
                    dismiss();
                }
            }
        });
        return  builder.create();
    }
}
