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
import models.Tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by XPS on 4/28/2016.
 */
public class TestingNormalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        final String correctTranslation = bundle.getString(Tags.WORD_TRANSLATE_TAG);

        String wordValue = bundle.getString(Tags.WORD_VALUE_TAG);
        List<String> fakeTranslate = new ArrayList<>();
        fakeTranslate.add(correctTranslation);
        fakeTranslate.add(bundle.getString(Tags.WORD_VALUE_FAKE_1));
        fakeTranslate.add(bundle.getString(Tags.WORD_VALUE_FAKE_2));
        fakeTranslate.add(bundle.getString(Tags.WORD_VALUE_FAKE_3));

        Collections.shuffle(fakeTranslate);

        View view = inflater.inflate(R.layout.fragment_testing_normal, container, false);
        TextView wordTextView = (TextView) view.findViewById(R.id.normalWordtextView);
        wordTextView.setText(wordValue);

        final RadioButton [] radioButtons = new RadioButton[]{
                (RadioButton) view.findViewById(R.id.testRadioButton1),
                (RadioButton) view.findViewById(R.id.testRadioButton2),
                (RadioButton) view.findViewById(R.id.testRadioButton3),
                (RadioButton) view.findViewById(R.id.testRadioButton4)
        };

        for(int i = 0;i < radioButtons.length; i++){
            radioButtons[i].setText(fakeTranslate.get(i));
        }

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.optionRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.testRadioButton1:
                        answerCheck(radioButtons,radioButtons[0],correctTranslation);
                        break;
                    case R.id.testRadioButton2:
                        answerCheck(radioButtons,radioButtons[1],correctTranslation);
                        break;
                    case R.id.testRadioButton3:
                        answerCheck(radioButtons,radioButtons[2],correctTranslation);
                        break;
                    case R.id.testRadioButton4:
                        answerCheck(radioButtons,radioButtons[3],correctTranslation);
                        break;
                }
            }
        });
        return view;
    }

    private void answerCheck(RadioButton[] radioButtons,
                             RadioButton radioToCheck,String correctTranslation){
        for (RadioButton rBtn : radioButtons ) {
            if (rBtn == radioToCheck){
                if(radioToCheck.getText() == correctTranslation) {
                    rBtn.setTextColor(getResources().getColor(R.color.right));
                } else rBtn.setTextColor(getResources().getColor(R.color.wrong));
            } else rBtn.setTextColor(getResources().getColor(R.color.separator));
        }
    }
}
