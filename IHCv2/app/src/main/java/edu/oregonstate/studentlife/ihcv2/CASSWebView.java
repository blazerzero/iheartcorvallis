package edu.oregonstate.studentlife.ihcv2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by dylan on 4/10/2018.
 */

public class CASSWebView extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cass_web_view);

        mWebView = (WebView)findViewById(R.id.activity_cass_webview);

        // Enable Javascript
        //WebSettings webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("https://login.oregonstate.edu/idp/profile/cas/login?service=http://web.engr.oregonstate.edu/~habibelo/ihc_server/appscripts/studentsignup.php");
    }


}
