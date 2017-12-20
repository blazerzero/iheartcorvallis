package edu.oregonstate.studentlife.ihcv2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Omeed on 12/20/17.
 */

public class LoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

            Button searchButton = (Button)findViewById(R.id.btn_search);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginPageActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            });

            Button viewButton = (Button)findViewById(R.id.btn_view);
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginPageActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            });
    }
}
