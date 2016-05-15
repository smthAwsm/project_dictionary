package Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

    private List<Word> wordData;
    int[] shapeColors;
    TextToSpeech textToSpeech;
    WordsActivity parent;
    Context context;

    public WordsRecyclingViewAdapter(List<Word> wordData, WordsActivity parent){
            this.wordData = wordData;
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
        final Word word = wordData.get(position);
        shapeColors = getMatColor();
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
        return wordData.size();
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

    private int[] getMatColor()
    {
        int[] returnColor = new int[]{ Color.BLACK };
        int arrayId = parent.getResources().getIdentifier("mdcolors" , "array", parent.getPackageName());

        if (arrayId != 0)
        {
            int size = wordData.size();
            TypedArray colors = parent.getResources().obtainTypedArray(arrayId);

            if (shapeColors == null){
                returnColor = new int[size];
                for (int i = 0; i < size; i++){
                    int index = (int) (Math.random() * colors.length());
                    returnColor[i] = colors.getColor(index, Color.BLACK);
                }
            }
            else{
                int [] temp = new int[size - shapeColors.length];
                returnColor = new int[size+temp.length];

                for (int i = 0; i < size - shapeColors.length; i++){
                    int index = (int) (Math.random() * colors.length());
                    temp[i] = colors.getColor(index, Color.BLACK);
                }

                System.arraycopy(shapeColors,0, returnColor,0,shapeColors.length);
                System.arraycopy(temp,0, returnColor,shapeColors.length,temp.length);
            }
        }
        return returnColor;
    }
}
