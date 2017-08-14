package com.iflytek.mkl.imepracticedemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iflytek.mkl.imepracticedemo.permission.NavigateManager;

import java.security.Provider;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String BROADCAST = "broadcast";
    public static final String CONTENT = "content";
    public static final String NOTIFICATION = "notification";

    public static final String PREFERENCE = "tmpsave";

    private static final int REQUEST_CODE = 1;

    RadioButton broadcastLtnBtn;
    RadioButton contentRecBtn;
    RadioButton notificationReadBtn;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        editor = preferences.edit();

        broadcastLtnBtn = (RadioButton) findViewById(R.id.use_broadcast);
        contentRecBtn = (RadioButton) findViewById(R.id.use_content_observer);
        notificationReadBtn = (RadioButton) findViewById(R.id.use_notification_listener);

        broadcastLtnBtn.setOnClickListener(clickListener);
        contentRecBtn.setOnClickListener(clickListener);
        notificationReadBtn.setOnClickListener(clickListener);
        findViewById(R.id.code_permission).setOnClickListener(clickListener);
        findViewById(R.id.notification_permission).setOnClickListener(clickListener);
        findViewById(R.id.sms_permission).setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastLtnBtn.setChecked(preferences.getBoolean(BROADCAST, false));
        contentRecBtn.setChecked(preferences.getBoolean(CONTENT, false));
        notificationReadBtn.setChecked(preferences.getBoolean(NOTIFICATION, true));
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.use_broadcast:
                case R.id.use_content_observer:
                case R.id.use_notification_listener:
                    editor.putBoolean(BROADCAST, broadcastLtnBtn.isChecked());
                    editor.putBoolean(CONTENT, contentRecBtn.isChecked());
                    editor.putBoolean(NOTIFICATION, notificationReadBtn.isChecked());
                    editor.commit();
                    break;
                case R.id.code_permission:
                    //获取通知类短信读取权限
                    NavigateManager.with(MainActivity.this).navigate();
                    break;
                case R.id.notification_permission:
                    //获取通知使用权
                    getNotificationReadPermission();
                    break;
                case R.id.sms_permission:
                    getReadSMSPermission();
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

    /**
     * 如果没获取通知使用权，跳转到通知使用权设置页面
     */
    private void getNotificationReadPermission() {
//        if (isNotificationPermissionGranted()) return;
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否已经获取通知使用权
     *
     * @return true 为已经获取
     */
    private boolean isNotificationPermissionGranted() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(getPackageName())) {
            return true;
        }
        return false;
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
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "can't get permission", Toast.LENGTH_SHORT).show();
            } else {
                getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            }
        }
    }
}
