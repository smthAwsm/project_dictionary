package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import Models.Tags;

/**
 * Created by XPS on 4/28/2016.
 */
public class TestingAdvancedFragment extends Fragment {
     @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String wordValue = bundle.getString(Tags.WORD_VALUE_TAG);
        String wordTranslate = bundle.getString(Tags.WORD_TRANSLATE_TAG);
         View view = inflater.inflate(R.layout.fragment_testing_advanced,container,false);

         TextView wordValueView = (TextView) view.findViewById(R.id.valueTextView);
         wordValueView.setText(wordValue);

         EditText translateValueView = (EditText) view.findViewById(R.id.translationEdit);

        return view;
    }


}
