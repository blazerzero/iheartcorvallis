package edu.oregonstate.studentlife.ihcv2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.sql.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Created by Omeed on 12/20/17.
 */

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        try {
            Connection conn = DriverManager.getConnection("oniddb.cws.oregonstate.edu","habibelo-db","RcAbWdWDkpj7XNTL");
        }

        catch (SQLException e) {
            AlertDialog errorAlert = new AlertDialog.Builder(DashboardActivity.this).create();
            errorAlert.setTitle("Error " + e.getErrorCode());
            errorAlert.setMessage(e.getMessage());
            errorAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            errorAlert.show();
        }

        Button button1 = (Button)findViewById(R.id.eventbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });


        Button button2 = (Button)findViewById(R.id.passportbtn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PassportActivity.class);
                startActivity(intent);
            }
        });

        Button button3 = (Button)findViewById(R.id.leaderbtn);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        Button button4 = (Button)findViewById(R.id.resourcebtn);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ResourcesActivity.class);
                startActivity(intent);
            }
        });

        Button button5 = (Button)findViewById(R.id.aboutbtn);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }
}
