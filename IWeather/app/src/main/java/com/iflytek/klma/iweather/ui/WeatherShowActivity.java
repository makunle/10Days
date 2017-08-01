package com.iflytek.klma.iweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.gson.HefengWeather;
import com.iflytek.klma.iweather.util.DBChangeMsg;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.HttpUtil;
import com.iflytek.klma.iweather.util.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherShowActivity extends AppCompatActivity {

    private static final String TAG = "WeatherShowActivity";
    private static final String URL = "http://guolin.tech/api/weather?cityid=";
    private static final String KEY = "7decd6786b9e47ba806484d665f685e6";

    private List<WeatherInfoFragment> weatherInfoPages = new ArrayList<WeatherInfoFragment>();

    private TextView countyName;
    private TextView showTime;
    private ViewPager pageContainer;
    private VerticalSwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        countyName = (TextView) findViewById(R.id.county_name);
        showTime = (TextView) findViewById(R.id.show_time);
        pageContainer = (ViewPager) findViewById(R.id.view_page);
        swipeRefresh = (VerticalSwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeatherShowActivity.this, SettingActivity.class));
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAllPages();
            }
        });

        initViewPagers();

        refreshOnlyIfNeed();

        EventBus.getDefault().register(this);
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
                fragment.setBookmarkId(msg.getBookMarkId());
                weatherInfoPages.add(fragment);

                break;
            case DBChangeMsg.DEL:
                for (int i = 0; i < weatherInfoPages.size(); i++) {
                    if (weatherInfoPages.get(i).getBookmarkId() == msg.getBookMarkId()) {
                        weatherInfoPages.remove(i);
                        break;
                    }
                }
                break;
        }
        //更新WeatherBookmark的showOrder
        for (int i = 0; i < weatherInfoPages.size(); i++) {
            DatabaseUtil.getInstance().updateWeatherBookMarkShowOrder(weatherInfoPages.get(i).getBookmarkId(), i + 1);
        }

        pagerAdapter.notifyDataSetChanged();
        pageContainer.setCurrentItem(weatherInfoPages.size());
        refreshOnlyIfNeed();
    }

    /**
     * 根据当前数据库的内容初始化ViewPage，并设置最初页面标题
     */
    private void initViewPagers() {
        List<WeatherBookmark> bookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        for (WeatherBookmark bookmark : bookmarks) {
            WeatherInfoFragment fragment = new WeatherInfoFragment();
            fragment.setBookmarkId(bookmark.getId());
            weatherInfoPages.add(fragment);
        }

        pageContainer.setAdapter(pagerAdapter);
        pageContainer.addOnPageChangeListener(pageChangeListener);

        int currentItem = bookmarks.size() - 1;
        pageContainer.setCurrentItem(currentItem);
        countyName.setText(bookmarks.get(currentItem).getCounty().getName());
        showTime.setText("--" + (currentItem) + "--");
    }

    /**
     * 刷新所有数据
     */
    private void refreshAllPages() {
        List<Integer> updateList = new ArrayList<Integer>();
        for (WeatherInfoFragment wf : weatherInfoPages) {
            updateList.add(wf.getBookmarkId());
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
            return weatherInfoPages.get(position);
        }

        @Override
        public int getCount() {
            return weatherInfoPages.size();
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
            int bookmarkId = weatherInfoPages.get(position).getBookmarkId();
            String name = DatabaseUtil.getInstance().getCountyByBookmarkId(bookmarkId).getName();
            countyName.setText(name);
            showTime.setText("--" + position + "--");
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 刷新指定的bookmark数据，执行更新操作
     */
    private void requestWeatherDataAndRefresh(final List<Integer> updateIdList) {

        swipeRefresh.setRefreshing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int wbId : updateIdList) {
                    WeatherBookmark wb = DatabaseUtil.getInstance().getWeatherBookmarkById(wbId);
                    String weatherId = DatabaseUtil.getInstance().getCountyByBookmarkId(wbId).getWeatherId();
                    String requestUrl = URL + weatherId + "&key=" + KEY;
                    String json = HttpUtil.getResponse(requestUrl);
                    if (!TextUtils.isEmpty(json)) {
                        HefengWeather weather = JsonUtil.handleHefengJson(json);
                        wb.setWeatherData(json);
                        wb.setUpdateTime(weather.getUpdateTime());
                        wb.save();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDataToPages();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();


    }

    /**
     * 数据库内数据得到更新后，将更新刷新到界面上
     */
    private void showDataToPages() {
        for (WeatherInfoFragment wf : weatherInfoPages) {
            if (wf.isViewCreated()) wf.refreshView();
        }
    }

    /**
     * 获取需要拉取天气数据的bookmark,2小时之前的天气都需要更新
     *
     * @return
     */
    private List<Integer> getNeedUpdateBookmarkList() {
        List<WeatherBookmark> bookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        List<Integer> needupdate = new ArrayList<Integer>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Date now = new Date();
        for (WeatherBookmark wb : bookmarks) {
            //最近一次更新时间在2小时以前的都需要更新
            if (now.getTime() - wb.getUpdateTime().getTime() > 2 * 60 * 60 * 1000) {
                needupdate.add(wb.getId());
            }
        }
        return needupdate;
    }
}
