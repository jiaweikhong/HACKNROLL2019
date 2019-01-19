package com.example.asus.hacknroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView max_dist_text;
    private SeekBar max_dist;
    private ImageButton generate_store;
    private Spinner min_price;
    private Spinner max_price;
    private Spinner min_star;
    private Spinner max_star;
    private Button gotocheckcurrentlocation;
    private Button getlocation;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        Log.i(Constants.TAG,"On main page");

        // Code for adjusting Max Distance from selected Location
        max_dist_text = findViewById(R.id.max_dist_text);
        max_dist = findViewById(R.id.max_dist);
        max_dist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(Constants.TAG,"Progress changing");
                if (progress < 4) {
                    max_dist_text.setText(Integer.toString(progress*200 + 200)+'m');
                } else {
                    max_dist_text.setText("1km");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(Constants.TAG,"Change started");
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(Constants.TAG,"Change ended");
            }
        });

        min_price = findViewById(R.id.min_price);
        max_price = findViewById(R.id.max_price);
        min_star = findViewById(R.id.min_star);
        max_star = findViewById(R.id.max_star);

        // When button is pushed
        generate_store = findViewById(R.id.generate_store);
        generate_store.setOnClickListener(this);
        gotocheckcurrentlocation = findViewById ( R.id.gotochecklocation );
        gotocheckcurrentlocation.setOnClickListener ( this );
        getlocation = findViewById ( R.id.getlocation );
        getlocation.setOnClickListener ( this );
    }

    public void onClick(View v){
        Intent fromMain = null;
        switch(v.getId()) {
            case R.id.generate_store:
                int minprice;
                int maxprice;
                int minstar;
                int maxstar;
                // Check if user has input min/max prices/ratings.
                try {
                    minprice = Integer.parseInt(min_price.getSelectedItem().toString());
                    maxprice = Integer.parseInt(max_price.getSelectedItem().toString());
                    minstar = Integer.parseInt(min_star.getSelectedItem().toString());
                    maxstar = Integer.parseInt(max_star.getSelectedItem().toString());
                } catch(Exception ex) {
                    Toast.makeText(MainActivity.this, "Please fill in all dropdowns!", Toast.LENGTH_LONG).show();
                    break;
                }

                // Check if min prices/ratings are less than max prices/ratings
                if (minprice >= maxprice) {
                    Toast.makeText(MainActivity.this, "Max price has to be higher than min price!", Toast.LENGTH_LONG).show();
                } else if (minstar >= maxstar) {
                    Toast.makeText(MainActivity.this, "Max rating has to be higher than min rating!", Toast.LENGTH_LONG).show();
                } else {
                    fromMain = new Intent(this, StoreInfoActivity.class);
                    Log.i(Constants.TAG,"going to store info page");
                    startActivity(fromMain);
                    break;
                }

            case R.id.gotochecklocation:
                Log.i(Constants.TAG, "Button Pressed");
                fromMain = new Intent(this, GetLocation.class);
                GetLocation.loadNearByPlaces(1.299101, 103.845679);
                Log.i ( Constants.TAG,"Going to currentlocation" );
                startActivity(fromMain);
                break;
        }
    }
}
