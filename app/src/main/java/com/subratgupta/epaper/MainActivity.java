package com.subratgupta.epaper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    String url;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);

    }

    public void onClick(View view) {

        findViewById(R.id.list).setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.setWebViewClient(new );
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        webView.loadUrl("https://inducesmile.com/blog");

        switch (view.getId()){
            case R.id.amarujala:
                break;

            case R.id.dainikjagran:
                break;

            case R.id.haribhui:
                break;

            case R.id.dainikbhaskar:
                break;

            case R.id.hindustan:
                break;

            case R.id.jansatta:
                break;

            case R.id.loktej:
                break;

            case R.id.naidunia:
                break;

            case R.id.navbharattimes:
                break;

            case R.id.prabhatkhabar:
                break;

            case R.id.punjabkesari:
                break;

            case R.id.patrika:
                break;
        }
    }
}
