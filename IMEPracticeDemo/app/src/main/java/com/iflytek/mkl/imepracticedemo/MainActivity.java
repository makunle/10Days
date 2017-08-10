package com.iflytek.mkl.imepracticedemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.iflytek.mkl.imepracticedemo.permission.NavigateManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.permission_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReadSMSPermission();
            }
        });


//        ContentResolver cr = getContentResolver();
//        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
//        String where = "  date >  " + 0 + "";
//        Cursor cursor = cr.query(Uri.parse("content://sms/inbox"), null, where, null, "date desc limit 10");
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String body = cursor.getString(cursor.getColumnIndex("body"));
//                Log.i(TAG, "body ===" + cursor.getString(cursor.getColumnIndex("body")));
//                Log.i(TAG, "status ===" + cursor.getString(cursor.getColumnIndex("status")));
//                Log.i(TAG, "date ===" + cursor.getString(cursor.getColumnIndex("date")));
//                Log.i(TAG, "seen ===" + cursor.getString(cursor.getColumnIndex("seen")));
//                Log.i(TAG, "read ===" + cursor.getString(cursor.getColumnIndex("read")));
//                String id = cursor.getString(cursor.getColumnIndex("_id"));
//                String code = VerificationCodeGetter.getCode(body);
////
////                ContentValues values = new ContentValues();
////                values.put("read", 1);
////                int res = getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id = ?", new String[]{id + ""});
////                Log.d(TAG, "upate === " + res + " id === " + id);
//
//            }
//        }
    }

    private boolean getReadSMSPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE);
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE);
            return false;
        }
        NavigateManager.with(this).navigate();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "can't get permission", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }
}
