package com.iflytek.klma.iweather.util.dbtool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/2.
 */

public class Util {
    /**
     * String 类型time转换为long类型，String格式为"yyyy-MM-dd HH:mm"
     * @param timeStr
     * @return 解析错误时返回0
     */
    public static long stringTime2long(String timeStr){
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
     * @param time
     * @return
     */
    public static String longTime2String(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm EE  M月d日");
        java.util.Date date = new Date(time);
        return dateFormat.format(date);
    }
}
