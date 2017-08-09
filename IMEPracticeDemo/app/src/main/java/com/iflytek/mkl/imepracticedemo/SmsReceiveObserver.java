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
        String[] projection = new String[]{"_id", "body"};//"_id", "address", "person",, "date", "type
        //5s之内收到的短信
        long now = new Date().getTime() - 5000;
        String where = "  date >  " + now + " and seen = 0";
        Cursor cursor = cr.query(Uri.parse("content://sms/inbox"), projection, where, null, "date desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                String code = VerificationCodeGetter.getCode(body);
                if (!TextUtils.isEmpty(code) && handler != null) {
                    Message msg = new Message();
                    msg.what = AndroidInputMethodService.IS_CODE;
                    msg.obj = VerificationCodeGetter.getCode(body);
                    handler.sendMessage(msg);

                    ContentValues values = new ContentValues();
//                    values.put("read", 1);  //设置read为1不提醒
                    values.put("seen", 1);  //设置seen为1也提醒
                    int res = cr.update(Uri.parse("content://sms/inbox"), values, "_id = ?", new String[]{id + ""});
                }
            }
        }
        cursor.close();
    }
}
