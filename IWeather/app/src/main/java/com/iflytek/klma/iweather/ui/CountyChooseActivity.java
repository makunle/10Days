package com.iflytek.klma.iweather.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.County;
import com.iflytek.klma.iweather.gson.Weather;
import com.iflytek.klma.iweather.util.AndroidUtil;
import com.iflytek.klma.iweather.util.DatabaseUtil;
import com.iflytek.klma.iweather.util.JsonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 天气选择界面，包括
 * 1、文字输入选择
 * 2、热门城市列表点选
 * 3、语音搜索
 */

public class CountyChooseActivity extends AppCompatActivity {

    private static final String TAG = "CountyChooseActivity";
    private static final int RECORD_AUDIO_CODE = 1;

    //    private EditText mCityInputEt;    //城市查询输入框
//    private Button mSearchBtn;        //城市搜索按钮
    private Button mSpeechSearchBtn;  //语音搜索按钮
    private RecyclerView mCountyListRv;   //城市列表
    private MyToolbar mToolbar;
    private ListView mSearchedListView;
    private TextView mNoResult;
    private LinearLayout mSearchBlock;

    //    private SpeechUnderstander mSpeechUnderstander;  //语音搜索理解器
    private TextUnderstander mTextUnderstander;       //文字理解
    private RecognizerDialog mRecognizerDialog;       //听写dialog

    private Toast toast;

    private List<String> mSearchResultList = new ArrayList<>();
    private ArrayAdapter<String> mSearchedAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_county_choose);
//        mCityInputEt = (EditText) findViewById(R.id.et_city_input);
//        mSearchBtn = (Button) findViewById(btn_search);
        mSpeechSearchBtn = (Button) findViewById(R.id.btn_speech_search);
        mCountyListRv = (RecyclerView) findViewById(R.id.rv_city_list);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        mSearchedListView = (ListView) findViewById(R.id.lv_search_result);
        mNoResult = (TextView) findViewById(R.id.no_result);
        mSearchBlock = (LinearLayout) findViewById(R.id.search_block);

        //热门城市列表，使用RecyclerView的瀑布布局方式展现
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        mCountyListRv.setLayoutManager(layoutManager);
        HotCountyAdapter adapter = new HotCountyAdapter(DatabaseUtil.getInstance().getHotCounties());
        mCountyListRv.setAdapter(adapter);

//        mSpeechUnderstander = SpeechUnderstander.createUnderstander(this, initListener);
        mTextUnderstander = TextUnderstander.createTextUnderstander(this, initListener);
        mRecognizerDialog = new RecognizerDialog(this, initListener);

        toast = Toast.makeText(CountyChooseActivity.this, "", Toast.LENGTH_SHORT);

//        mSearchBtn.setOnClickListener(searchOnClickListener);
//        mSpeechSearchBtn.setOnClickListener(searchOnClickListener);
        mSpeechSearchBtn.setVisibility(View.GONE);

        mToolbar.getInputEditText().addTextChangedListener(texInputWatcher);
        mToolbar.getSearchLeftButton().setOnClickListener(toolbarButtonClickListener);
        mToolbar.getSearchRightButton().setOnClickListener(toolbarButtonClickListener);

        mSearchedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSearchResultList);
        mSearchedListView.setAdapter(mSearchedAdapter);
        mSearchedListView.setOnItemClickListener(selectResultClickListener);
    }

    /**
     * 根据文字搜索结果选取事件，添加城市
     */
    private AdapterView.OnItemClickListener selectResultClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String countyName = mSearchedAdapter.getItem(position).split(" -")[0];
            DatabaseUtil.getInstance().addWeatherBookMark(countyName);
            WeatherShowActivity.startMe(CountyChooseActivity.this, countyName);
            CountyChooseActivity.this.finish();
        }
    };

    private View.OnClickListener toolbarButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toolbar_left_search:
                    finish();
                    break;
                case R.id.toolbar_right_search:
                    String tag = (String) mToolbar.getSearchRightButton().getTag();
                    if ("clear".equals(tag)) {//清除輸入框
                        mToolbar.getInputEditText().setText("");
                    } else {//语音搜索
                        if (AndroidUtil.checkAndGetPermission(CountyChooseActivity.this, Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_CODE)) {
                            recognizer();
                        }
                    }
                    break;
            }
        }
    };

    private void searchResultInfo(String info) {
        mSearchBlock.setVisibility(View.GONE);
        mNoResult.setVisibility(View.VISIBLE);
        mSearchedListView.setVisibility(View.GONE);
        mNoResult.setText(info);
    }

    /**
     * 搜索框文字改变事件响应
     */
    private TextWatcher texInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();

            //输入框为空时，显示语音搜索按钮，显示热门城市
            if (TextUtils.isEmpty(str)) {
                mToolbar.getSearchRightButton().setImageResource(R.drawable.toolbar_speak_dark);
                mToolbar.getSearchRightButton().setTag("speek");

                mSearchBlock.setVisibility(View.VISIBLE);
                mNoResult.setVisibility(View.GONE);
                mSearchedListView.setVisibility(View.GONE);
                return;
            } else {
                //输入框不为空时，显示清空按钮
                mToolbar.getSearchRightButton().setImageResource(R.drawable.toobar_close_dark);
                mToolbar.getSearchRightButton().setTag("clear");
                mSearchBlock.setVisibility(View.GONE);
                mNoResult.setVisibility(View.GONE);
                mSearchedListView.setVisibility(View.VISIBLE);
            }

            final List<County> counties = DatabaseUtil.getInstance().getAllCountyNameLike(str);
            if (counties != null && counties.size() > 0) {
                //搜索到城市时，显示列表
                mSearchedListView.setVisibility(View.VISIBLE);
                mNoResult.setVisibility(View.GONE);

                mSearchResultList.clear();
                String cityName, provinceName;
                String result = "";
                for (County county : counties) {
                    result = county.getName();
                    cityName = county.getCity().getName();
                    provinceName = county.getCity().getProvince().getName();
                    result += " - " + cityName;
                    result += " , " + provinceName;
                    mSearchResultList.add(result);
                }
                mSearchedAdapter.notifyDataSetChanged();
            } else {
                //无搜索结果时显示提示
                searchResultInfo("无搜索结果");
            }
        }
    };

    private void showTip(String msg) {
        toast.setText(msg);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mSpeechUnderstander != null) {
//            mSpeechUnderstander.cancel();
//            mSpeechUnderstander.destroy();
//        }
        if (mTextUnderstander != null) {
            mTextUnderstander.cancel();
            mTextUnderstander.destroy();
        }
    }


    private void recognizer() {
        if (null == mTextUnderstander) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        setParam();
        mRecognizerDialog.setListener(mRocognizeerDialogListener);
        mRecognizerDialog.show();
    }


    /**
     * 听写监听器。
     */
    private RecognizerDialogListener mRocognizeerDialogListener = new RecognizerDialogListener() {

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String resultText = JsonUtil.parseIatResult(recognizerResult.getResultString());
            if (!TextUtils.isEmpty(resultText)) {
                if (mTextUnderstander.isUnderstanding()) {
                    mTextUnderstander.cancel();
                } else {
                    if(DatabaseUtil.getInstance().isPlaceExist(resultText)){
                        resultText += "的天气";
                    }
                    int ret = mTextUnderstander.understandText(resultText, textUnderstanderListener);
                    if (ret != 0) {
                        showTip("语义理解失败");
                    }
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(TAG, "onError: textunderstand error: " + speechError.getErrorDescription());
        }
    };
    /**
     * 搜索按钮事件响应,首先获取权限
     */
//    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_speech_search:
//                    if (null == mSpeechUnderstander) {
//                        // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//                        showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 " +
//                                "createUtility 进行初始化");
//                        return;
//                    }
//                    if (AndroidUtil.checkAndGetPermission(CountyChooseActivity.this, Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_CODE)) {
//                        doSpeedUnderstand();
//                    }
//                    break;
//            }
//        }
//    };

//    private void doSpeedUnderstand() {
//        setSpeechUnderstanderParams();
//        if (mSpeechUnderstander.isUnderstanding()) {
//            mSpeechUnderstander.stopUnderstanding();
//            showTip("停止录音");
//        } else {
////                        mRecognizerDialog.setListener(mRecognizerDialogListener);
////                        mRecognizerDialog.show();
//            int ret = mSpeechUnderstander.startUnderstanding(speechUnderstanderListener);
////                        int ret = 1;
//            if (ret != 0) {
//                showTip("语义理解失败，错误码：" + ret);
//            } else {
//                showTip("请开始说话...");
//            }
//        }
//    }

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

    private TextUnderstanderListener textUnderstanderListener = new TextUnderstanderListener() {
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            if (null != understanderResult) {
                String res = understanderResult.getResultString();
                Log.d(TAG, "onResult: " + res);
                if (!TextUtils.isEmpty(res) && 0 == getResultError(res)) {
                    Weather weather = JsonUtil.handleIflyWeatherJson(res);
                    if (weather != null) {
                        if (!weather.isDataUsable()) {
                            searchResultInfo(weather.getInfo());
                            return;
                        }
                        searchResultInfo("正在查询" + weather.getCountyName() + "的天气...");
                        final String countyName = weather.getCountyName();
                        if (DatabaseUtil.getInstance().getCountyByName(countyName) == null) {
                            searchResultInfo("对不起，不支持当前城市");
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    DatabaseUtil.getInstance().addWeatherBookMark(countyName);
                                    WeatherShowActivity.startMe(CountyChooseActivity.this, countyName);
                                }
                            }).start();

                            return;
                        }
                    }
                }

            }
            searchResultInfo("只支持国内城市天气查询");
        }

        @Override
        public void onError(SpeechError speechError) {
            searchResultInfo("出错了");
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

        /**
         * 根据理解的语义添加城市
         * @param understanderResult
         */
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            Log.d(TAG, "语义理解失败\n" + understanderResult.getResultString());
            if (null != understanderResult) {
                String res = understanderResult.getResultString();
                Log.d(TAG, "onResult: " + res);
                if (!TextUtils.isEmpty(res) && 0 == getResultError(res)) {
                    Weather weather = JsonUtil.handleIflyWeatherJson(res);
                    if (weather != null) {
                        String countyName = weather.getCountyName();
                        if (DatabaseUtil.getInstance().getCountyByName(countyName) == null) {
                            showTip("不支持当前城市");
                        } else {
                            DatabaseUtil.getInstance().addWeatherBookMark(countyName);
                            WeatherShowActivity.startMe(CountyChooseActivity.this, countyName);
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
//    private void setSpeechUnderstanderParams() {
//        // 设置语言
//        mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        // 设置语言区域
//        mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, "mandarin");
//        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");
//        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1000");
//        // 设置标点符号，默认：1（有标点）
//        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, "0");
//
//        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageState() + "/msc/sud.wav");
//
//    }
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
        if (requestCode == RECORD_AUDIO_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                doSpeedUnderstand();
                recognizer();
            } else {
                showTip("未获得录音权限，不能执行语音搜索操作");
            }
        }
    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mRecognizerDialog.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mRecognizerDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mRecognizerDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");


        // 设置语言
        mRecognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mRecognizerDialog.setParameter(SpeechConstant.ACCENT, "mandarin");


        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mRecognizerDialog.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mRecognizerDialog.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mRecognizerDialog.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mRecognizerDialog.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mRecognizerDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }
}
