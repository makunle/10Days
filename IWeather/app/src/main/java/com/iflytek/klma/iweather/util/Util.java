package com.iflytek.klma.iweather.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.Alarm;
import com.iflytek.klma.iweather.gson.Weather;
import com.iflytek.klma.iweather.ui.AlarmInfoBroadcastReceiver;
import com.iflytek.klma.iweather.ui.AlarmSettingActivity;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/2.
 */

public class Util {
    /**
     * String 类型time转换为long类型，String格式为"yyyy-MM-dd HH:mm"
     *
     * @param timeStr
     * @return 解析错误时返回0
     */
    public static long stringTime2long(String timeStr) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            java.util.Date date = format.parse(timeStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * long 类型time转换为String，String格式为 "14:00 周一"
     *
     * @param time
     * @return
     */
    public static String longTime2String(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm EE");
        java.util.Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static void activeNearestAlarm(Context context) {
        DatabaseUtil.getInstance().cleanAlarm();
        //添加系统alarm,找寻最近的一个alarm添加
        Alarm nearestAlarm = DatabaseUtil.getInstance().getNearestAlarm();
        if (nearestAlarm == null) return;

        Intent intent = new Intent(context, AlarmInfoBroadcastReceiver.class);
        intent.setAction(AlarmInfoBroadcastReceiver.ALARM_ACTION);

        intent.putExtra(AlarmInfoBroadcastReceiver.BOOKMARK_ID, nearestAlarm.getWeatherBookmarkId());
        intent.putExtra(AlarmInfoBroadcastReceiver.ALARM_ID, nearestAlarm.getId());

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nearestAlarm.getAlarmTime(), sender);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, nearestAlarm.getAlarmTime(), sender);
        }

        String cn = DatabaseUtil.getInstance().getWeatherBookmarkById(nearestAlarm.getWeatherBookmarkId()).getCounty().getName();
        Log.d("Alarm", "set alarm time: " + nearestAlarm.getAlarmTime() + " " + cn);
    }

    public static String getDayShow(Date date){
        String dateShow = longTime2String(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat(" MM-dd");
        int deltaDay = date.getDay() - new Date().getDay();
        switch (deltaDay){
            case 0:
                dateShow += " 今天";
                break;
            case 1:
                dateShow += " 明天";
                break;
            case 2:
                dateShow += " 后天";
                break;
            default:
                dateShow += dateFormat.format(date);
        }
        return dateShow;
    }

    public static int getWeatherImageResource(Weather weather){
        int weatherPicRcId = R.drawable.weather_cloud;
        if(weather.getInfo().contains("雨")){
            weatherPicRcId = R.drawable.weather_rain;
        }else if(weather.getInfo().contains("云")){
            weatherPicRcId = R.drawable.weather_cloud;
        }else if(weather.getInfo().contains("雪")){
            weatherPicRcId = R.drawable.weather_snow;
        }else if(weather.getInfo().contains("阴")){
            weatherPicRcId = R.drawable.weather_gloomy;
        }else if(weather.getInfo().contains("晴")){
            weatherPicRcId = R.drawable.weather_sun;
        }

        return weatherPicRcId;
    }
}
