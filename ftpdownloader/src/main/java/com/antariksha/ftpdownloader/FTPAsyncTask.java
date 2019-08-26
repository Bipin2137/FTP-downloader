package com.antariksha.ftpdownloader;

import android.os.AsyncTask;
import android.os.Handler;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.antariksha.ftpdownloader.Constants.FTP_FILE_DOWNLOAD;
import static com.antariksha.ftpdownloader.Constants.FTP_FILE_DOWNLOAD_FAILED;


public class FTPAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "FTPAsyncTask";
    private ArrayList<FileSyncModel> mFileSyncModels;
    private String SERVER_ID;
    private String USERNAME;
    private String PASSWORD;
    private String DIRECTORY;
    Handler handler;

    public FTPAsyncTask(ArrayList<FileSyncModel> fileSyncModels, String SERVER_ID, String USERNAME, String PASSWORD, String DIRECTORY) {
        mFileSyncModels = fileSyncModels;
        this.SERVER_ID = SERVER_ID;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.DIRECTORY = DIRECTORY;
    }

    @Override
    protected String doInBackground(String... strings) {
        FTPUtils ftpUtils = new FTPUtils();
        if (ftpUtils.login(SERVER_ID, USERNAME, PASSWORD)) {
            ftpUtils.setConfigurations(DIRECTORY);
            for (FileSyncModel fileSyncModel : mFileSyncModels) {
                if (isCancelled()) {
                    break;
                }
                long fileSize = ftpUtils.isFilePresent(fileSyncModel.getName());
                if (fileSize > 0) {
                    if (ftpUtils.downloadFile(fileSyncModel.getName())) {
                        EventBus.getDefault().post(new MessageEvent(FTP_FILE_DOWNLOAD));
                    } else {
                        EventBus.getDefault().post(new MessageEvent(FTP_FILE_DOWNLOAD_FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new MessageEvent(FTP_FILE_DOWNLOAD_FAILED));
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
