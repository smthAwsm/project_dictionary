package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.study.xps.projectdictionary.R;

import models.Tags;

/**
 * Created by XPS on 4/9/2016.
 */
public class SplashActivity extends DriveOperationsActivity {

    private static final int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        SharedPreferences sharedPref = getSharedPreferences(Tags.APP_SETTINGS,MODE_PRIVATE);
        long currentDictionary = sharedPref.getLong(Tags.APP_SETTINGS, -1);
        new BackgroundStartTask(currentDictionary).execute();
    }

    private class BackgroundStartTask extends AsyncTask {
        private long mDictionaryId;

        BackgroundStartTask(long dictionaryId){
            this.mDictionaryId = dictionaryId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(mDictionaryId == -1){
                startActivity(new Intent(getApplicationContext(),IntroActivity.class));
            } else {
                Intent dictionaryTopicsIntent = new Intent(getApplicationContext(),
                        TopicsActivity.class);
                dictionaryTopicsIntent.putExtra(Tags.DICTIONARY_TAG,mDictionaryId);
                startActivity(dictionaryTopicsIntent);
            }
            finish();
        }
    }
}
