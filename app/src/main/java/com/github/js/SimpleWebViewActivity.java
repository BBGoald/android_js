package com.github.js;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by zlove on 2018/1/28.
 */

public class SimpleWebViewActivity extends AppCompatActivity {

    private static final String TAG = SimpleWebViewActivity.class.getSimpleName();

    private WebView webView;
    private Button btnLoadUrl;
    private Button btnEvaluateJavascript;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webView);
        btnLoadUrl = (Button) findViewById(R.id.loadUrl);
        btnEvaluateJavascript = (Button) findViewById(R.id.evaluateJavascript);
        initWebView();
        setListener();
    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置与Js交互的权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗

        webView.loadUrl("file:///android_asset/javascript.html");
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(SimpleWebViewActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }

    private void setListener() {
        btnLoadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 此处的callJS方法名与JS中的function方法名必须要一致
                        webView.loadUrl("javascript:callJS()");
                    }
                });
            }
        });

        btnEvaluateJavascript.setOnClickListener(new View.OnClickListener() {

            @TargetApi(19)
            @Override
            public void onClick(final View v) {
                webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.d(TAG, "value---" + value);
                    }
                });
            }
        });
    }
}
