package com.example.asus.hacknroll;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView max_dist_text;
    private SeekBar max_dist;
    private ImageButton generate_store;
    private Spinner location;
    private Spinner min_star;
    private Spinner max_star;
    private Integer radius=200;
    private String selLocation;
    private int selLocationInt;
    private int minstar = 1;
    private int maxstar = 5;
    private int selProgress;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        Log.i(Constants.TAG,"On main page");
        Constants.mPreferences = getSharedPreferences(Constants.sharedPref,MODE_PRIVATE);

        // Code for adjusting Max Distance from selected Location
        max_dist_text = findViewById(R.id.max_dist_text);
        max_dist = findViewById(R.id.max_dist);
        selProgress = Constants.mPreferences.getInt(Constants.selProgress,0);
        radius = Constants.mPreferences.getInt(Constants.selRadius,200);
        max_dist.setProgress(selProgress);
        if (Constants.mPreferences.getInt(Constants.selProgress,0) < 4) {
            String setString = Integer.toString(Constants.mPreferences.getInt(Constants.selProgress,0)*200 + 200)+'m';
            max_dist_text.setText(setString);
        } else {
            max_dist_text.setText("1km");
        }

        max_dist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(Constants.TAG,"Progress changing");
                selProgress = progress;
                if (progress < 4) {
                    max_dist_text.setText(Integer.toString(progress*200 + 200)+'m');
                } else {
                    max_dist_text.setText("1km");
                }
                radius = (progress * 200) + 200;
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

        location.setSelection(Constants.mPreferences.getInt(Constants.selLocationInt,0));
        min_star.setSelection(Constants.mPreferences.getInt(Constants.selMinStar,0));
        max_star.setSelection(Constants.mPreferences.getInt(Constants.selMaxStar,0));

        // When button is pushed
        generate_store = findViewById(R.id.generate_store);
        generate_store.setOnClickListener(this);

    }

    public void onClick(View v){
        Intent fromMain = null;
        switch(v.getId()) {
            case R.id.generate_store:
                // Check if user has input a location
                selLocationInt = location.getSelectedItemPosition();
                if (selLocationInt == 0){
                    Toast.makeText(MainActivity.this, "Please select a location", Toast.LENGTH_LONG).show();
                    break;
                }
                selLocation = location.getSelectedItem().toString();
                selLocation = selLocation.replace(" ", "");
                selLocation = selLocation.replace("MRT", "");

                // Check if user has input min/max prices/ratings.
                if (min_star.getSelectedItemPosition() != 0){
                    minstar = Integer.parseInt(min_star.getSelectedItem().toString());
                }
                if (max_star.getSelectedItemPosition() != 0){
                    maxstar = Integer.parseInt(max_star.getSelectedItem().toString());
                }

                Log.i(Constants.TAG,"Min: " + minstar);
                Log.i(Constants.TAG,"Max: " + maxstar);

                // Check if min prices/ratings are less than max prices/ratings
                if (minstar >= maxstar) {
                    Toast.makeText(MainActivity.this, "Max rating has to be higher than min rating!", Toast.LENGTH_LONG).show();
                } else {
                    fromMain = new Intent(this, GetLocation.class);
                    fromMain.putExtra("Location",getString(getResources().getIdentifier(selLocation, "string", getPackageName())));
                    Log.i(Constants.TAG,"going to loading page");
                    startActivity(fromMain);
                    break;
                }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Constants.TAG,"Upload on pause");
        SharedPreferences.Editor editor =Constants.mPreferences.edit();
        editor.putInt(Constants.selMinStar, minstar);
        editor.putInt(Constants.selMaxStar, maxstar -1);
        editor.putInt(Constants.selLocationInt, selLocationInt);
        editor.putString(Constants.selLocation, String.valueOf(selLocation));
        editor.putInt(Constants.selProgress, selProgress);
        editor.putInt(Constants.selRadius, radius);

        editor.apply();
    }

}
