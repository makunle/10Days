package com.iflytek.klma.iweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.County;
import com.iflytek.klma.iweather.gson.IflyWeather;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.JsonUtil;

import org.json.JSONObject;

import static com.iflytek.klma.iweather.R.id.btn_search;

/**
 * 天气选择界面，包括
 * 1、文字输入选择
 * 2、热门城市列表点选
 * 3、语音搜索
 */

public class CountyChooseActivity extends AppCompatActivity {

    private static final String TAG = "IWeather";


    private EditText etCityInput;    //城市查询输入框
    private Button btnSearch;        //城市搜索按钮
    private Button btnSpeechSearch;  //语音搜索按钮
    private RecyclerView rvCountyList;   //城市列表

    private SpeechUnderstander speechUnderstander;  //语音搜索理解器

    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.county_choose);
        etCityInput = (EditText) findViewById(R.id.et_city_input);
        btnSearch = (Button) findViewById(btn_search);
        btnSpeechSearch = (Button) findViewById(R.id.btn_speech_search);
        rvCountyList = (RecyclerView) findViewById(R.id.rv_city_list);

        //热门城市列表，使用RecyclerView的瀑布布局方式展现
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        rvCountyList.setLayoutManager(layoutManager);
        HotCountyAdapter adapter = new HotCountyAdapter(DatabaseUtil.getInstance().getHotCounties());
        rvCountyList.setAdapter(adapter);

        speechUnderstander = SpeechUnderstander.createUnderstander(this, new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    showTip("语音模块初始化失败,错误码：" + i);
                }
            }
        });

        toast = Toast.makeText(CountyChooseActivity.this, "", Toast.LENGTH_SHORT);

        btnSearch.setOnClickListener(searchOnClickListener);
        btnSpeechSearch.setOnClickListener(searchOnClickListener);
    }

    private void showTip(String msg) {
        toast.setText(msg);
        toast.show();
    }

    /**
     * 搜索按钮事件响应
     */
    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    String input = etCityInput.getText().toString();
                    if (!TextUtils.isEmpty(input)) {
                        County county = DatabaseUtil.getInstance().getCountyByName(input);
                        if (county != null) {
                            DatabaseUtil.getInstance().addWeatherBookMark(input);
                            startActivity(new Intent(CountyChooseActivity.this, WeatherShowActivity.class));
                            return;
                        }
                    }
                    Toast.makeText(CountyChooseActivity.this, "输入的城市名不正确", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_speech_search:
                    setSpeechUnderstanderParams();
                    if (speechUnderstander.isUnderstanding()) {
                        speechUnderstander.stopUnderstanding();
                        showTip("停止录音");
                    } else {
                        int ret = speechUnderstander.startUnderstanding(speechUnderstanderListener);
                        if (ret != 0) {
                            showTip("语义理解失败，错误码：" + ret);
                        } else {
                            showTip("请开始说话...");
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 语音理解listener
     */
    private SpeechUnderstanderListener speechUnderstanderListener = new SpeechUnderstanderListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            showTip("音量"+i);
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onResult(UnderstanderResult understanderResult) {Log.d(TAG, "语义理解失败\n"+understanderResult.getResultString());
            if (null != understanderResult) {
                String res = understanderResult.getResultString();
                Log.d(TAG, "onResult: "+res);
                if (!TextUtils.isEmpty(res) && 0 == getResultError(res)) {
                    IflyWeather weather = JsonUtil.handleIflyWeatherJson(res);
                    if (weather != null) {
                        String countyName = weather.getCountyName();
                        if (DatabaseUtil.getInstance().getCountyByName(countyName) == null) {
                            showTip("不支持当前城市");
                        } else {
                            DatabaseUtil.getInstance().addWeatherBookMark(countyName);
                            startActivity(new Intent(CountyChooseActivity.this, WeatherShowActivity.class));
                            return;
                        }
                    }
                }

            }
            showTip("语义理解失败");
        }

        @Override
        public void onError(SpeechError speechError) {
            showTip("发生错误啦");
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    /**
     * 语音模块参数配置
     */
    private void setSpeechUnderstanderParams() {
        speechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        speechUnderstander.setParameter(SpeechConstant.ACCENT, "mandarin");

        speechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");
        speechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1000");
        speechUnderstander.setParameter(SpeechConstant.ASR_PTT, "0");

        speechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        speechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageState() + "/msc/sud.wav");

    }

    private int getResultError(final String resultText) {
        int error = 0;
        try {
            final String KEY_ERROR = "error";
            final String KEY_CODE = "code";
            final JSONObject joResult = new JSONObject(resultText);
            final JSONObject joError = joResult.optJSONObject(KEY_ERROR);
            if (null != joError) {
                error = joError.optInt(KEY_CODE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }//end of try-catch

        return error;
    }
}
