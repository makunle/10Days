package com.iflytek.klma.iweather.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.Alarm;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.gson.Weather;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.JsonUtil;
import com.iflytek.klma.iweather.util.Util;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/3.
 */

public class AlarmInfoBroadcastReceiver extends BroadcastReceiver {
    public static final String ALARM_ACTION = "ALARM_ACTION";
    public static final String BOOKMARK_ID = "BOOKMARK_ID";
    public static final String ALARM_ID = "ALARM_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alarm", "onReceive: alarm once from broadcast begin");


        if(!intent.getAction().equals(ALARM_ACTION)) return;


        Alarm nearestAlarm = DatabaseUtil.getInstance().getNearestAlarm();
        Util.activeNearestAlarm(context);

        if(nearestAlarm == null) return;
        if(nearestAlarm.getAlarmTime() > new Date().getTime()) return;

        WeatherBookmark bookmark = DatabaseUtil.getInstance().getWeatherBookmarkById(nearestAlarm.getWeatherBookmarkId());
        if(bookmark == null) return;

        Weather weather = JsonUtil.handleHefengJson(bookmark.getWeatherData());
        if(weather == null) return;

        Intent showIntent = new Intent(context, WeatherShowActivity.class);
        showIntent.putExtra(WeatherShowActivity.COUNTY_NAME, weather.getCountyName());

        //使用第二个参数和最后一个参数，解决点击通知后跳转不正确问题
        PendingIntent pendingIntent = PendingIntent.getActivity(context, bookmark.getId(), showIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(weather.getCountyName() + "天气 "+weather.getInfo())
                .setContentText("温度："+weather.getNowTemperature() + " "+weather.getWindDirect()+" "+weather.getWindLevel())
                .setTicker(weather.getCountyName() + "天气 "+weather.getInfo())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.i_ico)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), Util.getWeatherImageResource(weather)))
                .setLights(Color.GREEN, 1000,200)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(bookmark.getId(), notification);

        Log.d("Alarm", "onReceive: alarm once from broadcast done");


    }
}
