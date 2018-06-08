package edu.oregonstate.studentlife.ihcv2;

import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        iv = (ImageView) findViewById(R.id.iv_ihc);
        Drawable image = iv.getDrawable();
        float[] negative = {
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        };      // negative color filter
        image.setColorFilter(new ColorMatrixColorFilter(negative));     // invert the colors of the heart icon to make it white
        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        iv.startAnimation(fadeIn);      // start the fade in animation
        final Intent i = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run (){
                try {
                    sleep(3000);        // wait for 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);       // auto-move to MainActivity
                    finish();
                }
            }
        };
                timer.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.push_left,R.anim.push_right);
    }
}
