package Models;


import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XPS on 4/10/2016.
 */


public class Dictionary extends SugarRecord{

    private static final String LOG_TAG = "DATABASE LOGING";

    //private Long id;
    private String dictionaryName;
    private String creationDate;

    public Dictionary(){

    }

    public Dictionary(String name){
        dictionaryName = name;
        creationDate = getDate();
    }

    public Dictionary(int dsa,String asd,String as){ //TODO delete

    }

    //public Long getId() {
    //    return id;
    //}
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
