package edu.oregonstate.studentlife.ihcv2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import edu.oregonstate.studentlife.ihcv2.data.Session;

/**
 * Created by Omeed on 12/20/17.
 */

public class LoginPageActivity extends AppCompatActivity {

    Session session;
    private ImageView iv;
    public final static String IHC_STATUS_KEY = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        getSupportActionBar().hide();

        // session information is retrieved and displayed on nav menu
        session = new Session(getApplicationContext());

        iv = (ImageView) findViewById(R.id.iv_ihc);
        Drawable image = iv.getDrawable();
        float[] negative = {
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        };
        image.setColorFilter(new ColorMatrixColorFilter(negative));

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
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(LoginPageActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
                else {
                    showNoInternetConnectionMsg();
                }
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void showNoInternetConnectionMsg() {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else {
            builder = new android.app.AlertDialog.Builder(this);
        }
        builder.setTitle("No Internet Connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close alert. User can try action again.
            }
        });
        builder.setMessage(getResources().getString(R.string.no_internet_connection_msg));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }
}
