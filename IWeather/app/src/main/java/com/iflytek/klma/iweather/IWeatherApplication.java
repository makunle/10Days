package com.iflytek.klma.iweather;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/8/3.
 */

public class IWeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(getApplicationContext());   //litepal数据库初始化
        SpeechUtility.createUtility(getApplicationContext(), getString(R.string.appid)); //语音理解sdk初始化
        DatabaseUtil.getInstance().firstTimeInitDataBase(this); //初次运行复制数据库

    }
}
