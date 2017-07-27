package com.iflytek.mkl.testwebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WebView webView;

    Button forwardBtn;
    Button freshBtn;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        WebView webView = new WebViewBase(this);
        setContentView(webView);
//        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");


//        forwardBtn = (Button) findViewById(R.id.forward);
//        freshBtn = (Button) findViewById(R.id.refresh);
//        backBtn = (Button) findViewById(R.id.back);
//
//        forwardBtn.setOnClickListener(this);
//        freshBtn.setOnClickListener(this);
//        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forward:
                webView.goForward();
                break;
            case R.id.back:
                webView.goBack();
                break;
            case R.id.refresh:
                webView.reload();
                break;
            default:
        }
    }
}
