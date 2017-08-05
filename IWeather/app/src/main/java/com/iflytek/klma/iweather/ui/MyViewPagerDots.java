package com.iflytek.klma.iweather.ui;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;

import java.util.ArrayList;
import java.util.List;

import static com.iflytek.klma.iweather.R.id.toolbar_normal_right_img;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MyViewPagerDots extends LinearLayout {

    private List<ImageView> mDots;
    private Context mContext;
    private LinearLayout mContainer;
    private int mPosition;

    public MyViewPagerDots(Context context) {
        this(context, null);
    }

    public MyViewPagerDots(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPagerDots(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyViewPagerDots(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.viewpager_dots, this);
        mContainer = (LinearLayout) findViewById(R.id.dots_container);
        mDots = new ArrayList<ImageView>();
    }

    public void setDotsNumber(int num) {
        Log.d("Dots", "setChoseDot: dots num : " + num);
        if (mDots.size() < num) {
            int times = num - mDots.size();
            for (int i = 0; i < times; i++) {
                ImageView iv = new ImageView(mContext);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(12, 12);
                lp.setMargins(12, 0, 12, 0);
                iv.setLayoutParams(lp);

                iv.setImageResource(R.drawable.dot_select);

                mDots.add(iv);
                mContainer.addView(iv);
            }
        } else {
            int times = mDots.size() - num;
            for (int i = 0; i < times; i++) {
                mContainer.removeViewAt(mDots.size() - 1);
                mDots.remove(mDots.size() - 1);
            }
        }

    }

    public void setChoseDot(int position) {
        Log.d("Dots", "setChoseDot: position : " + position +"  size: " + mDots.size());

        if (mPosition < 0 || mPosition > mDots.size() - 1) {
            mPosition = position;
        }else {
            mDots.get(mPosition).setImageResource(R.drawable.dot_select);
        }
        if (position < 0 || position > mDots.size() - 1){
            return;
        }else {
            mDots.get(position).setImageResource(R.drawable.dot_not_select);
        }
        mPosition = position;
//        for(ImageView iv : mDots){
//            iv.setImageResource(R.drawable.dot_not_select);
//        }
    }
}
