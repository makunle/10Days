package com.iflytek.mkl.vociesdktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;

public class MainActivity extends AppCompatActivity {

    private TextUnderstander textUnderstander;

    EditText textOutput;
    EditText textInput;
    Button understandBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOutput = (EditText) findViewById(R.id.et_output);
        textInput = (EditText) findViewById(R.id.et_input);
        understandBtn = (Button) findViewById(R.id.btn_understand);

        textUnderstander = TextUnderstander.createTextUnderstander(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        understandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( null == textUnderstander ){
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    Toast.makeText(MainActivity.this, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String text = textInput.getText().toString();
                if (textUnderstander.isUnderstanding()) {
                    textUnderstander.cancel();
                } else {
                    int ret = textUnderstander.understandText(text, new MyTextUnderstanderListener());
                    if (ret != 0) {
                        Toast.makeText(MainActivity.this, "语义理解失败:" + ret, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class MyTextUnderstanderListener implements TextUnderstanderListener {
        @Override
        public void onResult(UnderstanderResult understanderResult) {
            if (null != understanderResult) {
                String res = understanderResult.getResultString();
                if (!TextUtils.isEmpty(res)) {
                    textOutput.setText(res);
                }
            } else {
                Toast.makeText(MainActivity.this, "识别结果不正确", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(MainActivity.this, "error: " + speechError.getErrorCode(), Toast.LENGTH_SHORT).show();
        }
    }
}
