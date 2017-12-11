package edu.oregonstate.studentlife.ihc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    Button b1;
    EditText ed1;

    private WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button)findViewById(R.id.button);

        view.loadUrl("file:///android_asset/index.html");
        setContentView(view);
    }
}
