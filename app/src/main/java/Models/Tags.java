package Models;

/**
 * Created by XPS on 4/26/2016.
 */
public class Tags {

    public static final String EMPTY_LIST_TAG = "EMPTY";
    public static final String SUCCESS_QUERY_TAG = "GOT";

    public static final String LOG_TAG = "DATABASE LOGING";
    public static final String DICTIONARY_TAG = "DICTIONARY";
    public static final String TOPIC_TAG = "TOPIC";
    public static final String WORD_TAG = "WORD";
    public static final String TOPIC_NAME_TAG = "TOPIC_NAME";
    public static final String NEW_TOPIC_DIALOG = "ADD_TOPIC";
    public static final String NEW_WORD_DIALOG = "ADD_TOPIC";
    public static final String WORD_VALUE_TAG = "GET_WORD";
    public static final String WORD_VALUE_FAKE_1 = "GET_WORD_FAKE_1";
    public static final String WORD_VALUE_FAKE_2 = "GET_WORD_FAKE_2";
    public static final String WORD_VALUE_FAKE_3 = "GET_WORD_FAKE_3";
    public static final String WORD_TRANSLATE_TAG= "GET_TRANSLATE";
    public static final String TESTING_TYPE_TAG= "TESTING_TYPE";
    public static final String MATERIAL_COLORS = "GET_COLORS";

    //DONE Change graphics to SVG
    //DONE Volume up in reading (volume is max)
    //DONE Topics number calculation
    //DONE Check isOnline ASYNC
    //DONE Image storing as values array
    //DONE Image loading (for topics) do Async !!!!
    //DONE Test deleting/editing Dictionaries/Topics/Word for appropriate fragment behavior
    //DONE After deleting all topics fragment doesnt change
    //DONE Fix bug with empty topics/words list
    //DONE Attach FloatingButton to ListView (ListView -> RecyclerView)
    //DONE Load data on object editing (Dictionary - name/ Topic - name,image/ Word - value/translation)
    //DONE After topic adding counter on dictionary is not updated
    //DONE After adding/deleting topic/word is not updated
    //DONE On rename topic crash
    //DONE Fix topics displaying width
    //DONE Fix colors array on words activity
    //DONE Large word text displaying
    //DONE Large word text displaying
    //DONE Color after new word adding
    //DONE Color generation
    //DONE Colors saving before rotation
    //DONE Database data load ASYNC
    //TODO Appropriate DB deleting
    //TODO Crash adding word/topic after rotation (fragment destroyed)
    //TODO Test support on API < 23, fix problems if necessary
    //TODO Fix bottom element size
    //TODO Test large word text displaying after add


    //DONE Yandex API for word translation
    //DONE Input hints
    //TODO Word search
    //TODO Current dictionary saving (Shared Prefs), on app starts load last used dictionary
    //TODO Create first start quick guide
    //TODO Denie not English words in NewWordDialog -> Translation
    //TODO Testing TESTING/WRITING calculate results
    //TODO Settings to change current dictionary


    //TODO To think about multi language fuctionality (More than UKR/RUS -> Eng)
    //TODO LogIn button animate
    //TODO Develop animations, add to project
    //TODO Testing SELF EXAM IMPROVE translation appearence
    //TODO Store some statistics about results
    //TODO Google API Login + Google Drive Backup
    //TODO Check for Material guidelines
    //TODO Change new topic picture
    //TODO Add more images
    //TODO RecyclerView notifying about item not Setting new adapter
}
