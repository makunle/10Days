package com.iflytek.klma.iweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class BookmarkSettingActivity extends AppCompatActivity {
    private static final String TAG = "IWeather";
    private static final int ITEM_DELETE = 0;
    private static final int ITEM_ALARM = 1;


    private ListView mBookmarkList;
    private List<String> mCountyNameList = new ArrayList<String>();
    private MyToolbar mToolBar;
    private ArrayAdapter<String> mBookmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_setting);

        mBookmarkList = (ListView) findViewById(R.id.bookmark_list);

        List<WeatherBookmark> weatherBookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        for (WeatherBookmark wb : weatherBookmarks) {
            mCountyNameList.add(wb.getCounty().getName());
        }

        mBookmarkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCountyNameList);
        mBookmarkList.setAdapter(mBookmarkAdapter);
        mBookmarkList.setFooterDividersEnabled(true);
        mBookmarkList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, ITEM_DELETE, 0, "删除");
                menu.add(0, ITEM_ALARM, 0, "提醒");
            }
        });

        mToolBar = (MyToolbar) findViewById(R.id.toolbar);
        mToolBar.getNormalRight().setOnClickListener(clickListener);
        mToolBar.getNormalLeft().setOnClickListener(clickListener);
    }

    /**
     * 城市列表长按菜单响应
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String countyName = ((TextView) info.targetView).getText().toString();
        switch (item.getItemId()) {
            case ITEM_DELETE:   //删除
                DatabaseUtil.getInstance().deleteWeatherBookMarkByCountyName(countyName);
                for (int i = 0; i < mCountyNameList.size(); i++) {
                    if (countyName.equals(mCountyNameList.get(i))) {
                        mCountyNameList.remove(i);
                        break;
                    }
                }
//                mBookmarkList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCountyNameList));
                mBookmarkAdapter.notifyDataSetChanged();
                break;
            case ITEM_ALARM:   //设置提醒
                WeatherBookmark bookmark = DatabaseUtil.getInstance().getWeatherBookMarkByCountyName(countyName);
                AlarmSettingActivity.startMe(BookmarkSettingActivity.this, bookmark.getId());
                break;
            default:
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 设置页面返回\添加按钮响应
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //返回
                case R.id.toolbar_normal_left:
                    if (DatabaseUtil.getInstance().getAllWeatherBookMark().size() <= 0) {
                        startActivity(new Intent(BookmarkSettingActivity.this, CountyChooseActivity.class));
                    }
                    finish();
                    break;

                //添加
                case R.id.toolbar_normal_right:
                    if (DatabaseUtil.getInstance().getAllWeatherBookMark().size() >= 10) {
                        Toast.makeText(BookmarkSettingActivity.this, "最多添加10个城市", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(BookmarkSettingActivity.this, CountyChooseActivity.class));
                    }
                    break;
                default:
            }
        }
    };


    @Override
    public void onBackPressed() {
        mToolBar.getNormalLeft().performClick();
    }
}
