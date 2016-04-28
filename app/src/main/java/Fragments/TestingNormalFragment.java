package Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orm.util.Collection;
import com.study.xps.projectdictionary.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Models.Tags;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestingNormalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String correctTranslation = bundle.getString(Tags.WORD_TRANSLATE_TAG);
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

        RadioButton radioButton1 = (RadioButton) view.findViewById(R.id.testRadioButton1);
        radioButton1.setText(fakeTranslate.get(0));
        RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.testRadioButton2);
        radioButton2.setText(fakeTranslate.get(1));
        RadioButton radioButton3 = (RadioButton) view.findViewById(R.id.testRadioButton3);
        radioButton3.setText(fakeTranslate.get(2));
        RadioButton radioButton4 = (RadioButton) view.findViewById(R.id.testRadioButton4);
        radioButton4.setText(fakeTranslate.get(3));

        //final TextView translateTextView = (TextView) view.findViewById(R.id.translateTextView);

//        translateTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                translateTextView.setBackgroundColor(Color.TRANSPARENT);
//            }
//        });
//

//        translateTextView.setText(bundle.getString(Tags.WORD_TRANSLATE_TAG));

        return view;
    }

}
