package Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Models.Word;

/**
 * Created by XPS on 5/7/2016.
 */
public class WordsRecyclingViewAdapter extends RecyclerView.Adapter<WordsRecyclingViewAdapter.ViewHolder> {

    private List<Word> wordsList;

    public WordsRecyclingViewAdapter(List<Word> words){
        wordsList = words;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View wordView = inflater.inflate(R.layout.fragment_words_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Word word = wordsList.get(position);

         ImageView sideShape = holder.sideShape;
         sideShape.setBackgroundResource(R.drawable.shape);
         //GradientDrawable bgShape = (GradientDrawable) holder.sideShape.getBackground(); //TODO color changing add
         //bgShape.setColor(shapeColors[(int)holder.pronounceButton.getTag()]);

        final ImageView pronounceButton = holder.pronounceButton;
        pronounceButton.setTag(position);
        pronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) pronounceButton.getTag();
                String toSpeak = word.getTranslation();
                //textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

         TextView wordValue = holder.wordValue;
         wordValue.setText(word.getValue());
         TextView translationValue = holder.translationValue;
         translationValue.setText(word.getTranslation());
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView sideShape;
        public ImageView pronounceButton;
        public TextView wordValue;
        public TextView translationValue;


        public ViewHolder(View itemView) {
            super(itemView);

            sideShape = (ImageView) itemView.findViewById(R.id.word_item_side_shape);
            pronounceButton = (ImageView) itemView.findViewById(R.id.pronounceButton);
            wordValue = (TextView) itemView.findViewById(R.id.wordTextView);
            translationValue = (TextView) itemView.findViewById(R.id.translationTextView);
        }
    }
}
