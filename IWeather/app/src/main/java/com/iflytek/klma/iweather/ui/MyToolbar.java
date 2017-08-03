package com.iflytek.klma.iweather.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;

import static com.iflytek.klma.iweather.R.id.toolbar_normal_right_img;

/**
 * 支持一般toolbar类型与搜索框类型
 */

public class MyToolbar extends LinearLayout {
    private static final String TAG = "MyToolbar";

    public static final int NORMAL_TYPE = 0;
    public static final int SEARCH_TYPE = 1;
    private int mType;

    private ImageView mNormalLeftImage;
    private ImageView mNormalRightImage;
    private View mNormalLeft;
    private View mNormalRight;
    private ImageButton mSearchLeftButton;
    private ImageButton mSearchRightButton;

    private TextView mTitleTextView;
    private EditText mInputEditText;

    private LinearLayout normalLayout;
    private LinearLayout searchLayout;

    public MyToolbar(Context context) {
        this(context, null);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.MyToolbar, defStyleAttr, 0);

        mType = a.getInt(R.styleable.MyToolbar_titleType, 0);

        if(mType == NORMAL_TYPE)    //类型为一般导航toolbar
        {
            normalLayout.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);

            Drawable leftBackgroundImage = a.getDrawable(R.styleable.MyToolbar_leftBackground);
            if(leftBackgroundImage != null){
                mNormalLeftImage.setBackground(leftBackgroundImage);
            }

            Drawable rightBackgroundImage = a.getDrawable(R.styleable.MyToolbar_rightBackground);
            if(rightBackgroundImage != null){
                mNormalRightImage.setBackground(rightBackgroundImage);
            }

            String title = a.getString(R.styleable.MyToolbar_title);
            if(!TextUtils.isEmpty(title)){
                mTitleTextView.setText(title);
            }

            boolean showLeftButton = a.getBoolean(R.styleable.MyToolbar_showLeftButton, true);
            if(!showLeftButton){
                mNormalLeft.setVisibility(View.INVISIBLE);
            }

            boolean showRightButton = a.getBoolean(R.styleable.MyToolbar_showRightButton, true);
            if(!showRightButton){
                mNormalRight.setVisibility(View.INVISIBLE);
            }

        } else if(mType == SEARCH_TYPE){    //类型为搜索toobar
            normalLayout.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);

            String hint = a.getString(R.styleable.MyToolbar_inputTextHint);
            if(!TextUtils.isEmpty(hint)){
                mInputEditText.setHint(hint);
            }
        }

        a.recycle();

//        if(Build.VERSION.SDK_INT >= 21){
//            View decorView = getContext().getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toolbar, this);
        mNormalLeftImage = (ImageView) findViewById(R.id.toolbar_normal_left_img);
        mNormalRightImage = (ImageView) findViewById(toolbar_normal_right_img);
        mNormalLeft = findViewById(R.id.toolbar_normal_left);
        mNormalRight = findViewById(R.id.toolbar_normal_right);
        mSearchLeftButton = (ImageButton) findViewById(R.id.toolbar_left_search);
        mSearchRightButton = (ImageButton) findViewById(R.id.toolbar_right_search);

        mInputEditText = (EditText) findViewById(R.id.toolbar_input);
        mTitleTextView = (TextView) findViewById(R.id.toolbar_title);

        normalLayout = (LinearLayout) findViewById(R.id.toolbar_type_normal);
        searchLayout = (LinearLayout) findViewById(R.id.toolbar_type_search);
    }

//    public ImageButton getNormalLeftButton() {
//        return mNormalLeftImage;
//    }
//
//    public ImageButton getNormalRightButton() {
//        return mNormalRightImage;
//    }

    public View getNormalLeft() {
        return mNormalLeft;
    }

    public View getNormalRight() {
        return mNormalRight;
    }


    public ImageButton getSearchLeftButton() {
        return mSearchLeftButton;
    }

    public ImageButton getSearchRightButton() {
        return mSearchRightButton;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public EditText getInputEditText() {
        return mInputEditText;
    }

    public void setTitle(String title){
        mTitleTextView.setText(title);
    }
}
