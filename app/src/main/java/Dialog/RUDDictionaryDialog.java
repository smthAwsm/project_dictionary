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
import activities.DictionariesActivity;

/**
 * Created by XPS on 4/28/2016.
 */
public class RUDDictionaryDialog extends DialogFragment {

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
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.new_dic_title));
                    final Dictionary editDictionary = Dictionary.findById(Dictionary.class,dictionaryID);
                    final EditText input = new EditText(getActivity());
                    input.setText(editDictionary.getName());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

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
                    Dictionary editDictionary = Dictionary.findById(Dictionary.class,dictionaryID);
                    editDictionary.delete();
                    //TODO implement topic/word deletting

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
