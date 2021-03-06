package com.iflytek.klma.iweather.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.gson.Weather;
import com.iflytek.klma.iweather.util.DBChangeMsg;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.HttpUtil;
import com.iflytek.klma.iweather.util.JsonUtil;
import com.iflytek.klma.iweather.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WeatherShowActivity extends AppCompatActivity {

    private static final String TAG = "WeatherShowActivity";
    private static final String URL = "http://guolin.tech/api/weather?cityid=";
    private static final String KEY = "7decd6786b9e47ba806484d665f685e6";
    public static final String COUNTY_NAME = "COUNTY_NAME";

    private List<WeatherInfoFragment> mWeatherInfoPages = new ArrayList<WeatherInfoFragment>();

    private ViewPager mPageContainer;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private MyToolbar mToolbar;
    private MyViewPagerDots mDotsPage;

    public static void startMe(Context context, String countyName) {
        Intent intent = new Intent(context, WeatherShowActivity.class);
        intent.putExtra(COUNTY_NAME, countyName);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String countyName = intent.getStringExtra(WeatherShowActivity.COUNTY_NAME);
        if(!TextUtils.isEmpty(countyName)){
            setPageToCountyByName(countyName);
            Log.d(TAG, "onNewIntent: seted");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: WeatherShow");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_show);

        mPageContainer = (ViewPager) findViewById(R.id.view_page);
        mSwipeRefresh = (VerticalSwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        mDotsPage = (MyViewPagerDots) findViewById(R.id.dots_page);

        mToolbar.getNormalRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeatherShowActivity.this, BookmarkSettingActivity.class));
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAllPages();
            }
        });

        initViewPagers();

        refreshOnlyIfNeed();

        EventBus.getDefault().register(this);

        String countyName = getIntent().getStringExtra(COUNTY_NAME);
        if (TextUtils.isEmpty(countyName)) countyName = "";
        setPageToCountyByName(countyName);

        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(0x55000000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 相应数据库的增删操作，反映到界面上
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatabaseDataChanged(DBChangeMsg msg) {
        switch (msg.getType()) {
            case DBChangeMsg.ADD:
                WeatherBookmark bookmark = DatabaseUtil.getInstance().getWeatherBookmarkById(msg.getBookMarkId());
                WeatherInfoFragment fragment = new WeatherInfoFragment();
                fragment.setmBookmarkId(msg.getBookMarkId());
                mWeatherInfoPages.add(fragment);

                pagerAdapter.notifyDataSetChanged();
                mPageContainer.setCurrentItem(mWeatherInfoPages.size());
                break;
            case DBChangeMsg.DEL:
                for (int i = 0; i < mWeatherInfoPages.size(); i++) {
                    if (mWeatherInfoPages.get(i).getmBookmarkId() == msg.getBookMarkId()) {
                        mWeatherInfoPages.remove(i);
                        break;
                    }
                }
                pagerAdapter.notifyDataSetChanged();
                setPageToCountyByName(mToolbar.getTitleTextView().getText().toString());

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(msg.getBookMarkId());

                break;
            case DBChangeMsg.SET:
                Log.d(TAG, "onDatabaseDataChanged: + set : " + msg.getBookMarkId());
                setPageToCountyByName(DatabaseUtil.getInstance().getCountyByBookmarkId(msg.getBookMarkId()).getName());
                break;
        }
        //更新WeatherBookmark的showOrder
        for (int i = 0; i < mWeatherInfoPages.size(); i++) {
            DatabaseUtil.getInstance().updateWeatherBookMarkShowOrder(mWeatherInfoPages.get(i).getmBookmarkId(), i + 1);
        }
        refreshOnlyIfNeed();
    }

    private void setPageToCountyByName(String countyName) {
        List<WeatherBookmark> bookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        int currentItem = Math.max(0, bookmarks.size() - 1);
//        int currentItem = 0;
        if (!TextUtils.isEmpty(countyName)) {
            for (int i = 0; i < bookmarks.size(); i++) {
                if (bookmarks.get(i).getCounty().getName().equals(countyName)) {
                    currentItem = i;
                    break;
                }
            }
        }
        mPageContainer.setCurrentItem(currentItem);
        mToolbar.setTitle(bookmarks.get(currentItem).getCounty().getName());
        mDotsPage.setDotsNumber(bookmarks.size());
        mDotsPage.setChoseDot(currentItem);
    }

    /**
     * 根据当前数据库的内容初始化ViewPage，并设置最初页面标题
     */
    private void initViewPagers() {
        List<WeatherBookmark> bookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        for (WeatherBookmark bookmark : bookmarks) {
            WeatherInfoFragment fragment = new WeatherInfoFragment();
            fragment.setmBookmarkId(bookmark.getId());
            mWeatherInfoPages.add(fragment);
        }

        mPageContainer.setAdapter(pagerAdapter);
        mPageContainer.addOnPageChangeListener(pageChangeListener);
    }

    /**
     * 刷新所有数据
     */
    private void refreshAllPages() {
        List<Integer> updateList = new ArrayList<Integer>();
        for (WeatherInfoFragment wf : mWeatherInfoPages) {
            updateList.add(wf.getmBookmarkId());
        }
        requestWeatherDataAndRefresh(updateList);
    }

    /**
     * 仅刷新需要刷新的数据
     */
    private void refreshOnlyIfNeed() {
        List<Integer> updateList = getNeedUpdateBookmarkList();
        if (updateList.size() <= 0) return;
        requestWeatherDataAndRefresh(updateList);
    }

    /**
     * 用于ViewPage的显示，使用FragmentStatePagerAdapter控制删除问题
     */
    private FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return mWeatherInfoPages.get(position);
        }

        @Override
        public int getCount() {
            return mWeatherInfoPages.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;  //解决删除Fragment问题
        }
    };

    /**
     * 用于响应PageView滑动相关的事件
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int bookmarkId = mWeatherInfoPages.get(position).getmBookmarkId();
            String name = DatabaseUtil.getInstance().getCountyByBookmarkId(bookmarkId).getName();
            setPageToCountyByName(name);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 刷新指定的bookmark数据，执行更新操作
     */
    private void requestWeatherDataAndRefresh(final List<Integer> updateIdList) {

        mSwipeRefresh.setRefreshing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int wbId : updateIdList) {
                    WeatherBookmark wb = DatabaseUtil.getInstance().getWeatherBookmarkById(wbId);
                    String weatherId = DatabaseUtil.getInstance().getCountyByBookmarkId(wbId).getWeatherId();
                    String requestUrl = URL + weatherId + "&key=" + KEY;
                    String json = HttpUtil.getResponse(requestUrl);
                    if (!TextUtils.isEmpty(json)) {
                        Weather weather = JsonUtil.handleHefengJson(json);
                        if(weather != null) {
                            wb.setWeatherData(json);
                            wb.setUpdateTime(Util.stringTime2long(weather.getUpdateTime()));
                            wb.save();
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDataToPages();
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();


    }

    /**
     * 数据库内数据得到更新后，将更新刷新到界面上
     */
    private void showDataToPages() {
        for (WeatherInfoFragment wf : mWeatherInfoPages) {
            if (wf.isViewCreated()) wf.refreshView();
        }
    }

    /**
     * 获取需要拉取天气数据的bookmark,5小时之前的天气都需要更新
     *
     * @return
     */
    private List<Integer> getNeedUpdateBookmarkList() {
        List<WeatherBookmark> bookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        List<Integer> needupdate = new ArrayList<Integer>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Date now = new Date();
        for (WeatherBookmark wb : bookmarks) {
            Weather weather = JsonUtil.handleHefengJson(wb.getWeatherData());
            //最近一次更新时间在2小时以前的都需要更新
            if(!(weather != null && weather.isDataUsable()) || now.getTime() - wb.getUpdateTime().getTime() > 5 * 60 * 60 * 1000){
                needupdate.add(wb.getId());
            }
        }
        return needupdate;
    }
}
