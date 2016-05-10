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

    //DONE Change graphics to SVG
    //DONE Volume up in reading (volume is max)
    //DONE Topics number calculation
    //DONE Denie not English words in NewWordDialog -> Translation
    //DONE Image storing as values array
    //DONE Image loading (for topics) do Async !!!!
    //TODO Test deleting/editing Dictionaries/Topics/Word for appropriate fragment behavior
    //TODO After deleting all topics fregment doesnt change
    //TODO Fix bug with empty topics/words list
    //TODO Fix colors array on words activity
    //DONE Attach FloatingButton to ListView (ListView -> RecyclerView)
    //DONE Load data on object editing (Dictionary - name/ Topic - name,image/ Word - value/translation)


    //DONE Yandex API for word translation
    //TODO Input hints
    //TODO Database data load ASYNC
    //TODO Word search
    //TODO Testing TESTING/WRITING calculate results
    //TODO Current dictionary saving (Shared Prefs), on app starts load last used dictionary
    //TODO Settings to change current dictionary
    //TODO Create first start quick guide


    //TODO Test support on API < 23, fix problems if necessary
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
