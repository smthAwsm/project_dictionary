package helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.study.xps.projectdictionary.R;

import java.util.Arrays;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by XPS on 07/10/2016.
 */
public class GoogleDriveHelper {


    private GoogleAccountCredential mAccountCredential;
    private Activity mContext;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "JUST_LEARN_IT_DICTIONARY";
    private static final String[] SCOPES = {DriveScopes.DRIVE_METADATA_READONLY};

    public GoogleDriveHelper(Activity context){
                this.mContext = context;
        mAccountCredential = GoogleAccountCredential.usingOAuth2(
                mContext,
                Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        callApi();
    }

    public void callApi(){
        if(!isGooglePlayAvailable()){
            acquireGooglePlayServices();
        } else if(mAccountCredential.getSelectedAccountName() == null){
            chooseAccout();
        } else if (!isDeviceOnline()) {
            Toast.makeText(mContext, mContext.getString(R.string.no_inet), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(mContext,"TASK START",Toast.LENGTH_LONG).show();
        //else new MakeRequestTask
    }

    private boolean isGooglePlayAvailable(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(mContext);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }
    private void acquireGooglePlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(mContext);
        if(googleApiAvailability.isUserResolvableError(connectionStatusCode))
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
    }
    private void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = googleApiAvailability.getErrorDialog(
                mContext,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private boolean isDeviceOnline(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager .getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccout(){
        if(EasyPermissions.hasPermissions(mContext, android.Manifest.permission.GET_ACCOUNTS)){
            String accountName = mContext.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME,null);
            if(accountName != null){
                mAccountCredential.setSelectedAccountName(accountName);
                callApi();
            } else {
                mContext.startActivityForResult(
                        mAccountCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
                EasyPermissions.requestPermissions(
                        mContext,
                        mContext.getString(R.string.account_permission),
                        REQUEST_PERMISSION_GET_ACCOUNTS,
                        android.Manifest.permission.GET_ACCOUNTS);
            }
        }

    public void setAccountName(String accountName) {
        mAccountCredential.setSelectedAccountName(accountName);
    }

}


