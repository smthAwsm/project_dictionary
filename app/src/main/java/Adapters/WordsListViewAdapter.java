package Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.study.xps.projectdictionary.R;

import java.util.List;
import java.util.Locale;

import activities.WordsActivity;
import Models.Word;

/**
 * Created by XPS on 4/26/2016.
 */
public class WordsListViewAdapter extends BaseAdapter {
    private static Context context;
    private static int[] shapeColors;
    private static List<Word> wordsList;
    private LayoutInflater inflater;
    private TextToSpeech textToSpeech;
    private int currentPosition;

    public WordsListViewAdapter(WordsActivity wordsActivity, List<Word> data, int[] colors){
        wordsList = data;
        context = wordsActivity;
        shapeColors = colors;
        inflater = (LayoutInflater)context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getTTS();
    }

    @Override
    public int getCount() {
        return wordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public long getWord(int position) {
        return wordsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View listView = inflater.inflate(R.layout.fragment_words_list_item,null);

        holder.pronounceButton = (ImageView) listView.findViewById(R.id.pronounceButton);
        holder.pronounceButton.setTag(position);
        holder.pronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) holder.pronounceButton.getTag();
                String toSpeak = wordsList.get(pos).getTranslation();

                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        holder.sideShape = (ImageView) listView.findViewById(R.id.word_item_side_shape);
        holder.sideShape.setBackgroundResource(R.drawable.shape);
        GradientDrawable bgShape = (GradientDrawable) holder.sideShape.getBackground();
        bgShape.setColor(shapeColors[(int)holder.pronounceButton.getTag()]);

        holder.wordTextView = (TextView) listView.findViewById(R.id.wordTextView);
        holder.translateTextView= (TextView) listView.findViewById(R.id.translationTextView);

        holder.wordTextView.setText(wordsList.get(position).getValue());
        holder.translateTextView.setText(wordsList.get(position).getTranslation());

        return listView;
    }

    private class Holder
    {
        TextView wordTextView;
        TextView translateTextView;
        ImageView sideShape;
        ImageView pronounceButton;
    }

    private void getTTS(){
        textToSpeech =new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }


}


