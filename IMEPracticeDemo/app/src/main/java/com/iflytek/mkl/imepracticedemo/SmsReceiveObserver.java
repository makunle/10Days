package com.iflytek.mkl.imepracticedemo;

import android.content.ContentResolver;
import android.content.ContentValues;
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

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if(selfChange) return;
        if(!pattern.matcher(uri.toString()).matches()) return;

        ContentResolver contentObserver = context.getContentResolver();
        Cursor cursor = contentObserver.query(uri, null, null, null, null);
        if(cursor != null){
            if(cursor.moveToNext()){
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String code = VerificationCodeGetter.getCode(body);
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
}
