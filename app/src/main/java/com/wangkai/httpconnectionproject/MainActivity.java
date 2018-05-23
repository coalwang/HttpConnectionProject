package com.wangkai.httpconnectionproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.HttpURLConnection;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * URLConnection：抽象类，
 * HttpURLConnection：继承自URLConnection，现在主要使用的基本网络请求类，是android SDK中的类
 * HttpsURLConnection：继承自HttpURLConnection
 * HttpClient：是apache公司的开源产品，现在已经不再使用
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
