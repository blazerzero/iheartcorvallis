/* WELCOME SCREEN W/ LOG IN/SIGN UP BUTTONS */

package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    SessionActivity session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // session information is retrieved and displayed on nav menu
        session = new SessionActivity(getApplicationContext());
        session.checkLogin();  // check this

        ImageView iv = (ImageView) findViewById(R.id.iv_ihc);
        TextView tv = (TextView) findViewById(R.id.tv_main_title);




        Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });

        Button signupButton = (Button) findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupPageActivity.class);
                startActivity(intent);
            }
        });

        overridePendingTransition(0,0);

        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        iv.startAnimation(fadeIn);
        tv.startAnimation(fadeIn);
        loginButton.startAnimation(fadeIn);
        signupButton.startAnimation(fadeIn);

        //spinner.setVisibility(View.GONE);
    }

    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.push_left,R.anim.push_right);
    }



}
