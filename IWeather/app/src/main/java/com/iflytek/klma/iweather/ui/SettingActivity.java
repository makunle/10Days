package com.iflytek.klma.iweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.WeatherBookmark;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "IWeather";

    private ListView bookmarkList;
    private List<String> countyNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bookmarkList = (ListView) findViewById(R.id.bookmark_list);

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.add).setOnClickListener(clickListener);

        List<WeatherBookmark> weatherBookmarks = DatabaseUtil.getInstance().getAllWeatherBookMark();
        for (WeatherBookmark wb : weatherBookmarks){
            countyNameList.add(wb.getCounty().getName());
        }
        bookmarkList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countyNameList));
        bookmarkList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("选择操作");
                menu.add(0, 0, 0, "删除");
                menu.add(0, 0, 0, "提醒");
            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String countyName = ((TextView)info.targetView).getText().toString();
        switch (item.getItemId()){
            case 0:   //删除
                DatabaseUtil.getInstance().deleteWeatherBookMarkByCountyName(countyName);
                for (int i = 0; i < countyNameList.size(); i++) {
                    if(countyName.equals(countyNameList.get(i))){
                        countyNameList.remove(i);
                        break;
                    }
                }
                bookmarkList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countyNameList));
                break;
            case 1:   //设置提醒
                break;
            default:
        }
        return super.onContextItemSelected(item);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.add:
                    startActivity(new Intent(SettingActivity.this, CountyChooseActivity.class));
                    break;
                default:
            }
        }
    };
}
