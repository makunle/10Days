package com.iflytek.klma.iweather.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iflytek.cloud.SpeechUtility;
import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.initialize(this);   //litepal数据库初始化
        SpeechUtility.createUtility(getApplicationContext(), getString(R.string.appid));

//        LitePal.getDatabase();

        DatabaseUtil.getInstance().firstTimeInitDataBase(this);

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
}
