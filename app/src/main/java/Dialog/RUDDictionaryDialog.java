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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        dictionaryID = bundle.getLong(Tags.DICTIONARY_TAG);

        builder.setItems(R.array.rud, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0)
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.new_dic_title));

                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String dictionaryName = input.getText().toString();
                            Dictionary editDictionary = Dictionary.findById(Dictionary.class,dictionaryID);
                            editDictionary.setDictionaryName(dictionaryName);
                            editDictionary.save();

                            Activity parent = getActivity();
                            if (parent instanceof DictionariesActivity) {
                                DictionariesActivity dialogParent =(DictionariesActivity) parent;
                                dialogParent.updateData();
                                dismiss();
                                dialogParent.loadAppropriateFragment();
                            }
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

                    Activity parent = getActivity();
                    if (parent instanceof DictionariesActivity) {
                        DictionariesActivity dialogParent =(DictionariesActivity) parent;
                        dialogParent.updateData();
                        dismiss();
                        dialogParent.loadAppropriateFragment();
                    }
                }

            }
        });
        return  builder.create();
    }
}