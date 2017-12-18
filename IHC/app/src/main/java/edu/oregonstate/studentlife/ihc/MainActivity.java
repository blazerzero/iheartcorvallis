package edu.oregonstate.studentlife.ihc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.webView);
        WebSettings websettings = webview.getSettings();
        websettings.setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/index.html");
        webview.setWebViewClient(new WebViewClient());
        webview.setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}
