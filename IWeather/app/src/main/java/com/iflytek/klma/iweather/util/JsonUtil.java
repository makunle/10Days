package com.iflytek.klma.iweather.util;

import com.google.gson.Gson;
import com.iflytek.klma.iweather.gson.HefengWeather;

/**
 * Created by makunle on 2017/7/30.
 */

public class JsonUtil {
    public static HefengWeather handleHefengJson(String json){
        return new Gson().fromJson(json, HefengWeather.class);
    }
}
