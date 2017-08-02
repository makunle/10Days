package com.iflytek.mkl.vociesdktest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.mkl.vociesdktest.Gson.GsonUtil;
import com.iflytek.mkl.vociesdktest.Gson.IflyWeather;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextUnderstander textUnderstander;
    private SpeechUnderstander speechUnderstander;

    EditText textOutput;
    EditText textInput;

    private Toast toast;

    private void showTip(String msg) {
        if (toast == null) toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOutput = (EditText) findViewById(R.id.et_output);
        textInput = (EditText) findViewById(R.id.et_input);

        textUnderstander = TextUnderstander.createTextUnderstander(this, initListener);
        speechUnderstander = SpeechUnderstander.createUnderstander(this, initListener);

        findViewById(R.id.btn_startlisten).setOnClickListener(btnListener);
        findViewById(R.id.btn_understand).setOnClickListener(btnListener);
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_understand:
                    doTextUnderstand();
                    break;
                case R.id.btn_startlisten:
                    doSpeechUnderstand();
                    break;
            }
        }
    };

    private void doTextUnderstand() {
        if (null == textUnderstander) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 " +
                    "createUtility 进行初始化");
            return;
        }

        String text = textInput.getText().toString();
        if (textUnderstander.isUnderstanding()) {
            textUnderstander.cancel();
        } else {
            int ret = textUnderstander.understandText(text, textUnderstanderListener);
            if (ret != 0) {
                showTip("语义理解失败:" + ret);
            }
        }
    }

    private void doSpeechUnderstand() {
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
    }

    private TextUnderstanderListener textUnderstanderListener = new TextUnderstanderListener() {
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            if (null != understanderResult) {
                String text = understanderResult.getResultString();
                if (!TextUtils.isEmpty(text)) {
                    handleUnderstanderResult(text);
                }
            } else {
                showTip("识别结果不正确");
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            showTip("error: " + speechError.getErrorCode());
        }
    };

    private SpeechUnderstanderListener speechUnderstanderListener = new com.iflytek.cloud.SpeechUnderstanderListener() {
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            if (null != understanderResult) {
                String text = understanderResult.getResultString();
                if (!TextUtils.isEmpty(text)) {
                    Log.d(TAG, text);
                    handleUnderstanderResult(text);
                }
            } else {
                showTip("结果识别不正确");
            }
        }

        @Override
        public void onVolumeChanged(int i, byte[] data) {
            showTip("说话音量 " + i);
        }

        @Override
        public void onBeginOfSpeech() {
            textOutput.append("\n开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            textOutput.append("\n结束说话");
        }

        @Override
        public void onError(SpeechError error) {
            if (error.getErrorCode() == ErrorCode.MSP_ERROR_NO_DATA) {
                showTip(error.getPlainDescription(true));
            } else {
                showTip(error.getPlainDescription(true) + ", ");
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    public static IflyWeather handleIflyWeatherJson(String json) {
        IflyWeather weather = new Gson().fromJson(json, IflyWeather.class);
        if (weather.getRc() != 0) return null;
        return weather;
    }

    private void handleUnderstanderResult(String text) {
        Log.d(TAG, "handleUnderstanderResult: " + (handleIflyWeatherJson(text) == null));
        IflyWeather weather = GsonUtil.handleIflyWeatherData(text);
        if (weather != null) {
            String outstr = weather.getAnswer().getText() + "\n\n" +
                    weather.getData().getResult().get(0).info();

            textOutput.setText(outstr);


            textInput.setText(weather.getText());
        }
    }

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败 error code: " + code);
            }
        }
    };


    public void setSpeechUnderstanderParams() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechUnderstander != null) {
            speechUnderstander.cancel();
            speechUnderstander.destroy();
        }
        if (textUnderstander != null) {
            if (textUnderstander.isUnderstanding())
                textUnderstander.cancel();
            textUnderstander.destroy();
        }
    }
}
