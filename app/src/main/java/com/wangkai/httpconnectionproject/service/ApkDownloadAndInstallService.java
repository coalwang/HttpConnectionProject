package com.wangkai.httpconnectionproject.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.wangkai.httpconnectionproject.manager.NotificationShowManager;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 开启一个服务区下载某个应用，并进行安装
 */
public class ApkDownloadAndInstallService extends IntentService{

    private static final String TAG = ApkDownloadAndInstallService.class.getSimpleName();
    private static int MSG_DONE = 1;
    private static int MSG_UPDATE = 0;
    private static int MSG_ERROR = -1;
    private File mSaveFile;
    private String mFileName;
    private Handler mHandler;
    private int mFileLenght;
    private int mCurSize;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ApkDownloadAndInstallService(String name, final Context context) {
        super(name);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == MSG_UPDATE) {
                    // 显示notification
                    NotificationShowManager.getInstance().showApkDownloadingNotification(mCurSize, mFileLenght,
                            mFileName, context);
                    // 每隔0.5s发送一次消息就走到handleMessage方法，也就是调一次showApkDownloadingNotification方法
                    // mCurSize不断在更新，所以notification上的进度也更新了
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 500); // 0.5s更新一次进度
                } else if (msg.what == MSG_DONE) {
                    NotificationShowManager.getInstance().dismissApkDownloadingNotification();
                    mHandler.removeCallbacksAndMessages(null);
                    if (mSaveFile != null) {
                        // 安装apk
                        installApk(mSaveFile, context);
                    }
                }else if (msg.what == MSG_ERROR){
                    NotificationShowManager.getInstance().dismissApkDownloadingNotification();
                    mHandler.removeCallbacksAndMessages(null);
                }
                return true;
            }
        });
    }

    private void installApk(File file, Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.cola.fileprovider", file);
            intent.setDataAndType(contentUri, "application/android.package-archive");
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/android.package-archive");
        }
        startActivity(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 封装了handler，在子线程中运行的方法
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Uri uri = intent.getData();
        if (uri == null) return;  // 下载地址为null
        String url = uri.toString();
        if (TextUtils.isEmpty(url)) return;  // 下载地址为null
        // prefKey：用来作为下载内容的名字，
        String prefKey = String.valueOf(url.hashCode());
        HttpURLConnection connection = null;
        try {
            URL url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");  // 设置请求方式
            // 设置其他配置
            // 。。。。。。
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 拿到网络的输入流
                // 拿到本地文件的输出流
                // 将读网络的输入流写入到本地文件的输出流
                // 完成文件的下载
                // 关流
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    class LocalBinder extends Binder {

        public ApkDownloadAndInstallService getService(){
            return ApkDownloadAndInstallService.this;
        }
    }
}


