package com.iflytek.mkl.imepracticedemo;

import android.app.Application;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "05d95b952e", false);
    }
}
