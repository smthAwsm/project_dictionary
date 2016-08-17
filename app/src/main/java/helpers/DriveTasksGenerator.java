package helpers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Query;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import activities.DriveOperationsActivity;

/**
 * Created by XPS on 08/10/2016.
 */
public class DriveTasksGenerator {

    private final DriveOperationsActivity mContext;
    private final GoogleApiClient mApiClient;
    private final String START_TAG = "TASK_STARTED";
    private final String RESPONSE_TAG = "RESPONSE";
    private final String SUCCESS = "SUCCESS";

    public DriveTasksGenerator(DriveOperationsActivity context, GoogleApiClient client) {
        this.mContext = context;
        this.mApiClient = client;
    }

    public AsyncTask<Void,Void,Void> getDriveTask(DriveTask operation){
        switch (operation){
            case LIST_FILES_TASK:
                return new ListFilesTask();
            case APP_FOLDER_BACKUP_TASK:
                String mimeType = "text/xml";
                java.io.File fileContent = new java.io.File(Environment.getDataDirectory().getPath()
                                + "/data/com.example.xps.drivetest/shared_prefs/MainActivity.xml");
                                //+ "/data/com.example.xps.drivetest/databases/Dictionaries.db");
                String name = fileContent.getName();
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

    private class ListFilesTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            Log.i(START_TAG,"List files in folder task started");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
                retrieveNextPage();
            return null;
        }
        private void retrieveNextPage() {
            Query query = new Query.Builder()
                    .build();
            Drive.DriveApi.query(mApiClient, query)
                    .setResultCallback(metadataBufferCallback);
        }

        private final ResultCallback<DriveApi.MetadataBufferResult> metadataBufferCallback = new
                ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e("LIST FILES CALLBACK","Problem while retrieving files");
                            return;
                        }
                        for (Metadata res : result.getMetadataBuffer()){
                            Log.i("######RESULT","Name " + res.getOriginalFilename() +
                                    " Title "+ res.getTitle() +
                                    " Id " + res.getDriveId().encodeToString());
                        }
                    }
                };
    }
    private class BackupDbOnDrive extends AsyncTask<Void, Void, Void>{
        private final String title;
        private final String mimeType;
        private final java.io.File file;
        DriveFolder mFolder;

        public BackupDbOnDrive(String title, String mimeType, java.io.File file) {
            this.title = title;
            this.mimeType = mimeType;
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            Log.i(START_TAG,"Backup files in app folder task started");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
                if(mApiClient != null && title!= null && mimeType!= null && file!= null){
                    //try {
                    retrieveNextPage();

                    //} catch (Exception e) {e.printStackTrace();}
                }
            return null;
        }

        private void retrieveNextPage() {
            Query query = new Query.Builder()
                    .build();
            Drive.DriveApi.query(mApiClient, query)
                    .setResultCallback(metadataBufferCallback);
        }

        private final ResultCallback<DriveApi.MetadataBufferResult> metadataBufferCallback = new
                ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e("LIST FILES CALLBACK","Problem while retrieving files");
                            return;
                        }
                        for (Metadata res : result.getMetadataBuffer()){
                            if (res.getTitle().equals("NEW TEST FOLDER!!!") && res.isFolder()) {
                                mFolder = res.getDriveId().asDriveFolder();
                            }

                            Log.i("######RESULT","Name "  + res.getOriginalFilename() +
                                    " Title "+ res.getTitle() +
                                    " Id " + res.getDriveId().encodeToString());
                        }

                        Drive.DriveApi.newDriveContents(mApiClient)
                                .setResultCallback(driveContentsCallback);
                    }
                };

        ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                DriveContents contents = driveContentsResult != null &&
                        driveContentsResult.getStatus().isSuccess() ?
                        driveContentsResult.getDriveContents() : null;
                if(contents != null ){
                    try{
                        OutputStream outputStream = contents.getOutputStream();
                        if(outputStream != null)
                            try{
                                InputStream inputStream = new FileInputStream(file);
                                byte[] buf = new byte[4096];
                                int c;
                                while ((c = inputStream.read(buf,0,buf.length)) > 0) {
                                    outputStream.write(buf,0,c);
                                    outputStream.flush();
                                }
                                //inputStream.close();
                                //outputStream.close(); //TODO ??? while
                            } catch (IOException e) {e.printStackTrace();}
                            finally {
                                outputStream.close();
                            }

                        MetadataChangeSet metaSet = new MetadataChangeSet.Builder().
                                setTitle(title).
                                setMimeType(mimeType).
                                build();

                        //Drive.DriveApi.getAppFolder(mApiClient)
                                mFolder.createFile(mApiClient, metaSet, contents)
                                .setResultCallback(mFileResultCallback);

                       /* DriveFolder driveFolder = Drive.DriveApi.getAppFolder(mApiClient);
                        driveFolder.createFile(mApiClient,metaSet,contents).
                                setResultCallback(mFileResultCallback);*/
                    } catch (Exception e) { e.printStackTrace();}
            }
        }
    };

        ResultCallback<DriveFolder.DriveFileResult> mFileResultCallback =
                new ResultCallback<DriveFolder.DriveFileResult>() {
            @Override
            public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                if(driveFileResult != null && driveFileResult.getStatus().isSuccess()){
                    DriveFile driveFile = driveFileResult != null &&
                            driveFileResult.getStatus().isSuccess() ?
                            driveFileResult.getDriveFile() : null;
                    if(driveFile != null){
                        driveFile.getMetadata(mApiClient).setResultCallback(mMetadataResultCallback);
                    }
                }
            }
        };
        ResultCallback<DriveResource.MetadataResult> mMetadataResultCallback =
                new ResultCallback<DriveResource.MetadataResult>() {
                    @Override
                    public void onResult(@NonNull DriveResource.MetadataResult metadataResult) {
                        if(metadataResult != null && metadataResult.getStatus().isSuccess()){
                            Metadata metadata = metadataResult.getMetadata();
                            Log.i("Created file"," Name " + metadata.getTitle() +
                                  " Creation date "  + metadata.getCreatedDate() +
                                  " Size "  +metadata.getFileSize() +
                                  " ID "  +metadata.getDriveId().encodeToString());
                        }
                    }
                };
    }
    private class RestoreDbFromDrive extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            Log.i(START_TAG,"Restore files from app folder task started");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mApiClient != null && mApiClient.isConnected()) {
                try {
                    DriveFolder driveFolder = Drive.DriveApi.getAppFolder(mApiClient);
                    driveFolder.listChildren(mApiClient).setResultCallback(metadataResult);
                } catch (Exception e) {e.printStackTrace();}
            }
            return null;
        }

        final private ResultCallback<DriveApi.MetadataBufferResult> metadataResult = new
                ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(DriveApi.MetadataBufferResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e("LIST APP FOLDER FILES ", "Problem while retrieving files");
                            return;
                        }
                        MetadataBuffer resultBuffer = result.getMetadataBuffer();

                        for (Metadata res : resultBuffer){
                            if(res.getTitle().equals("Dictionaries.db")){
                                DriveFile driveFile = res.getDriveId().asDriveFile();
                                driveFile.open(mApiClient,DriveFile.MODE_READ_ONLY,null).
                                        setResultCallback(mDriveContentsCallback);
                            }
                        }
                    }
                };

        ResultCallback<DriveApi.DriveContentsResult> mDriveContentsCallback =
                new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if ((driveContentsResult != null) &&
                        (driveContentsResult.getStatus().isSuccess())) {
                    DriveContents contents = driveContentsResult.getDriveContents();
                    InputStream stream = contents.getInputStream();
                    java.io.File file = new java.io.File(Environment.getDataDirectory().getPath()
                            + "/data/com.example.xps.drivetest/databases/Dictionaries.db");
                    try {
                        writeToFile(stream, file);
                    } catch (IOException e) { e.printStackTrace(); }
                    contents.discard(mApiClient);
                }
            }
        };
        public void writeToFile(InputStream stream, java.io.File file) throws IOException {

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            int c;
            try {
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
    }

    public enum DriveTask {
        APP_FOLDER_BACKUP_TASK,
        APP_FOLDER_RESTORE_TASK,
        LIST_FILES_TASK
    }
}

