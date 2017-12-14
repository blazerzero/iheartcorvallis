package edu.oregonstate.studentlife.ihc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view.loadUrl("file:///android_assets/index.html");
        setContentView(view);
    }
}
