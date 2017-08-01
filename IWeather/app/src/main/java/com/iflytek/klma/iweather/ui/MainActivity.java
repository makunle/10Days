package com.iflytek.klma.iweather.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.util.AndroidUtil;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if(DatabaseUtil.getInstance().getAllWeatherBookMark().size() > 0){
            startActivity(new Intent(this, WeatherShowActivity.class));
        }else{
            startActivity(new Intent(this, CountyChooseActivity.class));
        }

        finish();

//        LitePal.getDatabase();
//        DbInit.initCityDb();
//        DbInit.testDb();

    }

    private void init(){
        LitePal.initialize(this);   //litepal数据库初始化
        SpeechUtility.createUtility(getApplicationContext(), getString(R.string.appid)); //语音理解sdk初始化
        DatabaseUtil.getInstance().firstTimeInitDataBase(this); //初次运行复制数据库
    }
}
