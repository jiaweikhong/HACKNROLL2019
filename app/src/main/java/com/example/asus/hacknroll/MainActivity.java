package com.example.asus.hacknroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.location.places.Place;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton generate_store;
    private Button gotocheckcurrentlocation;
    private Button getlocation;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        Log.i(Constants.TAG,"On main page");

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
                fromMain = new Intent(this, StoreInfoActivity.class);
                Log.i(Constants.TAG,"going to store info page");
                break;
            case R.id.gotochecklocation:
                fromMain = new Intent ( this,Picker.class );
                Log.i ( Constants.TAG,"Going to currentlocation" );
                break;
        }
        startActivity(fromMain);
    }
}
