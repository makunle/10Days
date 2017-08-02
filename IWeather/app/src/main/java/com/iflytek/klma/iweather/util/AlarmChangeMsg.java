package com.iflytek.klma.iweather.util;

/**
 * Created by Administrator on 2017/8/2.
 */

public class AlarmChangeMsg {
    public static final int ADD = 1;
    public static final int DEL = 2;

    private int alarmId;  //对应的WeatherBookmark id
    private int type;           //事件类型
    private long alarmTime;

    public AlarmChangeMsg(int alarmId, int type, long alarmTime) {
        this.alarmId = alarmId;
        this.type = type;
        this.alarmTime = alarmTime;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}
