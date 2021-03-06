package com.hochan.sqlite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

import com.hochan.sqlite.data.DownloadInfo;
import com.hochan.sqlite.sql.DownloadTaskDataHelper;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

public class DownloadService extends Service {

    public static final String URL = "url";
    public static final String PATH = "path";

    private HashMap<String, DownloadInfo> mDownloadList;
    private List<String> mDownloadUrls = new ArrayList<>();
    private DownloadBinder mBinder = new DownloadBinder();
    private DownloadServiceListener mListener;
    private int mDownloadCount;

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadList = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //System.out.println(flags);
        //System.out.println(startId);
        String url = intent.getStringExtra(URL);
        if(url == null){
            System.out.println("已启动");
            return 0;
        }

        mDownloadUrls.add(url);
        System.out.println(url);
        String name = url.substring(url.lastIndexOf("/")+1, url.length());

        String directory = this.getCacheDir() + "/download";
        String path = directory + "/" + name;

        System.out.println(path);
        File dr  = new File(directory);
        if (!dr.exists())
            dr.mkdir();

        File file = new File(path);
        mDownloadCount++;
        final DownloadInfo downloadInfo = new DownloadInfo(url, name, path);
        final int index = mDownloadCount-1;
        final Future<File> downloading = Ion
                .with(this)
                .load(url)
                .progressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        //System.out.println(downloaded+" "+total);
                        downloadInfo.setmTotalSize(total);
                        downloadInfo.setmDownloadedSize(downloaded);
                        if(downloaded == total){
                            downloadInfo.setmIsFinished(true);
                            downloadInfo.setmState(DownloadInfo.STATE_FINISHED);
                            DownloadTaskDataHelper.saveDownloadTask(DownloadService.this, downloadInfo);
                        }
                        else
                        downloadInfo.setmState(DownloadInfo.STATE_LOADING);
                        if(mListener != null) {
                            mListener.progress(downloadInfo.getmUrl(), downloaded, total);
                        }
                    }
                })
                .write(file);
        downloadInfo.setmDownloading(downloading);
        mDownloadList.put(url, downloadInfo);
        //System.out.println(url+" "+path);
        DownloadTaskDataHelper.saveDownloadTask(this, downloadInfo);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        System.out.println("DownloadService Destroy!");
        DownloadTaskDataHelper.saveDownloadTaskList(this, mDownloadList, mDownloadUrls);
        super.onDestroy();
    }

    public List<String> getDownloadUrls(){
        return mDownloadUrls;
    }

    public HashMap<String, DownloadInfo> getDownloadList(){
        return mDownloadList;
    }

    public class DownloadBinder extends Binder {

        public DownloadService getService(){
            return DownloadService.this;
        }
    }

    public void setDownloadListener(DownloadServiceListener listener){
        this.mListener = listener;
    }

    public interface DownloadServiceListener{
        void progress(String url, long downloaded, long total);
    }
}
