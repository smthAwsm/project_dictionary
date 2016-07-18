package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Tags;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestingNormalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        final String correctTranslation = bundle.getString(Tags.WORD_TRANSLATE_TAG);
        String wordValue = bundle.getString(Tags.WORD_VALUE_TAG);
        List<String> fakeTranslate = new ArrayList<String>();
        fakeTranslate.add(correctTranslation);
        fakeTranslate.add(bundle.getString(Tags.WORD_VALUE_FAKE_1));
        fakeTranslate.add(bundle.getString(Tags.WORD_VALUE_FAKE_2));
        fakeTranslate.add(bundle.getString(Tags.WORD_VALUE_FAKE_3));

        Collections.shuffle(fakeTranslate);

        View view = inflater.inflate(R.layout.fragment_testing_normal, container, false);
        TextView wordTextView = (TextView) view.findViewById(R.id.normalWordtextView);
        wordTextView.setText(wordValue);

        final RadioButton radioButton1 = (RadioButton) view.findViewById(R.id.testRadioButton1);
        radioButton1.setText(fakeTranslate.get(0));
        final RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.testRadioButton2);
        radioButton2.setText(fakeTranslate.get(1));
        final RadioButton radioButton3 = (RadioButton) view.findViewById(R.id.testRadioButton3);
        radioButton3.setText(fakeTranslate.get(2));
        final RadioButton radioButton4 = (RadioButton) view.findViewById(R.id.testRadioButton4);
        radioButton4.setText(fakeTranslate.get(3));

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.optionRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
             switch (checkedId){
                 case R.id.testRadioButton1:
                     if(radioButton1.getText() == correctTranslation)
                         radioButton1.setTextColor(getResources().getColor(R.color.right));
                     else radioButton1.setTextColor(getResources().getColor(R.color.wrong));

                     radioButton2.setTextColor(getResources().getColor(R.color.separator));
                     radioButton3.setTextColor(getResources().getColor(R.color.separator));
                     radioButton4.setTextColor(getResources().getColor(R.color.separator));

                     break;
                 case R.id.testRadioButton2:
                     if(radioButton2.getText() == correctTranslation)
                         radioButton2.setTextColor(getResources().getColor(R.color.right));
                     else radioButton2.setTextColor(getResources().getColor(R.color.wrong));

                     radioButton1.setTextColor(getResources().getColor(R.color.separator));
                     radioButton3.setTextColor(getResources().getColor(R.color.separator));
                     radioButton4.setTextColor(getResources().getColor(R.color.separator));

                     break;
                 case R.id.testRadioButton3:
                     if(radioButton3.getText() == correctTranslation)
                         radioButton3.setTextColor(getResources().getColor(R.color.right));
                     else radioButton3.setTextColor(getResources().getColor(R.color.wrong));

                     radioButton2.setTextColor(getResources().getColor(R.color.separator));
                     radioButton1.setTextColor(getResources().getColor(R.color.separator));
                     radioButton4.setTextColor(getResources().getColor(R.color.separator));
                     break;
                 case R.id.testRadioButton4:
                     if(radioButton4.getText() == correctTranslation)
                         radioButton4.setTextColor(getResources().getColor(R.color.right));
                     else radioButton4.setTextColor(getResources().getColor(R.color.wrong));

                     radioButton2.setTextColor(getResources().getColor(R.color.separator));
                     radioButton3.setTextColor(getResources().getColor(R.color.separator));
                     radioButton1.setTextColor(getResources().getColor(R.color.separator));
                     break;
             }
            }
        });


        return view;
    }

}
