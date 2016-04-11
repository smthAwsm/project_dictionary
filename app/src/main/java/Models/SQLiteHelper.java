package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import Models.Dictionary;

/**
 * Created by XPS on 4/10/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    private final String LOG_TAG = "SQLiteHelper LOG";
    ContentValues contentValues;

    public SQLiteHelper(Context context){
        super(context, "DictionariesDB",null,1);
        //contentValues = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG,"OnCreate DB touched!");

        db.execSQL("create table  Dictionaries(" +
                "id integer primary key autoincrement," +
                "name text," +
                "created_date text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
