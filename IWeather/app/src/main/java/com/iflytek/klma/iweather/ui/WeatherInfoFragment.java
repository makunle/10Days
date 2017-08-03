package com.iflytek.klma.iweather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;
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
    private TextView mSportInfo;
    private TextView mCarWashInfo;
    private TextView mComfortInfo;
    private TextView mPM25;
    private TextView mAirQuality;
    private TextView mAQI;
    private TextView mUpdateTime;
    private ImageView mWeatherPic;
    private LinearLayout mMainLayout;


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
            mView = inflater.inflate(R.layout.fragment_weather_info, container, false);
            mWeatherInfo = (TextView) mView.findViewById(R.id.weather_info);
            mNowTemperature = (TextView) mView.findViewById(R.id.now_temperature);
            mMinTemperature = (TextView) mView.findViewById(R.id.min_temperature);
            mMaxTemperature = (TextView) mView.findViewById(R.id.max_temperature);
            mWindDirect = (TextView) mView.findViewById(R.id.wind_direct);
            mWindLevel = (TextView) mView.findViewById(R.id.wind_level);
            mSportInfo = (TextView) mView.findViewById(R.id.sport_advice);
            mCarWashInfo = (TextView) mView.findViewById(R.id.car_wash);
            mComfortInfo = (TextView) mView.findViewById(R.id.comfort_value);
            mPM25 = (TextView) mView.findViewById(R.id.pm25);
            mAirQuality = (TextView) mView.findViewById(R.id.air_quality);
            mMainLayout = (LinearLayout) mView.findViewById(R.id.weather_layout);
            mAQI = (TextView) mView.findViewById(R.id.aqi);
            mUpdateTime = (TextView) mView.findViewById(R.id.update_time);
            mWeatherPic = (ImageView) mView.findViewById(R.id.weather_pic);

            mMainLayout.setVisibility(View.INVISIBLE);
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

        mWeatherInfo.setText(weather.getInfo());
        mNowTemperature.setText(weather.getNowTemperature() + "°");
        mMinTemperature.setText(weather.getMinTemperature() + "°");
        mMaxTemperature.setText(weather.getMaxTemperature() + "°");
        mWindDirect.setText(weather.getWindDirect());
        mWindLevel.setText(weather.getWindLevel());

        mSportInfo.setText("运动建议："+weather.getSportInfo());
        mCarWashInfo.setText("洗车建议："+weather.getCarWashInfo());
        mComfortInfo.setText("舒适度："+weather.getComfortInfo());
        mPM25.setText(weather.getPM25());
        mAirQuality.setText(weather.getAirQuality());
        mAQI.setText(weather.getAQI());

        mUpdateTime.setText(weather.getUpdateTime());

        int weatherPicRcId = R.drawable.weather_sun;
        if(weather.getInfo().contains("雨")){
            weatherPicRcId = R.drawable.weather_rain;
        }else if(weather.getInfo().contains("云")){
            weatherPicRcId = R.drawable.weather_cloud;
        }else if(weather.getInfo().contains("雪")){
            weatherPicRcId = R.drawable.weather_snow;
        }
        mWeatherPic.setImageResource(weatherPicRcId);

        mMainLayout.setVisibility(View.VISIBLE);
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
