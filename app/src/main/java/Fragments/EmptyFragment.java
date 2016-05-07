package Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import Dialog.NewTopicDialog;
import Dialog.NewWordDialog;
import Models.Dictionary;
import Models.Tags;
import Models.Word;
import activities.DictionariesActivity;
import activities.TopicsActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 4/9/2016.
 */
public class EmptyFragment extends Fragment {

    ImageView addingImage;
    TextView addingLabel;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_empty_list, container, false);

        addingImage = (ImageView) view.findViewById(R.id.addVocabularyImage);
        addingLabel = (TextView) view.findViewById(R.id.noVocabulariesText);

        addingImage.setOnClickListener(getListener());
        addingLabel.setOnClickListener(getListener());

        return view;
    }

    private View.OnClickListener getListener()
    {
        View.OnClickListener listener = null;

        if (getActivity()  instanceof WordsActivity){
            addingLabel.setText(getString(R.string.no_words));
            listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
                }
            };
        }

        if (getActivity()  instanceof TopicsActivity){
            addingLabel.setText(getString(R.string.no_topics));
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment topicDialog = new NewTopicDialog();
                    topicDialog.show(getFragmentManager(), Tags.NEW_TOPIC_DIALOG);
                }
            };
        }

        if (getActivity()  instanceof DictionariesActivity){
            addingLabel.setText(getString(R.string.no_vocabularies));
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DictionariesActivity parent = (DictionariesActivity) getActivity();
                    createDictionaryAlert(parent);
                }
            };
        }
        return listener;
    }

    private void createDictionaryAlert(final DictionariesActivity parent){

        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle(getString(R.string.new_dic_title));

        final EditText input = new EditText(parent);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = input.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.save();

                parent.updateData();
                parent.loadAppropriateFragment();
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





}
