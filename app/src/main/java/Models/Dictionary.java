package models;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by XPS on 4/10/2016.
 */

public class Dictionary extends SugarRecord{

    private String dictionaryName;
    private String creationDate;
    //private Languages languageFrom;
    //private Languages TranslationTo;

    public Dictionary(){

    }

    public Dictionary(String name){
        dictionaryName = name;
        creationDate = getDate();
    }

    public String getName(){
        return dictionaryName;
    }
    public void setDictionaryName(String newName){dictionaryName = newName;}
    public String getCreationDateString(){
        return creationDate.toString();
    }

    private String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return  df.format(c.getTime());
    }
}
