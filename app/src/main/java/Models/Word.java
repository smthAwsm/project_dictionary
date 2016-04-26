package Models;

import com.orm.SugarRecord;

/**
 * Created by XPS on 4/22/2016.
 */
public class Word extends SugarRecord{

    private static final String LOG_TAG = "DATABASE LOGING";

    //private Long id;
    private long topicID;
    private String value;
    private String translation;
    //private String assocValue;


    public Word(){

    }

    public Word(long topicId,String value,String translation){
        this.topicID = topicId;
        this.value = value;
        this.translation = translation;
        //this.assocValue = assocValue;
    }

    public String getValue(){
        return value;
    }
    public String getTranslation(){
        return translation;    }

    public void setValue(String newValue){value = newValue;}
    public void setTranslation(String newTranslation){translation = newTranslation;}
}
