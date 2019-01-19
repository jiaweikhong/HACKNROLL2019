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
    private Spinner location;
    private Spinner min_star;
    private Spinner max_star;
    private Button gotocheckcurrentlocation;
    private Button getlocation;
    private Integer radius;

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
                radius = progress * 200 +200;
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

        location = findViewById(R.id.Location);
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
                String selLocation;
                int minstar = 1;
                int maxstar = 5;

                // Check if user has input a location
                selLocation = location.getSelectedItem().toString();
                if (selLocation.equals("Select Location")){
                    Toast.makeText(MainActivity.this, "Please select a location", Toast.LENGTH_LONG).show();
                    break;
                }

                // Check if user has input min/max prices/ratings.
//                if ((min_star.getSelectedItem().toString()).equals("Set Min")){
//                    minstar = 1;
//                }else{
//                    minstar = Integer.parseInt(min_star.getSelectedItem().toString());
//                }
//                if ((max_star.getSelectedItem().toString()).equals("Set Max")){
//                    maxstar = 5;
//                }else{
//                    maxstar = Integer.parseInt(max_star.getSelectedItem().toString());
//                }
                try{
                    minstar = Integer.parseInt(min_star.getSelectedItem().toString());
                    maxstar = Integer.parseInt(max_star.getSelectedItem().toString());
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Select all spinner", Toast.LENGTH_LONG).show();
                    break;
                }

                Log.i(Constants.TAG,"Min: " + minstar);
                Log.i(Constants.TAG,"Max: " + maxstar);

                // Check if min prices/ratings are less than max prices/ratings
                if (minstar >= maxstar) {
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
                selLocation = location.getSelectedItem().toString();
                if (selLocation.equals("Select Location")){
                    Toast.makeText(MainActivity.this, "Please select a location", Toast.LENGTH_LONG).show();
                    break;
                }
                fromMain.putExtra("Location",getResources().getIdentifier(selLocation, "string", getPackageName()));
                fromMain.putExtra("Radius", radius);
                //fromMain.putExtra("Radius")
                Log.i ( Constants.TAG,"Going to currentlocation" );
                startActivity(fromMain);
                break;
        }
    }
}
