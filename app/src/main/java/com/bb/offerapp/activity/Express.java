package com.bb.offerapp.activity;


import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bb.offerapp.R;
import com.bb.offerapp.util.BaseActivity;


public class Express extends BaseActivity {

    private WebView webView;

    private String url = "https://m.kuaidi100.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        initView();
    }
    private void initView() {
        webView = (WebView)findViewById(R.id.express);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
