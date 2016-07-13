package activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.List;

import Helpers.GoogleDriveHelper;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by XPS on 4/9/2016.
 */
public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "JUST_LEARN_IT_DICTIONARY";
    private final LoginActivity mContext = this;
    private GoogleDriveHelper mGoogleDriveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        ImageButton loginButton = (ImageButton) findViewById(R.id.googleLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(mGoogleDriveHelper == null)
//                 mGoogleDriveHelper  = new GoogleDriveHelper(mContext);
//                else mGoogleDriveHelper.callApi();

                startActivity(new Intent(getApplicationContext(),DictionariesActivity.class));
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
}
