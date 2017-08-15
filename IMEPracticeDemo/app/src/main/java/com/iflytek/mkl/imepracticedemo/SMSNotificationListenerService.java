package com.iflytek.mkl.imepracticedemo;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SMSNotificationListenerService extends NotificationListenerService {

    private static final String TAG = "NotificationObserveServ";

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return super.onBind(intent);
    }


    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "onListenerDisconnected");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * 获取新post的notification，
     * API < 18 时，可用accessibility
     * @param sbn
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        Notification notification = sbn.getNotification();
        String code = null;
        //如果tickerText有内容，直接获取
        if (!TextUtils.isEmpty(notification.tickerText)) {
            code = VerificationCodeGetter.getCode(notification.tickerText.toString());
        } else {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            code = greaterThanAPI18(notification);
//            }else{
            //API小于18时，使用accessibility
//            }
        }
        if (code == null) code = "";
        EventBus.getDefault().post(code);
    }

    private String greaterThanAPI18(Notification notification) {
        if (notification.contentView == null) return null;
        try {
            RemoteViews views = notification.contentView;
            Field field = views.getClass().getDeclaredField("mActions");
            field.setAccessible(true);
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);
            // Find the setText() and setTime() reflection actions
            for (Parcelable p : actions) {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                // The tag tells which type of action it is (2 is ReflectionAction, from the source)
                int tag = parcel.readInt();
                if (tag != 2) continue;
                // View ID
                parcel.readInt();
                String methodName = parcel.readString();
                if (null == methodName) {
                    continue;
                } else if (methodName.equals("setText")) {
                    // Parameter type (10 = Character Sequence)
                    parcel.readInt();
                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    String code = VerificationCodeGetter.getCode(t);
                    if (!TextUtils.isEmpty(code)) return code;
                }
                parcel.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
