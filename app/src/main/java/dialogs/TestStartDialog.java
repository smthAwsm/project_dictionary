package dialogs;

import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import models.Tags;
import activities.TestingActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 4/27/2016.
 */
public class TestStartDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //getDialog().setTitle("New topic");
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final View testingDialog = inflater.inflate(R.layout.dialog_words_testing, null);

        View.OnClickListener cardClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.testingLevelBeginer:
                        Intent intentBegin = new Intent(getActivity(), TestingActivity.class);
                        intentBegin.putExtra(Tags.TOPIC_TAG, WordsActivity.sCurrentTopicId);
                        intentBegin.putExtra(Tags.TESTING_TYPE_TAG,0);
                        startActivity(intentBegin);
                        dismiss();
                        Log.d("QWE", "1");
                        break;
                    case R.id.testingLevelNormal:
                        Log.d("QWE", "2");
                        Intent intentNorm = new Intent(getActivity(), TestingActivity.class);
                        intentNorm.putExtra(Tags.TOPIC_TAG, WordsActivity.sCurrentTopicId);
                        intentNorm.putExtra(Tags.TESTING_TYPE_TAG,1);
                        startActivity(intentNorm);
                        dismiss();
                        break;
                    case R.id.testingLevelAdvanced:
                        Log.d("QWE", "3");
                        Intent intentAdvan = new Intent(getActivity(), TestingActivity.class);
                        intentAdvan.putExtra(Tags.TOPIC_TAG, WordsActivity.sCurrentTopicId);
                        intentAdvan.putExtra(Tags.TESTING_TYPE_TAG,2);
                        startActivity(intentAdvan);
                        dismiss();
                        break;
                }

            }
        };

            CardView cardBeginner = (CardView) testingDialog.findViewById(R.id.testingLevelBeginer);
            cardBeginner.setOnClickListener(cardClick);
            CardView cardNormal = (CardView) testingDialog.findViewById(R.id.testingLevelNormal);
            cardNormal.setOnClickListener(cardClick);
            CardView cardAdvanced = (CardView) testingDialog.findViewById(R.id.testingLevelAdvanced);
            cardAdvanced.setOnClickListener(cardClick);
            TextView closeButton = (TextView) testingDialog.findViewById(R.id.closeTestDialog);

            closeButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                dismiss();
            }
            });
            return testingDialog;
        }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
