package com.iflytek.klma.iweather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.gson.HefengWeather;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.JsonUtil;

/**
 * Created by makunle on 2017/7/30.
 */

public class WeatherInfoFragment extends Fragment {

    private static final String TAG = "IWeather";

    private int bookmarkId;

    private TextView weatherInfo;
    private TextView nowTemperature;
    private TextView minTemperature;
    private TextView maxTemperature;
    private TextView windDirect;
    private TextView windLevel;

    private View view;

    public WeatherInfoFragment() {
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.weather_info, container, false);
            weatherInfo = (TextView) view.findViewById(R.id.weather_info);
            nowTemperature = (TextView) view.findViewById(R.id.now_temperature);
            minTemperature = (TextView) view.findViewById(R.id.min_temperature);
            maxTemperature = (TextView) view.findViewById(R.id.max_temperature);
            windDirect = (TextView) view.findViewById(R.id.wind_direct);
            windLevel = (TextView) view.findViewById(R.id.wind_level);
        }
        Log.d(TAG, "onCreateView: Fragment" + bookmarkId + " " + (weatherInfo == null) + " " + bookmarkId);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + bookmarkId);
        refreshView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + bookmarkId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + bookmarkId);
    }

    public boolean isViewCreated() {
        return view != null;
    }

    public void refreshView() {
        Log.d(TAG, "refreshView: ");

        String weatherJson = DatabaseUtil.getInstance().getWeatherJsonFromBookmarkId(bookmarkId);
        if (TextUtils.isEmpty(weatherJson)) return;

        HefengWeather weather = JsonUtil.handleHefengJson(weatherJson);
        if (weather == null) return;

        weatherInfo.setText("天气状况：" + weather.getInfo());
        nowTemperature.setText("当前温度：" + weather.getNowTemperature());
        minTemperature.setText("最低气温：" + weather.getMinTemperature());
        maxTemperature.setText("最高气温：" + weather.getMaxTemperature());
        windDirect.setText("风向：" + weather.getWindDirect());
        windLevel.setText("风力：" + weather.getWindLevel());
        Log.d(TAG, "refreshView: done");
    }

    /**
     * 销毁前保存bookmarkId，之后加载时读取，解决旋转屏幕无信息问题
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", bookmarkId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //当前bookmarkId无效，且savedBundle有信息，则取出saved bookmarkId
        if (DatabaseUtil.getInstance().getWeatherBookmarkById(bookmarkId) == null && savedInstanceState != null) {
            int id = savedInstanceState.getInt("id", -1);
            //如果取出的bookmarkId有用，才使用它
            if (DatabaseUtil.getInstance().getWeatherBookmarkById(id) != null){
                    bookmarkId = id;
            }
        }
    }
}
