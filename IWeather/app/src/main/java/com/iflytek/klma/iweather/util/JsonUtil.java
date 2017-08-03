package com.iflytek.klma.iweather.util;

import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.klma.iweather.gson.HefengWeather;
import com.iflytek.klma.iweather.gson.IflyWeather;
import com.iflytek.klma.iweather.gson.Weather;

/**
 * Created by makunle on 2017/7/30.
 */

public class JsonUtil {
    private static final String TAG = "IWeather";
    /**
     * 和风天气json -> object
     * @param json
     * @return 如果不成功返回null
     */
    public static Weather handleHefengJson(String json){
        HefengWeather weather =  new Gson().fromJson(json, HefengWeather.class);
        if(weather.isStatusOk()) return weather;
        return null;
    }

    /**
     * 语义理解天气 json -> object
     * @param json
     * @return
     */
    public static Weather handleIflyWeatherJson(String json){
        IflyWeather weather = new Gson().fromJson(json, IflyWeather.class);
        if(weather.getRc() != 0){
            Log.d(TAG, "handleIflyWeatherJson: is null");
            return null;
        }
        Log.d(TAG, "handleIflyWeatherJson: is not null");
        return weather;
    }
}
