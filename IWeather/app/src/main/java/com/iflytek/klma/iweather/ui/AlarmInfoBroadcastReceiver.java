package com.iflytek.klma.iweather.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.Alarm;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.gson.Weather;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.JsonUtil;
import com.iflytek.klma.iweather.util.Util;

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
        Intent showIntent = new Intent(context, WeatherShowActivity.class);

        int bookmarkId = intent.getIntExtra(BOOKMARK_ID, -1);
        if(bookmarkId == -1) return;

        int alarmId = intent.getIntExtra(ALARM_ID, -1);
        if(alarmId == -1) return;

        Alarm alarm = DatabaseUtil.getInstance().getAlarmById(alarmId);
        if(alarm == null) return;

        WeatherBookmark bookmark = DatabaseUtil.getInstance().getWeatherBookmarkById(bookmarkId);
        Weather weather = JsonUtil.handleHefengJson(bookmark.getWeatherData());
        showIntent.putExtra(WeatherShowActivity.COUNTY_NAME, weather.getCountyName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, showIntent, 0);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.i_ico)
                .setTicker("请查看订阅的天气情况")
                .setContentTitle(weather.getCountyName() + "天气 "+weather.getInfo())
                .setContentText("温度："+weather.getNowTemperature() + " "+weather.getWindDirect()+" "+weather.getWindLevel())
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
        Log.d("Alarm", "onReceive: alarm once from broadcast done");

        Util.activeNearestAlarm(context);
    }
}
