package com.iflytek.mkl.vociesdktest;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Administrator on 2017/7/27.
 */

public class SpeechApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SpeechUtility.createUtility(SpeechApp.this, "appid=5979d0b1");
    }
}
