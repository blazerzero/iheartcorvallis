/* WELCOME SCREEN W/ LOG IN/SIGN UP BUTTONS */

package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;

import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.oregonstate.studentlife.ihcv2.data.Session;

/**
 * This page is the first page that the user sees upon loading the app, it simply displays a login
 * button and a signup button to navigate the user to either page
 *
 */
public class MainActivity extends AppCompatActivity {

    Session session;
    private Button mLoginButton;
    private Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // session information is retrieved and displayed on nav menu
        session = new Session(getApplicationContext());
        session.checkLogin();  // check this

        ImageView iv = (ImageView) findViewById(R.id.iv_ihc);
        TextView tv = (TextView) findViewById(R.id.tv_main_title);

        Drawable image = iv.getDrawable();
        float[] negative = {
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        };
        image.setColorFilter(new ColorMatrixColorFilter(negative));

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NonStudentLoginActivity.class);
                startActivity(intent);
            }
        });

        mSignupButton = (Button) findViewById(R.id.btn_signup);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
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
        mLoginButton.startAnimation(fadeIn);
        mSignupButton.startAnimation(fadeIn);

        //spinner.setVisibility(View.GONE);
    }

    public void onPause() {
        super.onPause();
        //this.overridePendingTransition(R.anim.push_left,R.anim.push_right);
    }



}
