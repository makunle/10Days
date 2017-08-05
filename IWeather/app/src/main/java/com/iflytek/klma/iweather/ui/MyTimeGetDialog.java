package com.iflytek.klma.iweather.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MyTimeGetDialog {

    private static final String TAG = "MyTimeGetDialog";

    private Calendar calendar;
    AlertDialog dialog;

    public MyTimeGetDialog(final Context context, final OnGetTimeListener getTime) {

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) +1);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("请选择");
        View view = LayoutInflater.from(context).inflate(R.layout.datetime_get_dialog, null);
        builder.setView(view);

        final Button timebutton = (Button) view.findViewById(R.id.get_time);
        final Button datebutton = (Button) view.findViewById(R.id.get_date);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd EE");

        timebutton.setText("选择日期：" + Util.getDayShow(calendar));
        datebutton.setText("选择时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + (calendar.get(Calendar.HOUR_OF_DAY) > 12 ? " 下午" : " 上午"));

        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        timebutton.setText("选择日期：" + Util.getDayShow(calendar));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        datebutton.setText("选择时间：" + hourOfDay + ":" + minute + (hourOfDay > 12 ? " 下午" : " 上午"));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getTime.getTime(calendar);
            }
        });

        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

    public static interface OnGetTimeListener {
        void getTime(Calendar calendar);
    }
}
