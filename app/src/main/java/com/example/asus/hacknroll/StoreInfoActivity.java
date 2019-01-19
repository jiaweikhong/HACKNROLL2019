package com.example.asus.hacknroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class StoreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton open_gmaps;
    private ImageButton randomize_again;
    private ImageButton return_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        open_gmaps = findViewById(R.id.open_gmaps);
        open_gmaps.setOnClickListener(this);
        randomize_again = findViewById(R.id.randomize_again);
        randomize_again.setOnClickListener(this);
        return_menu = findViewById(R.id.return_menu);
        return_menu.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent fromStoreInfo = null;
        switch(v.getId()) {
            case R.id.open_gmaps:
                // implement open google maps
                Log.i ( Constants.TAG,"Opening Google Maps" );
                break;

            case R.id.randomize_again:
                // implement randomize and refresh page. no intents required
                break;

            case R.id.return_menu:
                fromStoreInfo = new Intent(this, MainActivity.class);
                Log.i(Constants.TAG, "Returning to main menu");
                startActivity(fromStoreInfo);
        }
    }
}
