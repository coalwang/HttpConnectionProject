package com.wangkai.httpconnectionproject.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class DownloadManager {

    /**
     * 下载某个apk，下载完成后进行安装
     * @param url 下载链接
     */
    public static void  downloadAndInstallApk(Context context, String url){
        if (TextUtils.isEmpty(url))return;
        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }
        Intent i = new Intent("com.action.DOWNLOAD");
        i.setData(Uri.parse(url));
        i.setClass(context, ApkDownloadAndInstallService.class);
        context.startService(i);
    }
}
