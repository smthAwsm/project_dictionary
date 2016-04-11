package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XPS on 4/10/2016.
 */


public class Dictionary {

    private static final String LOG_TAG = "DATABASE LOGING";

    private String dictionaryName;
    private String creationDate;

    public Dictionary(String name){
        dictionaryName = name;
        creationDate = getDate();
    }

    public String getName(){
        return dictionaryName;
    }

    public String getCreationDateString(){
        return creationDate.toString();
    }


    public boolean writeToDB(Context context){

        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        Log.d(LOG_TAG,"############ Table inserting ############");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        contentValues.put("name",dictionaryName);
        contentValues.put("created_date", formattedDate);

        long rowID = db.insert("Dictionaries",null,contentValues);
        db.close();

        Log.d(LOG_TAG,"############ ROW " + rowID+" inserted ############");
        return (rowID > 0) ? true : false;
    }

    private String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return  df.format(c.getTime());
    }

}
