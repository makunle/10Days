package com.iflytek.klma.iweather;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.klma.iweather.ui.CityChooseFragment;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.dbtool.DbInit;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.initialize(this);   //litepal数据库初始化

        CityChooseFragment cf = new CityChooseFragment();
        replaceFragment(cf);

        new DatabaseUtil(this).firstTimeInitDataBase();
//        DbInit.initCityDb();
        DbInit.testDb();
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).commit();
    }
}
