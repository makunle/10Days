package com.iflytek.mkl.imepracticedemo;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class AndroidInputMethodService extends InputMethodService implements View.OnClickListener {
    @Override
    public View onCreateInputView() {
        View view = getLayoutInflater().inflate(R.layout.keyboard, null);
        view.findViewById(R.id.a).setOnClickListener(this);
        view.findViewById(R.id.b).setOnClickListener(this);
        view.findViewById(R.id.c).setOnClickListener(this);
        view.findViewById(R.id.d).setOnClickListener(this);
        view.findViewById(R.id.back).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            hideWindow();
        }else{
            Button button = (Button) v;
            InputConnection inputConnection = getCurrentInputConnection();
            if(button.getId() == R.id.a){
                inputConnection.setComposingText(button.getText(), 1);
            }else{
                inputConnection.commitText(button.getText(), 1);
            }
        }
    }
}
