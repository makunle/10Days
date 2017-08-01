package com.iflytek.mkl.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/31.
 */

public class GameView extends GridLayout {

    private Card[][] cardsMap = new Card[4][4];

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {
        setColumnCount(4);
        addCards(263, 263);
        setBackgroundColor(0xffbbada0);
        startGame();
        setOnTouchListener(new OnTouchListener() {
            private float startX, startY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float deltaX = motionEvent.getX() - startX;
                        float deltaY = motionEvent.getY() - startY;

                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (deltaX < -5) {
                                swipeLeft();//left
                            } else if (deltaX > 5) {
                                swipeRight();//right
                            }

                        } else {
                            if (deltaY < -5) {
                                swipeUp();//up
                            } else if (deltaY > 5) {
                                swipeDown();//down
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = ((Math.min(w, h) - 10) / 4);
        addCards(cardWidth, cardWidth);
    }

    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                Log.d("TAG", "addCards: ");
                cardsMap[i][j] = c;
            }
        }
    }

    private void swipeLeft() {
        addRandomNumber();
    }

    private void swipeRight() {
        Log.d("TAG", "right: ");addRandomNumber();
    }

    private void swipeUp() {
        Log.d("TAG", "up: ");addRandomNumber();
    }

    private void swipeDown() {
        Log.d("TAG", "down: ");addRandomNumber();
    }

    private void addRandomNumber() {
        int a = getRandom(10) > 2 ? 2 : 4;
        Point p = getSpaceCard();
        if(p == null)return;
        cardsMap[p.x][p.y].setNum(a);
        invalidate();
        Log.d("TAG", "addRandomNumber: "+p.x+" "+p.y);
    }

    private void startGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardsMap[i][j].setNum(0);
            }
        }
        addRandomNumber();
        addRandomNumber();
    }

    private Point getSpaceCard() {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cardsMap[i][j].getNum() == 0) points.add(new Point(i, j));
            }
        }
        int r = getRandom(points.size());
        return points.get(r);
    }


    private int getRandom(int round) {
        final Random random = new Random();
        return random.nextInt(round);
    }
}
