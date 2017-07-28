package com.iflytek.mkl.eventbustest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_msg);
        btnSend = (Button) findViewById(R.id.send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyEvent event = new MyEvent();
                event.setType("type0");
                event.setContent("type0 msg content");
                EventBus.getDefault().post(event);
            }
        });

        EventBus.getDefault().register(this);
    }

    private void postData(){
        String msg = "my msg";
        EventBus.getDefault().post(msg);
    }


    @Subscribe
    public void onEvent(MyEvent event){
        textView.append("\n"+event.getType() + " " + event.getContent());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventMainThread(MyEvent event){
        textView.append("\n"+event.getType() + " " + event.getContent());
    }
    //接受消息数据
//    public void Event(String msg){
//
//    }
//
//    public void onEventMainThread(String msg){
//
//    }
//
//    public void onEventPostThread(String msg){
//
//    }
//
//    public void onEventBackgroundThread(String msg){
//
//    }
//
//    public void onEventAsync(String msg){
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
