package com.iflytek.klma.iweather.ui;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by makunle on 2017/7/30.
 */

public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {

    private float startX;
    private float startY;
    private boolean isDragging;
    private final int threshold;

    public VerticalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        threshold = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                isDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDragging) return false;
                float deltaX = Math.abs(event.getX() - startX);
                float deltaY = Math.abs(event.getY() - startY);
                if(deltaX > threshold && deltaX > deltaY){
                    isDragging = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
