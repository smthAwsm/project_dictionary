package models;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XPS on 4/10/2016.
 */

public class Dictionary extends SugarRecord{

    private String dictionaryName;
    private String creationDate;

    private String languageFrom;
    private String translationTo;

    public Dictionary(){ }

    public Dictionary(String name){
        dictionaryName = name;
        creationDate = getDate();
    }

    public Dictionary(String name,String languageFrom, String translationTo) {
        this.dictionaryName = name;
        creationDate = getDate();
        this.languageFrom = languageFrom;
        this.translationTo = translationTo;
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
        try {
            return df.format(c.getTime());
        }   catch (Exception e){
            return df.format(new Date("1970-01-01"));
        }
    }


    public String getLanguageFrom() {
        return languageFrom;
    }

    public String getTranslationTo() {
        return translationTo;
    }
}
