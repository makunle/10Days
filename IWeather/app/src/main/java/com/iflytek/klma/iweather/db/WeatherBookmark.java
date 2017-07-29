package com.iflytek.klma.iweather.db;

import org.litepal.crud.DataSupport;

import java.sql.Date;


/**
 * 天气收藏数据
 * WeatherBookmark
 * ---------------
 *   +cityName
 *   +weatherData
 *   +markOrder
 */

public class WeatherBookmark extends DataSupport{

    private int id;

    private String weatherData; //保存城市天气json数据
    private Date updateTime;    //天气数据更新的时间
    private County county;      //县

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }
}
