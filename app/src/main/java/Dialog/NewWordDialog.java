package Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Adapters.TopicsSpinnerAdapter;
import Models.Topic;
import Models.Word;
import activities.TopicsActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 4/26/2016.
 */
public class NewWordDialog extends DialogFragment {

    private TextView okButton;
    private TextView cancelButton;
    private EditText valueTextBox;
    private EditText translationTextBox;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //getDialog().setTitle("New topic");
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final View topicDialog = inflater.inflate(R.layout.dialog_word_add,null);

        okButton = (TextView) topicDialog.findViewById(R.id.newWordOK);
        cancelButton = (TextView) topicDialog.findViewById(R.id.newWordCancel);

        valueTextBox = (EditText) topicDialog.findViewById(R.id.wordTextBox);
        translationTextBox = (EditText) topicDialog.findViewById(R.id.translationTextBox);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!valueTextBox.getText().toString().equals("")) && (!translationTextBox.getText().toString().equals("")))
                        new Word(WordsActivity.currentTopicId,valueTextBox.getText().toString(),translationTextBox.getText().toString()).save();
                else return;

                    Activity parent = getActivity();
                    if (parent instanceof WordsActivity) {
                        WordsActivity _parent = (WordsActivity) parent;
                        _parent.loadAppropriateFragment();

                        _parent.wordsInfo = Word.find(Word.class, "topic_ID = ?", WordsActivity.currentTopicId + "");
                        dismiss();
                    }
                }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return topicDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }


}
