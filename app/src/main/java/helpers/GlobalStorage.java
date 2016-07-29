package helpers;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

import models.Dictionary;
import models.Language;
import models.Languages;
import models.Topic;
import models.Word;

/**
 * Created by XPS on 07/28/2016.
 */
public class GlobalStorage {
    private static GlobalStorage sStorage;
    private List<Language> mLanguagesList;
    private List<Dictionary> mDictionariesList;
    private List<Topic> mTopicList;
    private List<Word> mWordsList;

    private long mCurrentDictionaryID;
    private long mCurrentTopicId;

    private GlobalStorage(){}

    public static GlobalStorage getStorage(){
        if(sStorage == null){
            synchronized (GlobalStorage.class){
                if (sStorage == null){
                    sStorage = new GlobalStorage();
                }
            }
        }
    return sStorage;
    }

    public void updateDictionariesData(){
        mDictionariesList = Dictionary.listAll(Dictionary.class);
    }
    public List<Dictionary> getDictionariesData() {
        return mDictionariesList;
    }
    public long getCurrentDictionaryID() {
        return mCurrentDictionaryID;
    }
    public void setCurrentDictionaryID(long currentDictionaryID) {
        mCurrentDictionaryID = currentDictionaryID;
    }

    public void updateTopicsData(long currentDictionaryID ) {
        if(mTopicList != null){
            mTopicList.clear();
        } else {
            mTopicList = new ArrayList<>();
        }
        try {
            List<Topic> topicsFound = Topic.find(Topic.class,
                    "dictionary_ID = " + currentDictionaryID + "");
            mTopicList.addAll(topicsFound);
        } catch (Exception e) {
            mTopicList = new ArrayList<Topic>();
        }
    }
    public List<Topic> getTopicsData() {
        return mTopicList;
    }
    public long getCurrentTopicId() {
        return mCurrentTopicId;
    }
    public void setCurrentTopicId(long currentTopicId) {
        mCurrentTopicId = currentTopicId;
    }

    public void updateWordsData() {
        try {
            List<Word> wordsFound = Word.find(Word.class, "topic_ID = "+ mCurrentTopicId +"");
            mWordsList.clear();
            mWordsList.addAll(wordsFound);
        } catch (Exception e){
            mWordsList = new ArrayList<Word>();
        }
    }
    public List<Word> getWordsData() {
        return mWordsList;
    }

    private void fillLanguagesData(Context context){
        int arrayLanguagesId = context.getResources().
                getIdentifier("languages" , "array", context.getPackageName());
        int arrayFlagsId = context.getResources().
                getIdentifier("flags" , "array", context.getPackageName());

        if (arrayLanguagesId != 0 && arrayFlagsId !=0)
        {
            TypedArray languageRecources = context.getResources().
                    obtainTypedArray(arrayLanguagesId);
            String[] languageStrings = languageRecources.getResources().
                    getStringArray(arrayLanguagesId);
            TypedArray flagRecources = context.getResources().
                    obtainTypedArray(arrayFlagsId);

            mLanguagesList = new ArrayList<>();
            for (int i = 0; i < languageRecources.length(); i++ ){
                Languages language = Languages.fromString(languageStrings[i]);
                Integer flagImageId = flagRecources.getResourceId(i,0);
                mLanguagesList.add(new Language(language,flagImageId));
            }
            languageRecources.recycle();
            flagRecources.recycle();
        }
    }

    public List<Language> getLanguagesList(Context context){
        if(mLanguagesList == null || !(mLanguagesList.size() > 0)){
            fillLanguagesData(context);
            return mLanguagesList;
        } else return mLanguagesList;
    }
}
