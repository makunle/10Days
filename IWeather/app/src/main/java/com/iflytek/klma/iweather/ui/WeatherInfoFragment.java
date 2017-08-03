package com.iflytek.klma.iweather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.gson.HefengWeather;
import com.iflytek.klma.iweather.gson.Weather;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.JsonUtil;

/**
 * Created by makunle on 2017/7/30.
 */

public class WeatherInfoFragment extends Fragment {

    private static final String TAG = "IWeather";

    private int mBookmarkId;

    private TextView mWeatherInfo;
    private TextView mNowTemperature;
    private TextView mMinTemperature;
    private TextView mMaxTemperature;
    private TextView mWindDirect;
    private TextView mWindLevel;

    private View mView;

    public WeatherInfoFragment() {
    }

    public int getmBookmarkId() {
        return mBookmarkId;
    }

    public void setmBookmarkId(int mBookmarkId) {
        this.mBookmarkId = mBookmarkId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.weather_info, container, false);
            mWeatherInfo = (TextView) mView.findViewById(R.id.weather_info);
            mNowTemperature = (TextView) mView.findViewById(R.id.now_temperature);
            mMinTemperature = (TextView) mView.findViewById(R.id.min_temperature);
            mMaxTemperature = (TextView) mView.findViewById(R.id.max_temperature);
            mWindDirect = (TextView) mView.findViewById(R.id.wind_direct);
            mWindLevel = (TextView) mView.findViewById(R.id.wind_level);
        }
        Log.d(TAG, "onCreateView: Fragment" + mBookmarkId + " " + (mWeatherInfo == null) + " " + mBookmarkId);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + mBookmarkId);
        refreshView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + mBookmarkId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + mBookmarkId);
    }

    public boolean isViewCreated() {
        return mView != null;
    }

    public void refreshView() {
        Log.d(TAG, "refreshView: ");

        String weatherJson = DatabaseUtil.getInstance().getWeatherJsonFromBookmarkId(mBookmarkId);
        if (TextUtils.isEmpty(weatherJson)) return;

        Weather weather = JsonUtil.handleHefengJson(weatherJson);
        if (weather == null) return;

        mWeatherInfo.setText("天气状况：" + weather.getInfo());
        mNowTemperature.setText("当前温度：" + weather.getNowTemperature());
        mMinTemperature.setText("最低气温：" + weather.getMinTemperature());
        mMaxTemperature.setText("最高气温：" + weather.getMaxTemperature());
        mWindDirect.setText("风向：" + weather.getWindDirect());
        mWindLevel.setText("风力：" + weather.getWindLevel());
        Log.d(TAG, "refreshView: done");
    }

    /**
     * 销毁前保存bookmarkId，之后加载时读取，解决旋转屏幕无信息问题
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", mBookmarkId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //当前bookmarkId无效，且savedBundle有信息，则取出saved bookmarkId
        if (DatabaseUtil.getInstance().getWeatherBookmarkById(mBookmarkId) == null && savedInstanceState != null) {
            int id = savedInstanceState.getInt("id", -1);
            //如果取出的bookmarkId有用，才使用它
            if (DatabaseUtil.getInstance().getWeatherBookmarkById(id) != null) {
                mBookmarkId = id;
            }
        }
    }
}
