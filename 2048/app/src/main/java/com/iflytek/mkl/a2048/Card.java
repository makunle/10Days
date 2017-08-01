package com.iflytek.mkl.a2048;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/31.
 */

public class Card extends FrameLayout {
    private int num = 0;

    private TextView label;

    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);
        label.setBackgroundColor(0x33ffffff );

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);
        setNum(0);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num <= 0) label.setText("");
        else label.setText(""+num);
    }

    public boolean equals(Card c){
        return getNum() == c.getNum();
    }
}
