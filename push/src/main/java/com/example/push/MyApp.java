package com.example.push;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by HONGBO on 2017/11/4.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
    }
}
