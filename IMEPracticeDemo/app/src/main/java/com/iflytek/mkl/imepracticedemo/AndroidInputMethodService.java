package com.iflytek.mkl.imepracticedemo;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AndroidInputMethodService extends InputMethodService implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "AndroidInputMethodServi";

    public static int IS_CODE = 1;

    private List<TextView> keys = new ArrayList<>();
    private boolean upperCase = false;
    private TextView candidateTextView;

    private SmsReceiveObserver smsReceiveObserver;
    private SmsReceiveBroadcastReceiver smsReceiveBroadcastReceiver;

    private Handler codeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == IS_CODE && candidateTextView != null) {
                candidateTextView.setText((String) msg.obj);
            }
        }
    };

    private void registerObserver() {
        smsReceiveObserver = new SmsReceiveObserver(codeHandler, this);
        getContentResolver().registerContentObserver(
                Uri.parse("content://sms/"), true,
                smsReceiveObserver
        );
    }

    private void registerReceiver() {
        smsReceiveBroadcastReceiver = new SmsReceiveBroadcastReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(smsReceiveBroadcastReceiver, filter);
        smsReceiveBroadcastReceiver.setMessageListener(new SmsReceiveBroadcastReceiver.MessageListener() {
            @Override
            public void onReceiveVerificationCode(String code) {
                if (candidateTextView != null) candidateTextView.setText(code);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        registerReceiver();   //使用BroadcastReceiver方式监听短信
        registerObserver();     //使用ContentObserver监听短信
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (smsReceiveObserver != null)
            getContentResolver().unregisterContentObserver(smsReceiveObserver);
        if (smsReceiveBroadcastReceiver != null)
            unregisterReceiver(smsReceiveBroadcastReceiver);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getVerificationCode(String code) {
        candidateTextView.setText(code);
    }

    @Override
    public void onWindowShown() {
        super.onWindowShown();
        Log.d(TAG, "onWindowShown: ");
    }

    @Override
    public View onCreateInputView() {
        Log.d(TAG, "onCreateInputView: ");
        View view = getLayoutInflater().inflate(R.layout.keyboard, null);
        for (int i = R.id.n1; i <= R.id.delete; i++) {
            TextView key = (TextView) view.findViewById(i);
            key.setOnClickListener(this);
            key.setOnLongClickListener(this);
            keys.add(key);
        }
        view.findViewById(R.id.a);
        return view;
    }

    @Override
    public void onClick(View v) {
        InputConnection inputConnection = getCurrentInputConnection();
        switch (v.getId()) {
            case R.id.hide:
                hideWindow();
                break;
            case R.id.caseChange:
                for (TextView key : keys) {
                    if (!upperCase) key.setText(key.getText().toString().toUpperCase());
                    else key.setText(key.getText().toString().toLowerCase());
                }
                upperCase = !upperCase;
                break;
            case R.id.delete:
                inputConnection.deleteSurroundingText(1, 0);
                break;
            case R.id.candidate:
                inputConnection.commitText(candidateTextView.getText().toString(), 1);
                candidateTextView.setText("");
                break;
            default:
                TextView editText = (TextView) v;
                inputConnection.commitText(editText.getText().toString(), 1);
                break;
        }
    }

    @Override
    public View onCreateCandidatesView() {
        Log.d(TAG, "onCreateCandidatesView: ");
        setCandidatesViewShown(true);
        View v = getLayoutInflater().inflate(R.layout.candidate, null);
        candidateTextView = (TextView) v.findViewById(R.id.candidate);
        candidateTextView.setOnClickListener(this);
        v.findViewById(R.id.hide).setOnClickListener(this);
        return v;
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                InputConnection inputConnection = getCurrentInputConnection();
                inputConnection.deleteSurroundingText(Integer.MAX_VALUE, 0);
                return true;
        }
        return false;
    }
}
