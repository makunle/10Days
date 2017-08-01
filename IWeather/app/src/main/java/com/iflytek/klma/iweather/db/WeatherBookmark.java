package com.iflytek.klma.iweather.db;

import android.icu.text.SimpleDateFormat;
import android.text.format.DateUtils;

import com.iflytek.klma.iweather.util.DatabaseUtil;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.typechange.DateOrm;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;


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
    private long updateTime;    //天气数据更新的时间
    private int countyId;       //县
    private int showOrder;      //用于控制在viewpage中显示的顺序，递增显示

    public County getCounty(){
        return DataSupport.where("id = ?", String.valueOf(countyId)).findFirst(County.class);
    }

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

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public java.util.Date getUpdateTime() {
        return new Date(updateTime);
    }

    /**
     * 更新时间 获取到的格式为"2017-07-30 13:49"，需转换为long
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            java.util.Date date = format.parse(updateTime);
            this.updateTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }
}