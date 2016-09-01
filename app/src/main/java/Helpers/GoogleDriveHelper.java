package helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.study.xps.projectdictionary.R;

import java.util.Arrays;

import activities.DriveOperationsActivity;

/**
 * Created by XPS on 07/10/2016.
 */

public class GoogleDriveHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String CALLBACK_TAG = "API CONNECTION CALLBACK";
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String[] SCOPES = {DriveScopes.DRIVE_METADATA_READONLY};

    private final DriveOperationsActivity mContext;
    private GoogleAccountCredential mAccountCredential;
    private GoogleApiClient mGoogleApiClient;

    public GoogleDriveHelper(DriveOperationsActivity context) {
        mContext = context;
        mAccountCredential = GoogleAccountCredential.usingOAuth2(
                mContext, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public boolean aquireDriveApi(){
        if(!isGooglePlayServicesAvailable()){
            acquireGooglePlayServices();
        } else if(mAccountCredential.getSelectedAccountName() == null){
            mContext.chooseAccount();
        } else if(!Utils.isDeviceOnline(mContext)){
            String offlineString = mContext.getString(R.string.offline);
            Toast.makeText(mContext,offlineString,Toast.LENGTH_SHORT).show();
        } else {
            GoogleApiClient apiClient = getApiClient();
            if(apiClient != null && apiClient.isConnected()){
                mContext.launchDriveTaskExecution(mGoogleApiClient);
            }
            return true;
        }
        return false;
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = googleApiAvailability.
                isGooglePlayServicesAvailable(mContext);
        if(googleApiAvailability.isUserResolvableError(connectionStatusCode))
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode,mContext);
    }

    public static void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode,
                                                                     Activity context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = googleApiAvailability.getErrorDialog(
                context,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    public GoogleApiClient getApiClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if(mGoogleApiClient.isConnected()) {
            return mGoogleApiClient;
        } else mGoogleApiClient.connect();
        return null;
    }

    public void setAccountName(String accountName) {
        mAccountCredential.setSelectedAccountName(accountName);
    }

    public Intent getAccountChoosingIntent() {
        return mAccountCredential.newChooseAccountIntent();
    }

//region Connection Callbacks
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(CALLBACK_TAG, "GoogleApiClient connected");
        mContext.launchDriveTaskExecution(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(CALLBACK_TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(CALLBACK_TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(mContext,
                    result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(mContext , REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(CALLBACK_TAG, "Exception while starting resolution activity", e);
        }
    }
//endregion
}


