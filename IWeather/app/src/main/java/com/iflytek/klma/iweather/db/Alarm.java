package com.iflytek.klma.iweather.db;

import org.litepal.crud.DataSupport;

/**
 * 提醒
 * 多个提醒对应一个WeatherBookmark
 * 一个WeatherBookmark删除时，同时删除提醒
 */

public class Alarm extends DataSupport {

    private int id;

    private int weatherBookmarkId;  //对应与bookmark  1 ----> n  Alarm

    private long alarmTime;         //提醒时间

    private boolean repeat;         //是否重复

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeatherBookmarkId() {
        return weatherBookmarkId;
    }

    public void setWeatherBookmarkId(int weatherBookmarkId) {
        this.weatherBookmarkId = weatherBookmarkId;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}
