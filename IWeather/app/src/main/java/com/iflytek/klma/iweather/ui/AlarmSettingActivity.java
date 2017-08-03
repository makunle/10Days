package com.iflytek.klma.iweather.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.Alarm;
import com.iflytek.klma.iweather.util.AlarmChangeMsg;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.dbtool.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmSettingActivity extends AppCompatActivity {

    private static final String TAG = "AlarmSettingActivity";

    private static final String BOOKMARK_ID = "BOOKMARK_ID";

    private ListView mAlarmListView;
    private List<AlarmItem> mAlarmItems;
    private MyAlarmItemAdapter mAlarmItemAdapter;
    private MyToolbar mToolbar;

    private int mBookmarkId;

    public static void startMe(Context context, int bookmarkId) {
        Intent intent = new Intent(context, AlarmSettingActivity.class);
        intent.putExtra(BOOKMARK_ID, bookmarkId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        mBookmarkId = getIntent().getIntExtra(BOOKMARK_ID, -1);
        Log.d(TAG, "onCreate: " + mBookmarkId);

        mAlarmListView = (ListView) findViewById(R.id.alarm_list);

        mAlarmItems = new ArrayList<AlarmItem>();
        List<Alarm> alarms = DatabaseUtil.getInstance().getAllAlarmByWeatherBookmarkId(mBookmarkId);
        for (Alarm alarm : alarms) {
            mAlarmItems.add(new AlarmItem(alarm.getId(), alarm.getAlarmTime()));
        }
        Log.d(TAG, "onCreate: bookmark id: " + mBookmarkId +" list size : " + mAlarmItems.size());
        mAlarmItemAdapter = new MyAlarmItemAdapter(this, R.layout.item_alarm, mAlarmItems);
        mAlarmListView.setAdapter(mAlarmItemAdapter);

        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        mToolbar.getNormalLeft().setOnClickListener(buttonOnClieckListener);
        mToolbar.getNormalRight().setOnClickListener(buttonOnClieckListener);

        EventBus.getDefault().register(this);
    }

    /**
     * 响应EventBus分发的Alarm变化事件
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlarmChanged(AlarmChangeMsg msg) {
        Log.d(TAG, "onAlarmChanged: " + msg.getType());
        switch (msg.getType()){
            case AlarmChangeMsg.ADD:
                //ui listview刷新
                mAlarmItems.add(new AlarmItem(msg.getAlarmId(), msg.getAlarmTime()));
                //添加系统alarm

                break;
            case AlarmChangeMsg.DEL:
                //用于listview刷新
                for (int i = 0; i < mAlarmItems.size(); i++) {

                    if(mAlarmItems.get(i).getAlarmId() == msg.getAlarmId()){
                        mAlarmItems.remove(i);
                        break;
                    }
                }
                //取消系统alarm

                break;
        }
        mAlarmItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private View.OnClickListener buttonOnClieckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toolbar_normal_left:
                    //返回按钮
                    finish();
                    break;
                case R.id.toolbar_normal_right:
                    //添加一个提醒
                    MyTimeGetDialog dialog = new MyTimeGetDialog(AlarmSettingActivity.this, new MyTimeGetDialog.OnGetTimeListener() {
                        @Override
                        public void getTime(Date datetime) {
                            long alarmTime = datetime.getTime();
                            long now = new Date().getTime();
                            if(alarmTime <= now){
                                Toast.makeText(AlarmSettingActivity.this, "设置的时间即将过期或已过期，请重新设置", Toast.LENGTH_SHORT).show();
                            }else {
                                DatabaseUtil.getInstance().addAlarm(alarmTime, mBookmarkId);
                            }
                        }
                    });
                    dialog.show();
                    break;
            }
        }
    };

    private class MyAlarmItemAdapter extends ArrayAdapter<AlarmItem> {
        private int resource;

        public MyAlarmItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AlarmItem> objects) {
            super(context, resource, objects);
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            AlarmItem alarmItem = getItem(position);
            ViewHolder viewHolder;
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
                TextView alarmInfo = (TextView) view.findViewById(R.id.alarm_info);
                Button alarmDelete = (Button) view.findViewById(R.id.alarm_delete);
                viewHolder = new ViewHolder();
                viewHolder.alarmInfo = alarmInfo;
                viewHolder.deleteButton = alarmDelete;

                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.alarmInfo.setText(alarmItem.getTime());
            viewHolder.deleteButton.setTag(R.id.alarm_delete, alarmItem.getAlarmId());
            viewHolder.deleteButton.setOnClickListener(deleteButtonClickListener);

            return view;
        }
    }

    private View.OnClickListener deleteButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.alarm_delete) {
                int alarmId = (int) v.getTag(R.id.alarm_delete);
                DatabaseUtil.getInstance().deleteAlarmById(alarmId);
            }
        }
    };

    private class ViewHolder {
        TextView alarmInfo;
        Button deleteButton;
    }

    /**
     * 提示单元
     */
    private class AlarmItem {

        private int alarmId;
        private String time;

        public AlarmItem(int alarmId, long time) {
            this.alarmId = alarmId;
            this.time = Util.longTime2String(time);
        }

        public int getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(int alarmId) {
            this.alarmId = alarmId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
