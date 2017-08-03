package com.iflytek.klma.iweather.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.dbtool.DbInit;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //用来生成随apk打包的数据库
//        LitePal.initialize(this);   //litepal数据库初始化
//        DbInit.initCityDb();
//        DbInit.makeHotCity();

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

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
