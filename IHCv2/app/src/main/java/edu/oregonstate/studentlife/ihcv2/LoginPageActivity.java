package edu.oregonstate.studentlife.ihcv2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * Created by Omeed on 12/20/17.
 */

public class LoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        /*myToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        Button studentButton = (Button)findViewById(R.id.btn_student);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        Button nonStudentButton = (Button)findViewById(R.id.btn_non_student);
        nonStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, NonStudentLoginActivity.class);
                startActivity(intent);
            }
        });

        overridePendingTransition(0,0);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        studentButton.startAnimation(myanim);
        nonStudentButton.startAnimation(myanim);
    }

    public void onPause() {
        super.onPause();
        this.overridePendingTransition(0,0);
    }
}
