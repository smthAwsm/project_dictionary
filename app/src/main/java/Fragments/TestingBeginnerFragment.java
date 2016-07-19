package fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;
import models.Tags;


/**
 * Created by XPS on 4/28/2016.
 */
public class TestingBeginnerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        View view = inflater.inflate(R.layout.fragment_testing_beginner, container, false);
        TextView wordTextView = (TextView) view.findViewById(R.id.wordTextView);
        final TextView translateTextView = (TextView) view.findViewById(R.id.translateTextView);

        translateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateTextView.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        try {
            wordTextView.setText(bundle.getString(Tags.WORD_VALUE_TAG));
            translateTextView.setText(bundle.getString(Tags.WORD_TRANSLATE_TAG));
        } catch (Exception e){
            throw new RuntimeException();
        }

        return view;
    }

}
