package adapters;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xps.projectdictionary.R;
import dialogs.UpdateWordDialog;
import helpers.GlobalStorage;
import models.Tags;
import models.Word;
import activities.WordsActivity;

import java.util.Locale;


/**
 * Created by XPS on 5/7/2016.
 */
public class WordsRecyclingViewAdapter extends
        RecyclerView.Adapter<WordsRecyclingViewAdapter.ViewHolder> {

    private WordsActivity mContextActivity;
    private TextToSpeech mTextToSpeech;
    private GlobalStorage mGlobalStorage;

    public WordsRecyclingViewAdapter(WordsActivity parent){
        mGlobalStorage = GlobalStorage.getStorage();
        this.mContextActivity = parent;

//        Dictionary dic = mGlobalStorage.getCurrentDictionary();

//        Locale[] azza = Locale.getAvailableLocales();
//        Set<String> lan = new HashSet();
//
//        for (Locale loc : azza)
//            lan.add(loc.getLanguage());



//        mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status == TextToSpeech.SUCCESS) {
//                    mTts.setLanguage(Locale.UK);
//                }
//            }
//        });

        getTTS();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContextActivity);
        View wordView = inflater.inflate(R.layout.fragment_words_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Word word = mGlobalStorage.getWordsData().get(position);
        holder.bindData(word,position);

        holder.mPronounceButton.setTag(position);
        holder.mPronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wordToSpeak = word.getTranslation();
                mTextToSpeech.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        holder.mItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UpdateWordDialog updateWordDialog = new UpdateWordDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(Tags.WORD_TAG, word.getId());
                updateWordDialog.setArguments(bundle);
                updateWordDialog.show( mContextActivity.
                        getActivityFragmentManager(),Tags.UPDATE_WORD_DIALOG);
                return true;
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if(mTextToSpeech != null) mTextToSpeech.shutdown();
    }

    @Override
    public int getItemCount() {
        return mGlobalStorage.getWordsData().size();
    }

    private void getTTS(){
        mTextToSpeech = new TextToSpeech(mContextActivity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    mTextToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View mItemView;
        public ImageView mPronounceButton;
        public FrameLayout mSideShape;

        public TextView mWordTextView;
        public TextView mTranslationTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.mItemView = itemView;
            mPronounceButton = (ImageView) itemView.findViewById(R.id.pronounceButton);
            mWordTextView = (TextView) itemView.findViewById(R.id.wordTextView);
            mTranslationTextView = (TextView) itemView.findViewById(R.id.translationTextView);
            mSideShape = (FrameLayout) itemView.findViewById(R.id.sideShape);
        }

        public void bindData(Word word,int position){
            mWordTextView.setText(word.getValue());
            mTranslationTextView.setText(word.getTranslation());

            int materialColor = mContextActivity.getMaterialColor(position);
            mSideShape.setBackgroundColor(materialColor);
        }
    }
}

