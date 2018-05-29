package com.wangkai.httpconnectionproject;

import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * 文件断点下载
 * 关键的三点：
 * 1. 获取已经下载的文件的大小
 * 2. 请求服务器时告诉服务器开始下载的位置：Range
 * 3. 写入文件时使用RandomAccessFile指定开始写入文件的位置
 * create by wangkai on 2018.5.23
 */
public class BreakpointDownload {

    private static final String TAG = BreakpointDownload.class.getSimpleName();
    //文件流校验段大小
    private static final int CHECK_SIZE = 512;


    private void getFile(String url, Map<String,String> headers, Map<String, String> params, String destFilePath){
        if (TextUtils.isEmpty(url)){
            // 返回一个监听事件，url is null，通过接口回调的方式

            return;
        }

        // 组合get请求的url
        if (params != null && params.size() > 0){
            //url = url + ? + map2QueryString(params);
        }

        HttpURLConnection connection = null;
        InputStream netIn = null;

        long start = 0;  // 开始下载文件的地方
//        file = initFile(destFilePath);  // 获取file对象
//        //断点续传的原理，去检查已经下载的文件的大小
//        seek = file.length();  // 已下载的文件的长度

        String path = Environment.getExternalStorageDirectory().getPath()+"aaaa/baidu_map.apk";
        // 断点下载使用文件对象RandomAccessFile
        // 第一个参数：文件路径
        // 第二个参数：操作类型
        RandomAccessFile randomAccessFile = null;
        try {
            // 这里应该考虑文件是否存在，不存在的话需要去创建
            randomAccessFile = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            start = randomAccessFile.length();  // 获取已经下载的文件的长度
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            randomAccessFile.seek(start);  // 移动指针到开始位置
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection = initHttpURLConnection(url); // 获取HttpURLConnection
        //connection = addHeaders(connection, headers);  //添加header，该方法未实现
        connection.addRequestProperty("Range", "bytes=" + start + "-");  // 告诉服务器从哪开始下载
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();  // 与服务器连接并返回响应码
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HTTP_PARTIAL: 客户端表明自己只需要目标URL上的部分资源的时候返回的.这种情况经常发生在客户端继续请求一个未完成的下载的时候
        if (HttpURLConnection.HTTP_OK == responseCode || HttpURLConnection.HTTP_PARTIAL == responseCode){
            // 下载文件
            try {
                netIn = connection.getInputStream();  // 网络获取输入流
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = netIn.read(buffer)) != -1){
                    // 三个参数
                    // 第一个参数：数据
                    // 第二个参数：数据的开始位置
                    // 第三个参数：数据的长度
                    randomAccessFile.write(buffer, 0, count);
                }

                //关流
                netIn.close();
                randomAccessFile.close();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                // 关流
                if (connection != null){
                    connection.disconnect();
                }
            }

        }

    }


    /**
     * 初始化HttpURLConnection
     * 关于要不要显示调用connect方法的问题：
     * 1、不需要显示调用connect方法，可调用，可不调用
     * 2、必须调用getResponseCode()方法
     * 在不调用getResponseCode()方法的时候，无论是否调用connect()方法，请求都是不能成功的，
     * 调用connect()方法只是建立连接，并不会向服务器传递数据，
     * 只用调用getRespconseCode()方法时，才会向服务器传递数据(有博文说是getInputStream()才会向服务器传递数据，
     * getResponseCode中会调用getInputStream方法)。
     * 跟着getResponseCode()源码发现里面调用了getInputStream()方法，
     * 在getInputStream()方法中会判断当前是否连接，如果没有连接，则调用connect()方法建立连接。
     * @param urlPath
     * @return
     */
    private HttpURLConnection initHttpURLConnection(String urlPath) {
        URL url = null;
        try {
            url = new URL(urlPath);  // 实例化url
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();  // 实例化HttpURLConnection
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置连接超时时间和读取超时时间
        connection.setConnectTimeout(500);
        connection.setReadTimeout(500);
        // 设置请求方式
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return connection  // 返回一个HttpURLConnection
                ;
    }

    /**
     * 初始化文件，检查文件是否存在，存在则返回，不存在则创建后返回
     *
     * @param filePath 文件路径
     * @return 文件对象
     */
    private File initFile(String filePath) {
        File file = new File(filePath);  // 根据路径创建一个文件对象
        // String getParent()返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null。
        // File getParentFile()返回此抽象路径名父目录的抽象路径名；如果此路径名没有指定父目录，则返回 null。
        File fileParent = new File(file.getParent());  // 返回file的父目录
        if (!fileParent.exists()){
            // mkdir() 如果你想在已经存在的文件夹(D盘下的yyy文件夹)下建立新的文件夹（2010-02-28文件夹），
            // 就可以用此方法。此方法不能在不存在的文件夹下建立新的文件夹。
            // 假如想建立名字是”2010-02-28”文件夹，那么它的父文件夹必须存在。
            // mkdirs() 如果你想根据File里的路径名建立文件夹（当你不知道此文件夹是否存在，也不知道父文件夹存在），
            // 就可用此方法，它建立文件夹的原则是：如果父文件夹不存在并且最后一级子文件夹不存在，
            // 它就自动新建所有路经里写的文件夹；如果父文件夹存在，它就直接在已经存在的父文件夹下新建子文件夹
            //mkdir：只能用来创建文件夹，且只能创建一级目录，如果上级不存在，就会创建失败。
            //mkdirs：只能用来创建文件夹，且能创建多级目录 ，如果上级不存在，就会自动创建。(创建文件夹多用此)
            fileParent.mkdirs();
        }
        if (!file.exists()){
            try {
                // 只能用来创建文件，且只能在已存在的目录下创建文件，否则会创建失败
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}


