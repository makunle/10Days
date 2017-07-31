package com.iflytek.mkl.vociesdktest.Gson;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/7/28.
 */

public class GsonUtil {
    public static IflyWeather handleIflyWeatherData(String data){
        return new Gson().fromJson(data, IflyWeather.class);
    }
}
