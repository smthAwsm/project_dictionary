package Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.List;
import java.util.Locale;

import Dialog.RUDWordDialog;
import Models.Tags;
import Models.Word;
import activities.WordsActivity;

/**
 * Created by XPS on 5/7/2016.
 */
public class WordsRecyclingViewAdapter extends RecyclerView.Adapter<WordsRecyclingViewAdapter.ViewHolder> {

    private List<Word> wordsList;
    int[] shapeColors;
    TextToSpeech textToSpeech;
    WordsActivity parent;
    Context context;

    public WordsRecyclingViewAdapter(List<Word> wordsList, WordsActivity parent, int[] shapeColors){
            this.shapeColors = shapeColors;
            this.wordsList = wordsList;
            this.parent = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        getTTS();

        View wordView = inflater.inflate(R.layout.fragment_words_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Word word = wordsList.get(position);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RUDWordDialog rudDialog = new RUDWordDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.WORD_TAG, word.getId());
                rudDialog.setArguments(bundle);
                rudDialog.show( parent.getActivityFragmentManager(),"RUD");
                Log.d("DIALOG", "TOUCHED" );
                return true;
            }
        });

        holder.sideShape.setBackgroundResource(R.drawable.shape);
        GradientDrawable bgShape = (GradientDrawable) holder.sideShape.getBackground(); //TODO color changing add
        bgShape.setColor(shapeColors[position]);

        final ImageView pronounceButton = holder.pronounceButton;
        pronounceButton.setTag(position);
        pronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = word.getTranslation();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
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

    private void getTTS(){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        public ImageView sideShape;
        public ImageView pronounceButton;
        public TextView wordValue;


        public TextView translationValue;
        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            sideShape = (ImageView) itemView.findViewById(R.id.word_item_side_shape);
            pronounceButton = (ImageView) itemView.findViewById(R.id.pronounceButton);
            wordValue = (TextView) itemView.findViewById(R.id.wordTextView);
            translationValue = (TextView) itemView.findViewById(R.id.translationTextView);
        }

    }
}
