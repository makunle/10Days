package com.iflytek.mkl.imepracticedemo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SmsReceiveObserver extends ContentObserver {

    private static final String TAG = "SmsReceiveObserver";

    private Context context;
    private Handler handler;
    private Pattern pattern = Pattern.compile("content://sms/\\d+");    //只接收content://sms/1321 这样的onChange

    public SmsReceiveObserver(Handler handler, Context context) {
        super(handler);
        this.handler = handler;
        this.context = context;
    }

    private void deal(boolean selfChange, Uri uri){
        Log.d(TAG, "onChange with two paramater");
        if (selfChange) return;
        if (!pattern.matcher(uri.toString()).matches()) return;

        ContentResolver contentObserver = context.getContentResolver();
        Cursor cursor = contentObserver.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String code = VerificationCodeGetter.getCode(body);
                Toast.makeText(context, "observer msg:" + body, Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(code) && handler != null) {
                    Message msg = new Message();
                    msg.what = AndroidInputMethodService.IS_CODE;

                    msg.obj = VerificationCodeGetter.getCode(body);
                    handler.sendMessage(msg);
                }
            }
            cursor.close();
        }
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        deal(selfChange, uri);
    }

    @Override
    public void onChange(boolean selfChange) {
        if (Build.VERSION.SDK_INT >= 16) return;
        Toast.makeText(context, "in one param func begin", Toast.LENGTH_SHORT).show();
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/inbox"), null, null, null, "date desc limit 1"
        );
        Toast.makeText(context, "in one param func" + (cursor == null), Toast.LENGTH_SHORT).show();
        if (cursor != null) {
            cursor.moveToNext();
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            deal(selfChange, Uri.parse("content://sms/" + id));
            Log.d(TAG, "onChange with one paramater");
        }
    }
}
