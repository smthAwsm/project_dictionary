package activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import java.util.List;

import helpers.DriveTasksGenerator;
import helpers.DriveTasksGenerator.DriveTaskRunnuble;
import helpers.DriveTasksGenerator.DriveTask;
import helpers.GoogleDriveHelper;
import models.Tags;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by XPS on 08/16/2016.
 */
public class DriveOperationsActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks  {

    public static final int ACCOUNT_PICKER_REQUEST = 1;
    public static final int REQUEST_ACCOUNT_PICKER_AND_EXEC = 1000;
    public static final int REQUEST_ACCOUNT_PICKER = 1001;
    public static final int REQUEST_AUTHORIZATION = 1002;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1003;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1004;
    public static final int REQUEST_PERMISSION_WRITE_STORAGE = 1005;

    private DriveTask mCurrentDriveTask;
    protected GoogleDriveHelper mGoogleDriveHelper;

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(
                            this,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    executeDriveTask(mCurrentDriveTask);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    setupAccountName(data);
                }
                break;
            case REQUEST_ACCOUNT_PICKER_AND_EXEC:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                        setupAccountName(data);
                        executeDriveTask(mCurrentDriveTask);
                    }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    executeDriveTask(mCurrentDriveTask);
                }
                break;
            case ACCOUNT_PICKER_REQUEST:
                if (resultCode != RESULT_OK) {
                    Toast.makeText( this,"Logging error",Toast.LENGTH_SHORT).show();
                } else {
                    executeDriveTask(mCurrentDriveTask);
                }
                break;
        }
    }

    private void setupAccountName(Intent data){
        String accountName =
                data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (accountName != null) {
            SharedPreferences.Editor prefEditor = getSharedPreferences(
                    Tags.APP_DATA,Context.MODE_PRIVATE).edit();
            prefEditor.putString(Tags.PREF_ACCOUNT_NAME, accountName);
            prefEditor.apply();
            mGoogleDriveHelper.setAccountName(accountName);
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    public void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getSharedPreferences(Tags.APP_DATA, Context.MODE_PRIVATE)
                    .getString(Tags.PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mGoogleDriveHelper.setAccountName(accountName);
                mGoogleDriveHelper.aquireDriveApi();
                return;
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mGoogleDriveHelper.getAccountChoosingIntent(),
                        REQUEST_ACCOUNT_PICKER_AND_EXEC);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    public void chooseAnotherAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mGoogleDriveHelper.getAccountChoosingIntent(),
                        REQUEST_ACCOUNT_PICKER);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    public void executeDriveTask(DriveTask task){
        mCurrentDriveTask = task;
        initDriveHelper();
        mGoogleDriveHelper.aquireDriveApi();
    }

    public boolean initDriveHelper(){
        if(mGoogleDriveHelper == null){
            try {
                mGoogleDriveHelper = new GoogleDriveHelper(this);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_WRITE_STORAGE)
    public void launchDriveTaskExecution() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            DriveTasksGenerator generator = new DriveTasksGenerator(this,
                    mGoogleDriveHelper.getApiClient());
            DriveTaskRunnuble driveTask = generator.getDriveTask(mCurrentDriveTask);
            driveTask.run();
            } else {
            EasyPermissions.requestPermissions(this,getString(R.string.storage_permission),
                    REQUEST_PERMISSION_WRITE_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        switch (requestCode){
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                mGoogleDriveHelper.aquireDriveApi();
                break;
            case REQUEST_PERMISSION_WRITE_STORAGE:
            DriveTasksGenerator generator = new DriveTasksGenerator(this,
                    mGoogleDriveHelper.getApiClient());
                DriveTaskRunnuble driveTask = generator.getDriveTask(mCurrentDriveTask);
            driveTask.run();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
    //endregion
}
