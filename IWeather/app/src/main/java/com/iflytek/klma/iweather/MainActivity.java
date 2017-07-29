package com.iflytek.klma.iweather;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iflytek.klma.iweather.ui.CountyChooseFragment;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.initialize(this);   //litepal数据库初始化

        CountyChooseFragment cf = new CountyChooseFragment();
        replaceFragment(cf);

        DatabaseUtil.getInstance().firstTimeInitDataBase(this);
//        DbInit.initCityDb();
//        DbInit.testDb();
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).commit();
    }
}
