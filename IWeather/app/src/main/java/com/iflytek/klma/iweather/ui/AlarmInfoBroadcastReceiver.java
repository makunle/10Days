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
import java.util.List;

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

        //处理所有的过期alarm，这样同时可以有多个alarm被触发
        List<Alarm> alarmList = DatabaseUtil.getInstance().getAllOutOfDateAlarm();
        Util.activeNearestAlarm(context);

        if (!intent.getAction().equals(ALARM_ACTION)) {
            return;
        }

        for (Alarm nearestAlarm : alarmList) {
            if (nearestAlarm == null) continue;
            if (nearestAlarm.getAlarmTime() > new Date().getTime()) continue;

            WeatherBookmark bookmark = DatabaseUtil.getInstance().getWeatherBookmarkById(nearestAlarm.getWeatherBookmarkId());
            if (bookmark == null) continue;

            Weather weather = JsonUtil.handleHefengJson(bookmark.getWeatherData());
            if (weather == null) continue;

            Intent showIntent = new Intent(context, WeatherShowActivity.class);
            showIntent.putExtra(WeatherShowActivity.COUNTY_NAME, weather.getCountyName());

            //使用第二个参数和最后一个参数，解决点击通知后跳转不正确问题
            PendingIntent pendingIntent = PendingIntent.getActivity(context, bookmark.getId(), showIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            String infoText = weather.getCountyName() + " " + weather.getInfo();

            String tempText = "温度: " + weather.getNowTemperature() +
                    "°  (" + weather.getMinTemperature() + "° ~ " + weather.getMaxTemperature() + "°)  ";

            if (!weather.getAirQuality().equals("-1")) tempText += "空气" + weather.getAirQuality();
            tempText += "     更新时间：" + weather.getUpdateTime().split(" ")[1];

            Notification notification = new Notification.Builder(context)
                    .setContentTitle(infoText)
                    .setContentText(tempText)
                    .setTicker(weather.getCountyName() + weather.getInfo())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(Util.getWeatherImageResource(weather))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(bookmark.getId(), notification);

            Log.d("Alarm", "onReceive: alarm once from broadcast done");

        }
    }
}
