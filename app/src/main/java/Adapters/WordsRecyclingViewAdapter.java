package Adapters;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private List<Integer> materialColors;
    private WordsActivity context;

    public WordsRecyclingViewAdapter(List<Word> wordData, WordsActivity parent){
            this.wordData = wordData;
            this.context = parent;
            getTTS();
    }


    private TextToSpeech textToSpeech;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordView = inflater.inflate(R.layout.fragment_words_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Word word = wordData.get(position);

        holder.bindData(word,position);

        final ImageView pronounceButton = holder.pronounceButton;
        pronounceButton.setTag(position);
        pronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = word.getTranslation();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RUDWordDialog rudDialog = new RUDWordDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.WORD_TAG, word.getId());
                rudDialog.setArguments(bundle);
                rudDialog.show( context.getActivityFragmentManager(),"RUD");
                Log.d("DIALOG", "TOUCHED" );
                return true;
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public ImageView pronounceButton;
        public TextView wordValue;
        public FrameLayout sideShape;

        public TextView translationValue;
        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            pronounceButton = (ImageView) itemView.findViewById(R.id.pronounceButton);
            wordValue = (TextView) itemView.findViewById(R.id.wordTextView);
            translationValue = (TextView) itemView.findViewById(R.id.translationTextView);

            sideShape = (FrameLayout) itemView.findViewById(R.id.sideShape);
        }

        public void bindData(Word word,int position){

            int materialColor = context.getMaterialColor(position);
            sideShape.setBackgroundColor(materialColor);

            wordValue.setText(word.getValue());
            translationValue.setText(word.getTranslation());
        }
    }
}
