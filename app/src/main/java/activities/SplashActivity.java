package activities;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.List;

import helpers.GoogleDriveHelper;
import models.Tags;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by XPS on 4/9/2016.
 */
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final SplashActivity mContext = this;
    private GoogleDriveHelper mGoogleDriveHelper;

    private static final String PREF_ACCOUNT_NAME = "JUST_LEARN_IT_DICTIONARY";
    private static final int SPLASH_TIME = 2000;
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        SharedPreferences sharedPref = getSharedPreferences(Tags.APP_SETTINGS,MODE_PRIVATE);
        long currentDictionary = sharedPref.getLong(Tags.APP_SETTINGS, -1);
        new BackgroundStartTask(currentDictionary).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode){
        case REQUEST_GOOGLE_PLAY_SERVICES:
            if(requestCode == RESULT_OK ){
                Toast.makeText(
                        this,
                        getString(R.string.no_play_services),
                        Toast.LENGTH_SHORT
                ).show();
            } else mGoogleDriveHelper.callApi();
            break;
        case REQUEST_ACCOUNT_PICKER:
            if(resultCode == RESULT_OK && data!=null && data.getExtras() != null){
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if(accountName != null){
                        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(PREF_ACCOUNT_NAME,accountName);
                        editor.apply();
                        mGoogleDriveHelper.setAccountName(accountName);
                        mGoogleDriveHelper.callApi();
                    }
            }
            break;
        case REQUEST_AUTHORIZATION:
            if(resultCode == RESULT_OK){
                mGoogleDriveHelper.callApi();
            }
            break;
    }
}

    //region PermissionCallbacks
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
//endregion

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
/*                if(mGoogleDriveHelper == null){
                  mGoogleDriveHelper  = new GoogleDriveHelper(mContext);
                }
                else mGoogleDriveHelper.callApi();*/

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
