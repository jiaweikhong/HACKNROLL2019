package com.example.asus.hacknroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton generate_store;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        Log.i(Constants.TAG,"On main page");

        generate_store = findViewById(R.id.generate_store);
        generate_store.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent fromMain = null;
        switch(v.getId()) {
            case R.id.generate_store:
                fromMain = new Intent(this, StoreInfoActivity.class);
                Log.i(Constants.TAG,"going to store info page");
                break;

        }
        startActivity(fromMain);
    }
}
