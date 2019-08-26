package com.antariksha.ftpdownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.antariksha.ftpdownloader.Constants.FTP_FILE_DOWNLOAD;
import static com.antariksha.ftpdownloader.Constants.FTP_FILE_DOWNLOAD_FAILED;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<FileSyncModel> fileSyncModels = new ArrayList<>();
        FileSyncModel fileSyncModel = new FileSyncModel();
        fileSyncModel.setName("dhoni.mp4");
        fileSyncModels.add(fileSyncModel);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        new FTPAsyncTask(fileSyncModels, "10.0.0.24", "admin", "password1", "/Demo").execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getData().equalsIgnoreCase(FTP_FILE_DOWNLOAD)) {

        } else if (messageEvent.getData().equalsIgnoreCase(FTP_FILE_DOWNLOAD_FAILED)) {

        }
    }
}
