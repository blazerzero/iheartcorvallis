package cs496.yelpsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Button foodButton = (Button)findViewById(R.id.button_food);
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String food = "food";
                Intent intent = new Intent(CategoryActivity.this, ViewLocalActivity.class);
                intent.putExtra("term", food);
                startActivity(intent);
            }
        });

        Button gasButton = (Button)findViewById(R.id.button_gas);
        gasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gas = "gas";
                Intent intent = new Intent(CategoryActivity.this, ViewLocalActivity.class);
                intent.putExtra("term", gas);
                startActivity(intent);
            }
        });

        Button groceryButton = (Button)findViewById(R.id.button_grocery);
        groceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grocery = "grocery stores";
                Intent intent = new Intent(CategoryActivity.this, ViewLocalActivity.class);
                intent.putExtra("term", grocery);
                startActivity(intent);
            }
        });

        Button barsButton = (Button)findViewById(R.id.button_bars);
        barsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bars = "bars";
                Intent intent = new Intent(CategoryActivity.this, ViewLocalActivity.class);
                intent.putExtra("term", bars);
                startActivity(intent);
            }
        });
    }
}
