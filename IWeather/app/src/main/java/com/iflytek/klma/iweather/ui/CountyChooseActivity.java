package com.iflytek.klma.iweather.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.County;
import com.iflytek.klma.iweather.gson.IflyWeather;
import com.iflytek.klma.iweather.util.AndroidUtil;
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
    private static final int RECORD_AUDIO_CODE = 1;

    private EditText etCityInput;    //城市查询输入框
    private Button btnSearch;        //城市搜索按钮
    private Button btnSpeechSearch;  //语音搜索按钮
    private RecyclerView rvCountyList;   //城市列表

    private SpeechUnderstander speechUnderstander;  //语音搜索理解器
    private RecognizerDialog recognizerDialog;

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

        speechUnderstander = SpeechUnderstander.createUnderstander(this, initListener);
        recognizerDialog = new RecognizerDialog(this, initListener);

        toast = Toast.makeText(CountyChooseActivity.this, "", Toast.LENGTH_SHORT);

        btnSearch.setOnClickListener(searchOnClickListener);
        btnSpeechSearch.setOnClickListener(searchOnClickListener);
    }

    private void showTip(String msg) {
        toast.setText(msg);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechUnderstander != null) {
            speechUnderstander.cancel();
            speechUnderstander.destroy();
        }
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
                    if (null == speechUnderstander) {
                        // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                        showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 " +
                                "createUtility 进行初始化");
                        return;
                    }
                    if (AndroidUtil.checkAndGetPermission(CountyChooseActivity.this, Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_CODE)) {
                        doSpeedUnderstand();
                    }
                    break;
            }
        }
    };

    private void doSpeedUnderstand() {
        setSpeechUnderstanderParams();
        if (speechUnderstander.isUnderstanding()) {
            speechUnderstander.stopUnderstanding();
            showTip("停止录音");
        } else {
//                        recognizerDialog.setListener(mRecognizerDialogListener);
//                        recognizerDialog.show();
            int ret = speechUnderstander.startUnderstanding(speechUnderstanderListener);
//                        int ret = 1;
            if (ret != 0) {
                showTip("语义理解失败，错误码：" + ret);
            } else {
                showTip("请开始说话...");
            }
        }
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, "recognizeResult: " + results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Log.d(TAG, "onError: ");
        }

    };

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                showTip("语音模块初始化失败,错误码：" + i);
            }
        }
    };

    /**
     * 语音理解listener
     */
    private SpeechUnderstanderListener speechUnderstanderListener = new SpeechUnderstanderListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            showTip("音量" + i);
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onResult(UnderstanderResult understanderResult) {
            Log.d(TAG, "语义理解失败\n" + understanderResult.getResultString());
            if (null != understanderResult) {
                String res = understanderResult.getResultString();
                Log.d(TAG, "onResult: " + res);
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
        // 设置语言
        speechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        speechUnderstander.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        speechUnderstander.setParameter(SpeechConstant.VAD_BOS, "1000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        speechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号，默认：1（有标点）
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RECORD_AUDIO_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doSpeedUnderstand();
            }else{
                showTip("未获得录音权限，不能执行语音搜索操作");
            }
        }
    }
}
