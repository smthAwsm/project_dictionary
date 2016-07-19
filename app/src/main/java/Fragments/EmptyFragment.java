package fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;
import dialogs.NewTopicDialog;
import dialogs.NewWordDialog;
import models.Dictionary;
import models.Tags;
import activities.DictionariesActivity;
import activities.TopicsActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 4/9/2016.
 */
public class EmptyFragment extends Fragment {

    private ImageView mAddingImage;
    private TextView mAddingLabel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_empty_list, container, false);

        mAddingImage = (ImageView) view.findViewById(R.id.addVocabularyImage);
        mAddingLabel = (TextView) view.findViewById(R.id.noVocabulariesText);

        mAddingImage.setOnClickListener(getListener());
        mAddingLabel.setOnClickListener(getListener());

        return view;
    }

    private View.OnClickListener getListener()
    {
        View.OnClickListener listener = null;

        if (getActivity()  instanceof WordsActivity){
            mAddingLabel.setText(getString(R.string.no_words));
            listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment wordDialog = new NewWordDialog();
                wordDialog.show(getFragmentManager(), Tags.NEW_WORD_DIALOG);
                }
            };
        }

        if (getActivity()  instanceof TopicsActivity){
            mAddingLabel.setText(getString(R.string.no_topics));
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment topicDialog = new NewTopicDialog();
                    topicDialog.show(getFragmentManager(), Tags.NEW_TOPIC_DIALOG);
                }
            };
        }

        if (getActivity()  instanceof DictionariesActivity){
            mAddingLabel.setText(getString(R.string.no_dictionaries));
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

    private void createDictionaryAlert(final DictionariesActivity contextActivity){

        AlertDialog.Builder builder = new AlertDialog.Builder(contextActivity);
        builder.setTitle(getString(R.string.new_dict_title));

        final EditText mDictionaryNameEdit = new EditText(contextActivity);
        mDictionaryNameEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(mDictionaryNameEdit);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictionaryName = mDictionaryNameEdit.getText().toString();
                Dictionary newDictionary = new Dictionary(dictionaryName);
                newDictionary.save();

                contextActivity.updateData();
                contextActivity.loadAppropriateFragment();
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
