package com.iflytek.mkl.imepracticedemo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SmsReceiveObserver extends ContentObserver {

    private static final String TAG = "SmsReceiveObserver";

    private Context context;
    private Handler handler;

    public SmsReceiveObserver(Handler handler, Context context) {
        super(handler);
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body,type,date"};//"_id", "address", "person",, "date", "type
        String where = "date >=  " + (new Date().getTime() - 200);
        Cursor cursor = cr.query(Uri.parse("content://sms/"), projection, where, null, "date desc");
        if (cursor != null) {
            if (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Log.i(TAG, "getSmsFromPhone:短信内容===" + body);
                Log.d(TAG, "type == " + cursor.getString(cursor.getColumnIndex("type")));
                Log.d(TAG, "date == " + cursor.getString(cursor.getColumnIndex("date")));
                String code = VerificationCodeGetter.getCode(body);
                if(!TextUtils.isEmpty(code) && handler != null) {
                    Message msg = new Message();
                    msg.what = AndroidInputMethodService.IS_CODE;
                    Log.d(TAG, "code == " + code);
                    msg.obj = code;
                    handler.sendMessage(msg);
                }
            }
        }



        cursor.close();
    }
}
