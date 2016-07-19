package dialogs;


import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.study.xps.projectdictionary.R;
import models.Dictionary;
import models.Tags;
import models.Topic;
import models.Word;
import activities.DictionariesActivity;

import com.orm.SugarTransactionHelper;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;


/**
 * Created by XPS on 4/28/2016.
 */
public class UpdateDictionaryDialog extends AppCompatDialogFragment {

    private DictionariesActivity mContextActivity;
    private long mDictionaryID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            Bundle bundle = getArguments();
            mDictionaryID = bundle.getLong(Tags.DICTIONARY_TAG);
        } catch (Exception e){
            mDictionaryID = 0;
        }

        mContextActivity = (DictionariesActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContextActivity);
        builder.setItems(R.array.rud,mDialogClicListener);
        return  builder.create();
    }

    private DialogInterface.OnClickListener mDialogClicListener =
            new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == 0){
                createUpdateDictionaryDialog();
            }

            if( which == 1) {
                deleteDictionary();
            }
        }
    };

    private void createUpdateDictionaryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.new_dict_title));

        View dictionaryUpdateDialog = LayoutInflater.from(getActivity()).
                inflate(R.layout.dialog_dictionary_add,null);
        final EditText dictionaryNameInput = (EditText) dictionaryUpdateDialog.
                findViewById(R.id.dictionaryNameText);
        final Dictionary editDictionary = Dictionary.
                findById(Dictionary.class, mDictionaryID);
        dictionaryNameInput.setText(editDictionary.getName());
        builder.setView(dictionaryUpdateDialog);

        builder.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dictionaryName = dictionaryNameInput.getText().toString();

                        editDictionary.setDictionaryName(dictionaryName);
                        editDictionary.save();

                        mContextActivity.updateData();
                        mContextActivity.updateViewData();
                        dismiss();
                        mContextActivity.loadAppropriateFragment();
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteDictionary(){
        final Dictionary editDictionary = Dictionary.findById(Dictionary.class, mDictionaryID);

        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {

                List<Topic> topics = Select.from(Topic.class)
                        .where(Condition.prop("dictionary_ID").eq(editDictionary.getId()))
                        .list();

                for (Topic topic : topics){
                    List<Word> words = Select.from(Word.class)
                            .where(Condition.prop("topic_ID").eq(topic.getId()))
                            .list();
                    for (Word word : words)
                        word.delete();
                    topic.delete();
                }
                editDictionary.delete();
            }
        });

        mContextActivity.updateData();
        mContextActivity.updateViewData();
        dismiss();
        mContextActivity.loadAppropriateFragment();
    }
}
