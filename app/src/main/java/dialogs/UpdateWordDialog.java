package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.study.xps.projectdictionary.R;
import models.Tags;
import models.Word;
import activities.WordsActivity;

/**
 * Created by XPS on 4/29/2016.
 */
public class UpdateWordDialog extends DialogFragment {

    private long mWordId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        try {
            Bundle bundle = getArguments();
            mWordId = bundle.getLong(Tags.WORD_TAG);
        } catch (Exception e){
            mWordId = -1;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.rud, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0)
                {
                    DialogFragment topicDialog = new NewWordDialog();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Tags.SUCCESS_QUERY_TAG,true);
                    bundle.putLong(Tags.WORD_VALUE_TAG, mWordId);
                    topicDialog.setArguments(bundle);

                    topicDialog.show(getFragmentManager(), Tags.NEW_TOPIC_DIALOG);
                }

                if( which == 1) {
                    Word editWord = Word.findById(Word.class, mWordId);
                    editWord.delete();

                        WordsActivity parent = (WordsActivity) getActivity();
                        parent.updateData();
                        dismiss();
                    }
            }
        });
        return  builder.create();
    }
}
