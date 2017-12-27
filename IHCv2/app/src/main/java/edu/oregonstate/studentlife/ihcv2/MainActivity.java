/* WELCOME SCREEN W/ LOG IN/SIGN UP BUTTONS */

package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);*/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button loginButton = (Button)findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });

        Button signupButton = (Button)findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupPageActivity.class);
                startActivity(intent);
            }
        });

        //spinner.setVisibility(View.GONE);
    }



}
