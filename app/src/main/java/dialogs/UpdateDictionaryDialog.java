package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.study.xps.projectdictionary.R;

import adapters.DictionaryLanguageSpinnerAdapter;
import helpers.GlobalStorage;
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
            mDictionaryID = -1;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContextActivity,
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.new_dict_title));
        View dialogView = LayoutInflater.from(mContextActivity).
                inflate(R.layout.dialog_dictionary_add,null);
        final EditText dictionaryNameInput = (EditText)
                dialogView.findViewById(R.id.dictionaryNameText);

        DictionaryLanguageSpinnerAdapter spinnerFromAdapter =
                new DictionaryLanguageSpinnerAdapter(mContextActivity);
        final Spinner languageFromSpinner = (Spinner) dialogView.
                findViewById(R.id.languageFromSpinner);
        languageFromSpinner.setAdapter(spinnerFromAdapter);

        DictionaryLanguageSpinnerAdapter spinnerToAdapter =
                new DictionaryLanguageSpinnerAdapter(mContextActivity);
        final Spinner translationToSpinner = (Spinner) dialogView.
                findViewById(R.id.languageToSpinner);
        translationToSpinner.setAdapter(spinnerToAdapter);

        builder.setView(dialogView);

        final Dictionary editDictionary = Dictionary.
                findById(Dictionary.class, mDictionaryID);
        dictionaryNameInput.setText(editDictionary.getName());
        dictionaryNameInput.setSelection(editDictionary.getName().length());

        builder.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dictionaryName = dictionaryNameInput.getText().toString();

                        editDictionary.setDictionaryName(dictionaryName);
                        editDictionary.save();

                        GlobalStorage mGlobalStorage = GlobalStorage.getStorage();
                        mGlobalStorage.updateDictionariesData();
                        mContextActivity.loadAppropriateFragment();
                        dismiss();
                        mContextActivity.updateViewData();
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

        checkSavedDictionary(mContextActivity,editDictionary);
        GlobalStorage mGlobalStorage = GlobalStorage.getStorage();
        mGlobalStorage.updateDictionariesData();
        mContextActivity.updateViewData();
        dismiss();
        mContextActivity.loadAppropriateFragment();
    }

    private void checkSavedDictionary(Context context,Dictionary dictionaryToDelete){
        SharedPreferences sharedPref =
                context.getSharedPreferences(Tags.APP_SETTINGS,context.MODE_PRIVATE);
        long currentDictionary = sharedPref.getLong(Tags.APP_SETTINGS, -1);

        if(dictionaryToDelete.getId() == currentDictionary){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(Tags.APP_SETTINGS);
            editor.apply();
        }
    }
}
