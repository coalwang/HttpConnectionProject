package com.wangkai.httpconnectionproject.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.wangkai.httpconnectionproject.R;
import com.wangkai.httpconnectionproject.utils.Tools;

public class NotificationShowManager {

    private NotificationManager mNotificationManager;
    private Notification.Builder mApkDownloadingNfBuilder;

    private static NotificationShowManager mNotificationShowManager;

    /**
     * 单例模式获得实例
     * @return
     */
    public static NotificationShowManager getInstance(){
        if (mNotificationShowManager == null){
            synchronized (NotificationShowManager.class){
                if (mNotificationShowManager == null){
                    mNotificationShowManager = new NotificationShowManager();
                }
            }
        }
        return mNotificationShowManager;
    }

    /**
     * 显示下载apk的notification
     * @param size
     * @param length
     * @param name
     * @param context
     */
    public void showApkDownloadingNotification(int size, int length, String name, Context context){
        if (mApkDownloadingNfBuilder == null){
            mApkDownloadingNfBuilder = new Notification.Builder(context);
            String tickerText = "开始下载应用";
            //setTicker()设置的是通知时在状态栏显示的通知内容，一般是一段文字，例如在状态栏显示“您有一条短信，待查收”。
            //setTitle()设置的是收到通知后你将状态栏下拉后，显示的通知的具体内容的标题。
            mApkDownloadingNfBuilder.setTicker(tickerText);
            mApkDownloadingNfBuilder.setSmallIcon(R.drawable.status_icon);
            // 参数加载的是bitmap资源
            mApkDownloadingNfBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.icon));
            mApkDownloadingNfBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
            //设置通知栏标题
            mApkDownloadingNfBuilder.setContentText(name);
            //设置为ture，表示它为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
            mApkDownloadingNfBuilder.setOngoing(true);
            mApkDownloadingNfBuilder.setOnlyAlertOnce(true);
            mApkDownloadingNfBuilder.setContentText(Tools.getSizeStr(size, 2, Tools.SIZE_UINT_MB)
                    + "/" + Tools.getSizeStrNoB(length, 2, Tools.SIZE_UINT_MB));
            // max:进度条最大数值  、progress:当前进度、indeterminate:表示进度是否不确定，true为不确定
            // 此方法在4.0及以后版本才有用，如果为早期版本：需要自定义通知布局，其中包含ProgressBar视图
            // 使用：如果为确定的进度条：调用setProgress(max, progress, false)来设置通知，在更新进度的时候在此发起通知更新progress，
            // 并且在下载完成后要移除进度条，通过调用setProgress(0, 0, false)既可。
            mApkDownloadingNfBuilder.setProgress(length, size, false);
            // 4.1以下的手机需要配置ContentIntent，否则报错
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            mApkDownloadingNfBuilder.setContentIntent(pendingIntent);
            // 更新remoteView
            // 通过NotificationManager 的 notify(int, Notification)方法来启动Notification。
            // 第一个参数唯一的标识该Notification
            // 第二个参数就是Notification对象
            mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0x20150401, mApkDownloadingNfBuilder.build());
        }
    }

    public void dismissApkDownloadingNotification() {
        // 参数id:启动该notification时传入的唯一标识
        mNotificationManager.cancel(0x20150401);
    }
}
