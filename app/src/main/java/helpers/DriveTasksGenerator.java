package helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.study.xps.projectdictionary.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import activities.DictionariesActivity;
import activities.DriveOperationsActivity;
import activities.IntroActivity;
import activities.SettingsActivity;
import models.Tags;

/**
 * Created by XPS on 08/10/2016.
 */
public class DriveTasksGenerator {

    private final DriveOperationsActivity mContext;
    private final GoogleApiClient mApiClient;
    private final String START_TAG = "TASK_STARTED";
    private final String RESPONSE_TAG = "RESPONSE";
    private final String SUCCESS = "SUCCESS";
    private final String DB_PATH = "/data/com.study.xps.projectdictionary/databases/Dictionaries.db";
    private AlertDialog mProgressDialog = null;

    public DriveTasksGenerator(DriveOperationsActivity context, GoogleApiClient client) {
        this.mContext = context;
        this.mApiClient = client;
    }

    public interface DriveTaskRunnuble{
        void run();
    }

    public DriveTaskRunnuble getDriveTask(DriveTask operation){
        switch (operation){
            case APP_FOLDER_BACKUP_TASK:
                java.io.File fileContent = new java.io.File(Environment.
                        getDataDirectory().getPath() + DB_PATH);
                String name = fileContent.getName();
                String mimeType = "application/x-sqlite3";
                return new BackupDbOnDrive(name,mimeType,fileContent);
            case APP_FOLDER_RESTORE_TASK:
                return new RestoreDbFromDrive();
            default: return null;
        }
    }

    private void taskCanceled(Exception error){
        if (error != null) {
            if (error instanceof GooglePlayServicesAvailabilityIOException) {
                GoogleDriveHelper.showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) error)
                                .getConnectionStatusCode(),mContext);
            } else if (error instanceof UserRecoverableAuthIOException) {
                ((Activity)mContext).startActivityForResult(
                        ((UserRecoverableAuthIOException) error).getIntent(),
                        DriveOperationsActivity.REQUEST_AUTHORIZATION);
            } else {
                Log.e("ERROR","The following error occurred:\n" + error.getMessage());
            }
        } else {
            Toast.makeText(mContext,"Request canceled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(String message, String title,
                            DialogInterface.OnClickListener dialogClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok,dialogClickListener)
                .setNegativeButton(R.string.cancel,dialogClickListener);
        builder.create().show();
    }
    private void showProgressDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View progressView = inflater.inflate(R.layout.dialog_progress_bar,null);
        TextView dialogTextView = (TextView) progressView.
                findViewById(R.id.progresDialogText);
        dialogTextView.setText(message);
        mProgressDialog = builder.setView(progressView).create();
        mProgressDialog.show();
    }

    private String getPercentDiff(long driveDbSize){
        java.io.File f = new java.io.File(Environment.getDataDirectory().getPath() + DB_PATH);
        if(f.exists()){
            long localDbSize = f.length();
            if(localDbSize == driveDbSize){
                return mContext.getString(R.string.equal_files);
            }
            int percentDiff;
            if(localDbSize > driveDbSize){
                percentDiff = (int)(100 - (driveDbSize * 100) / localDbSize);
                StringBuilder builder = new StringBuilder();
                String localDbLarger = mContext.getString(R.string.local_file);
                String larger = mContext.getString(R.string.larger);
                builder.append(localDbLarger).append(" " + percentDiff + "% ").append(larger);
                return builder.toString();
            } else {
                percentDiff = (int)(100 - (localDbSize * 100) / driveDbSize);
                StringBuilder builder = new StringBuilder();
                String localDbLarger = mContext.getString(R.string.cloud_file);
                String larger = mContext.getString(R.string.larger);
                builder.append(localDbLarger).append(" " + percentDiff + "% ").append(larger);

                return builder.toString();
            }
        }
        return "";
    }

    private class BackupDbOnDrive implements DriveTaskRunnuble {
        private final String title;
        private final String mimeType;
        private final java.io.File file;
        private DriveFile mDbOldFile;

        public BackupDbOnDrive(String title, String mimeType, java.io.File file) {
            this.title = title;
            this.mimeType = mimeType;
            this.file = file;
        }

        @Override
        public void run() {
            Log.i(START_TAG,"Backup files in app folder task started");
            if(mApiClient != null && title!= null && mimeType!= null && file!= null){
                try {
                    DriveFolder appFolder = Drive.DriveApi.getAppFolder(mApiClient);
                    appFolder.listChildren(mApiClient).setResultCallback(searchOldDbFilesCallback);
                } catch (NullPointerException e) {e.printStackTrace();}
            }
        }

        private final ResultCallback<DriveApi.MetadataBufferResult> searchOldDbFilesCallback =
                new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                        if (!metadataBufferResult.getStatus().isSuccess()) {
                            Toast.makeText(mContext,mContext.getString(R.string.backup_error),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MetadataBuffer buffer = metadataBufferResult.getMetadataBuffer();
                        for (Metadata data : buffer){
                            if(data.getTitle().equals("Dictionaries.db")){
                               mDbOldFile = data.getDriveId().asDriveFile();
                               prepareAndShowDialog(data);
                            }
                        }
                        buffer.release();
                    }
        };

        private void prepareAndShowDialog(Metadata data){
            String title = mContext.getString(R.string.backup_data);
            String percentDiff = getPercentDiff(data.getFileSize());
            StringBuilder builder = new StringBuilder(title);
            builder.append(percentDiff);
            DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == -1){
                                Drive.DriveApi.newDriveContents(mApiClient)
                                        .setResultCallback(driveContentsCallback);
                                showProgressDialog(mContext.getString(R.string.backup_progress));
                            }
                        }
                    };
            showDialog(builder.toString(),title,dialogClickListener);
        }

        private final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                DriveContents contents = driveContentsResult != null &&
                        driveContentsResult.getStatus().isSuccess() ?
                        driveContentsResult.getDriveContents() : null;
                if(contents != null ) {
                    fileToStream(contents);

                    MetadataChangeSet metaSet = new MetadataChangeSet.Builder().
                            setTitle(title).
                            setMimeType(mimeType).
                            build();

                    DriveFolder driveFolder = Drive.DriveApi.getAppFolder(mApiClient);
                    driveFolder.createFile(mApiClient,metaSet,contents).
                            setResultCallback(mFileResultCallback);
                }
            }
        };

        private void fileToStream(DriveContents contents){
            try {
                OutputStream outputStream = contents.getOutputStream();
                if(outputStream != null){
                    InputStream inputStream = new FileInputStream(file);
                    byte[] buf = new byte[4096];
                    int c;
                    while ((c = inputStream.read(buf,0,buf.length)) > 0) {
                        outputStream.write(buf,0,c);
                        outputStream.flush();
                    }
                    inputStream.close();
                }
                outputStream.close();
            }  catch (IOException e) { e.printStackTrace(); }
        }

        private final ResultCallback<DriveFolder.DriveFileResult> mFileResultCallback =
                new ResultCallback<DriveFolder.DriveFileResult>() {
            @Override
            public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                if(driveFileResult != null && driveFileResult.getStatus().isSuccess()){
                    DriveFile driveFile = driveFileResult != null &&
                            driveFileResult.getStatus().isSuccess() ?
                            driveFileResult.getDriveFile() : null;
                    if(driveFile == null){
                        return;
                    }
                    if (mDbOldFile != null) {
                        mDbOldFile.delete(mApiClient);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                        Date date = new Date();
                        SharedPreferences.Editor prefEditor = mContext.getSharedPreferences(
                                Tags.APP_DATA, Context.MODE_PRIVATE).edit();
                        prefEditor.putString(Tags.LAST_BACKUP_DATE,dateFormat.format(date));
                        prefEditor.commit();
                        if(mProgressDialog != null && mProgressDialog.isShowing()){
                        mProgressDialog.cancel();
                        }
                        if(mContext instanceof SettingsActivity){
                            ((SettingsActivity) mContext).updateSummaries();
                        }
                        Toast.makeText(mContext,R.string.backup_success,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext,R.string.backup_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    private class RestoreDbFromDrive implements DriveTaskRunnuble {

        @Override
        public void run() {
            if (mApiClient != null && mApiClient.isConnected()) {
                try {
                    DriveFolder driveFolder = Drive.DriveApi.getAppFolder(mApiClient);
                    driveFolder.listChildren(mApiClient).setResultCallback(mMetadataResult);
                } catch (NullPointerException e) {e.printStackTrace();}
            }
        }

        final private ResultCallback<DriveApi.MetadataBufferResult> mMetadataResult =
                new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Toast.makeText(mContext,mContext.getString(R.string.restore_error),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MetadataBuffer resultBuffer = result.getMetadataBuffer();
                        for (Metadata data : resultBuffer){
                            if(data.getTitle().equals("Dictionaries.db")) {
                                prepareAndShowDialog(data);
                            }
                        }
                        resultBuffer.release();
                    }
        };

        private void prepareAndShowDialog(Metadata data){
            String dataFound = mContext.getString(R.string.backup_restore);
            String created = mContext.getString(R.string.created);
            String restoreQuestion = mContext.getString(R.string.restore);
            StringBuilder messageBuiler = new StringBuilder();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String backupDate = dateFormat.format(data.getCreatedDate()).toString();
            String message = messageBuiler.append(dataFound).append(" (").
                    append(created + " ").append(backupDate).
                    append(getPercentDiff(data.getFileSize())).append("). ").
                    append(restoreQuestion).toString();
            String title = mContext.getString(R.string.backup_found);
            final DriveFile driveFile = data.getDriveId().asDriveFile();
            DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == -1){
                                driveFile.open(mApiClient,DriveFile.MODE_READ_ONLY,null).
                                        setResultCallback(mDriveContentsCallback);
                                showProgressDialog(mContext.getString(R.string.restore_progress));
                            }
                        }
                    };
            showDialog(message,title,dialogClickListener);
        }

        ResultCallback<DriveApi.DriveContentsResult> mDriveContentsCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if ((driveContentsResult != null) &&
                        (driveContentsResult.getStatus().isSuccess())) {
                    DriveContents contents = driveContentsResult.getDriveContents();
                    InputStream stream = contents.getInputStream();
                    java.io.File file = new java.io.File(Environment.getDataDirectory().getPath()
                            + DB_PATH);
                    try {
                        writeToFile(stream, file);
                        if(mProgressDialog != null && mProgressDialog.isShowing()){
                            mProgressDialog.cancel();
                        }
                        Toast.makeText(mContext,R.string.restore_success,
                                Toast.LENGTH_SHORT).show();
                        if(mContext instanceof IntroActivity) {
                            startDictionariesActivity((IntroActivity) mContext);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("RESTORE TASK","ERROR");
                        Toast.makeText(mContext,R.string.restore_error,
                                Toast.LENGTH_SHORT).show();
                    }
                    contents.discard(mApiClient);
                }
            }
        };

        public void writeToFile(InputStream stream, java.io.File file) throws IOException {
            java.io.File f = new java.io.File(Environment.getDataDirectory().getPath() +
                 "/data/com.study.xps.projectdictionary/databases");
            if (!f.isDirectory()) {
                if(!f.mkdir()) throw new IOException("Failed to create DB folder");
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            try {
                int c;
                while ((c = stream.read(buf, 0, buf.length)) > 0) {
                    outputStream.write(buf, 0, c);
                }
            } finally {
                if (outputStream != null) {
                    stream.close();
                    outputStream.close();
                }
            }
        }

        private void startDictionariesActivity(IntroActivity activity){
            activity.finish();
            activity.startActivity(new Intent(mContext, DictionariesActivity.class));
        }
    }

    public enum DriveTask {
        APP_FOLDER_BACKUP_TASK,
        APP_FOLDER_RESTORE_TASK
    }
}

