package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.orm.SugarTransactionHelper;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.study.xps.projectdictionary.R;

import java.util.List;

import models.Dictionary;
import models.Tags;
import models.Topic;
import models.Word;
import activities.DictionariesActivity;

/**
 * Created by XPS on 4/28/2016.
 */
public class RUDDictionaryDialog extends AppCompatDialogFragment {

    private long dictionaryID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final DictionariesActivity parentActivity = (DictionariesActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        Bundle bundle = getArguments();
        dictionaryID = bundle.getLong(Tags.DICTIONARY_TAG);

        builder.setItems(R.array.rud, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0)
                {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(getString(R.string.new_dict_title));
                    View dictionaryName = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_dictionary_add,null);
                    final EditText input = (EditText) dictionaryName.findViewById(R.id.dictionaryNameText);
                    final Dictionary editDictionary = Dictionary.findById(Dictionary.class,dictionaryID);
                    input.setText(editDictionary.getName());

                    builder.setView(dictionaryName);


                    //android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    //builder.setTitle(getString(R.string.new_dic_title));
                    //final EditText input = new EditText(getActivity());
                    //input.setInputType(InputType.TYPE_CLASS_TEXT);
                    //builder.setView(input);

                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String dictionaryName = input.getText().toString();

                            editDictionary.setDictionaryName(dictionaryName);
                            editDictionary.save();

                                parentActivity.updateData();
                                parentActivity.updateViewData();
                                dismiss();
                                parentActivity.loadAppropriateFragment();
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

                if( which == 1) {
                    final Dictionary editDictionary = Dictionary.findById(Dictionary.class,dictionaryID);

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

                    parentActivity.updateData();
                    parentActivity.updateViewData();
                    dismiss();
                    parentActivity.loadAppropriateFragment();
                }

            }
        });
        return  builder.create();
    }
}
