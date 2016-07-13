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
    public void onBindViewHolder(final ViewHolder holder, int position) {

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
        public FrameLayout sideShape;

        public TextView wordValue;
        public TextView translationValue;

        private CountingLayoutListener mTranslationLayoutListener;
        private CountingLayoutListener mWordLayoutListener;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            pronounceButton = (ImageView) itemView.findViewById(R.id.pronounceButton);
            wordValue = (TextView) itemView.findViewById(R.id.wordTextView);
            translationValue = (TextView) itemView.findViewById(R.id.translationTextView);
            sideShape = (FrameLayout) itemView.findViewById(R.id.sideShape);

            mTranslationLayoutListener = new CountingLayoutListener(translationValue);
            translationValue.addOnLayoutChangeListener(mTranslationLayoutListener);
            mWordLayoutListener = new CountingLayoutListener(wordValue,mTranslationLayoutListener);
            wordValue.addOnLayoutChangeListener(mWordLayoutListener);
        }

        public void bindData(Word word,int position){

            wordValue.setText(word.getValue());
            translationValue.setText(word.getTranslation());

            int materialColor = context.getMaterialColor(position);
            sideShape.setBackgroundColor(materialColor);

            //int i = mTranslationLayoutListener.getLineCount();
            //int m = mTranslationLayoutListener.getLineCount();


            //Log.e("********* Word counted","Lines counted"+i);
            //Log.e("@@@ Translation counted","Lines counted"+m);
            //wordListener.get

//
//              //  wordValue.addOnLayoutChangeListener();
//            //} else {
//                final int lineCountWord = (wordValue.getLineCount() > 1) ?
//                        wordValue.getLineCount() : 0;
//                final int lineCountTranslate = (translationValue.getLineCount() > 1) ?
//                        translationValue.getLineCount() : 0;
            }
        }




//        View.OnLayoutChangeListener wordListener = new View.OnLayoutChangeListener() {
//
//
//            public int getLineCountWord() {
//                return lineCountWord;
//            }
//
//            public int lineCountWord;
//
//            @Override
//            public void onLayoutChange(final View v, final int left, final int top,
//                                       final int right, final int bottom, final int oldLeft,
//                                       final int oldTop, final int oldRight, final int oldBottom) {
//                //wordValue.removeOnLayoutChangeListener(this);
//
//                //lineCountWord = (wordValue.getLineCount() > 1) ?
//                //        wordValue.getLineCount() : 0;
//
////                    final int lineCountTranslate = (translationValue.getLineCount() > 1) ?
////                            translationValue.getLineCount() : 0;
//
////                    CardView cardView = (CardView) itemView.findViewById(R.id.wordCard);
////                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
//
////                    int additionalValue = (layoutParams.height / 3) * (lineCountWord + lineCountTranslate);
////                    int height = layoutParams.height + additionalValue;
////                    layoutParams.height = 0;
////                    layoutParams.height = height;
////                    //Log.e("RESULT",layoutParams.height+"");
////                    Log.e("Word Count",lineCountWord+"");
////                    Log.e("Translate Count",lineCountTranslate+"");
//                //cardView.setLayoutParams(layoutParams);
//            }
//        };



//        View.OnLayoutChangeListener translateListener = new View.OnLayoutChangeListener() {
//            public int lineCountWord;
//            @Override
//            public void onLayoutChange(final View v, final int left, final int top,
//                                       final int right, final int bottom, final int oldLeft,
//                                       final int oldTop, final int oldRight, final int oldBottom) {
////                translationValue.removeOnLayoutChangeListener(this);
////
////                lineCountWord = (translationValue.getLineCount() > 1) ?
////                        translationValue.getLineCount() : 0;
//            }
//        };

        class CountingLayoutListener implements View.OnLayoutChangeListener{

            private int mLineCount;
            private TextView mCountedTextView;
            private CountingLayoutListener mListenerToCheck = null;

            public CountingLayoutListener(TextView textViewValue) {
                this.mCountedTextView = textViewValue;
            }

            public CountingLayoutListener(TextView textViewValue,CountingLayoutListener listenerToCheck) {
                this.mCountedTextView = textViewValue;
                mListenerToCheck = listenerToCheck;
            }

            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                //mTranslationTextView.removeOnLayoutChangeListener(this);
                mLineCount =  mCountedTextView.getLineCount();//(translationValue.getLineCount() > 1) ? translationValue.getLineCount() : 0;

                if(mListenerToCheck != null) {
                    Log.d("************* COUNTED", "Word count " + mListenerToCheck.getLineCount()
                                                +"Translate count " + getLineCount());
                    fitCard();
                }
            }

            public int getLineCount() {
                return mLineCount;
            }
            public void fitCard() {
                Log.e("&&&&&&&&&&&&&&&","CARD FITTED!!!");
            }
        }


}

