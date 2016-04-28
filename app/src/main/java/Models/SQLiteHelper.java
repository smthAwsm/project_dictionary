package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Models.Dictionary;

/**
 * Created by XPS on 4/10/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    private final String LOG_TAG = "SQLiteHelper LOG";
   ContentValues contentValues;

    public SQLiteHelper(Context context){
        super(context, "DictionariesDB", null,1);
        contentValues = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG,"OnCreate DB touched!");

        db.execSQL("create table Dictionaries ("
                +"id integer primary key autoincrement," +
                "name text," +
                "created_date text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Dictionary> getDictionaries(){

        List<Dictionary> dictionaryList =  new ArrayList<Dictionary>();

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

       // if (this.tableExists("Dictionaries")) {
            Cursor cursorDictionaries = db.query("Dictionaries", null, null, null, null, null, null);

            if (cursorDictionaries.moveToFirst()) {

                int idColIndex = cursorDictionaries.getColumnIndex("id");
                int nameColIndex = cursorDictionaries.getColumnIndex("name");
                int dateColIndex = cursorDictionaries.getColumnIndex("created_date");

                do {
                    Dictionary dictionary = new Dictionary(
                            cursorDictionaries.getInt(idColIndex),
                            cursorDictionaries.getString(nameColIndex),
                            cursorDictionaries.getString(dateColIndex));
                    dictionaryList.add(dictionary);
                } while (cursorDictionaries.moveToNext());
                Log.i(LOG_TAG, "############ Dictionaries selected ############");
            } else Log.e(LOG_TAG, "0 Rows ");
            db.close();
            return dictionaryList;
        }
            //else Log.e(LOG_TAG, "############ No Dictionaries Table ############");
       // return dictionaryList;
   // }

    public void dropTable(String name,DataModels model){

            SQLiteDatabase db = this.getWritableDatabase();
            switch (model) {
                case Dictionary:
                    try { db.execSQL("DROP TABLE Dictionaries");}
                     catch (Exception e){
                        Log.e(LOG_TAG,"DROP FAILED "+ e.getMessage());}
                    db.close();
                    break;
                case Word:
                    db.execSQL("DROP TABLE " + name);
                    db.close();
                    break;
            }
        }
    public boolean tableExists(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String existQuery ="SELECT " + tableName +" FROM sqlite_master WHERE type='table'";
            Cursor check = db.rawQuery(existQuery,null);
            if (check.getColumnCount() > 0){
            Log.i(LOG_TAG,"TABLE " + tableName + " exists");
            return true;
            }
            else {
                Log.e(LOG_TAG,"CHECK FAILED "+tableName);
                return false;
            }
        }
        catch (Exception e){
            Log.e(LOG_TAG,"CHECK FAILED "+tableName +" " + e.getMessage());
        return false;
        }
    }



    public static enum DataModels{
        Dictionary,
        Category,
        Word
    }
}
