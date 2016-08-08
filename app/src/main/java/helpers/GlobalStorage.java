package helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import models.Dictionary;
import models.Language;
import models.TranslateApiLanguage;
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

    private TextToSpeech mTextToSpeech;
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
        if(mDictionariesList == null) updateDictionariesData();
        return mDictionariesList;
    }
    public long getCurrentDictionaryID() {
        return mCurrentDictionaryID;
    }
    public void setCurrentDictionaryID(long currentDictionaryID) {
        mCurrentDictionaryID = currentDictionaryID;
    }
    public Dictionary getCurrentDictionary(){
        List<Dictionary> currentDictionary = Dictionary.find(Dictionary.class,"ID = " + mCurrentDictionaryID);
                //Topic.find(Topic.class,
                //"dictionary_ID = " + currentDictionaryID + "");
        return currentDictionary.get(0);
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
            mTopicList = new ArrayList<>();
        }
    }
    public List<Topic> getTopicsData() {
        if(mTopicList == null) updateTopicsData(mCurrentDictionaryID);
        return mTopicList;
    }
    public long getCurrentTopicId() {
        return mCurrentTopicId;
    }
    public void setCurrentTopicId(long currentTopicId) {
        mCurrentTopicId = currentTopicId;
    }

    public void updateWordsData() {
        if(mWordsList != null){
            mWordsList.clear();
        } else {
            mWordsList = new ArrayList<>();
        }
        try {
            List<Word> wordsFound = Word.find(Word.class, "topic_ID = "+ mCurrentTopicId +"");
            mWordsList.addAll(wordsFound);
        } catch (Exception e){
            mWordsList = new ArrayList<>();
        }
    }
    public List<Word> getWordsData() {
        if(mWordsList == null) updateWordsData();
        return mWordsList;
    }

    public List<Language> getUsedLanguagesData(Context context){
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

            List<Language> languagesList = new ArrayList<>();
            for (int i = 0; i < languageRecources.length(); i++ ){
                TranslateApiLanguage language = TranslateApiLanguage.fromString(languageStrings[i]);
                Integer flagImageId = flagRecources.getResourceId(i,0);
                languagesList.add(new Language(language,flagImageId));
            }
            languageRecources.recycle();
            flagRecources.recycle();
            return languagesList;
        }
        return null;
    }

    public void loadSupportedLanguagesData(final Context context){
        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale[] locales = Locale.getAvailableLocales();
                Set<String> localeSet = new HashSet<>();
                for (Locale locale : locales) {
                    int res = mTextToSpeech.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeSet.add(locale.getLanguage());
                    }
                }
                mTextToSpeech.shutdown();
                int i = 0;

                List<Language> usedLanguagesList = getUsedLanguagesData(context);
                for (Language lan : usedLanguagesList){
                    String language = lan.getLanguage().toString();
                    for (String supportedLanguage : localeSet){
                        if(language.equals(supportedLanguage)){
                            mLanguagesList.add(lan);
                        }
                    }
                }
            }
        });
    }

    public List<Language> getLanguagesData() {
        if (mLanguagesList != null && mLanguagesList.size() > 0) {
            return mLanguagesList;
        } else return mLanguagesList = new ArrayList<>();
    }
}
