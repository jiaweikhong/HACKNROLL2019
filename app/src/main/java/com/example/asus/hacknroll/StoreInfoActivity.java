package com.example.asus.hacknroll;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StoreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton open_gmaps;
    private ImageButton randomize_again;
    private ImageButton return_menu;

    private TextView selected_location;
    private TextView rating_textview;
    private TextView selected_address;

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

        selected_location = findViewById(R.id.selected_location);
        rating_textview = findViewById(R.id.rating_textview);
        selected_address = findViewById(R.id.selected_address);

        selected_location.setText(Constants.mPreferences.getString(Constants.pageName,"Toast Box"));
        rating_textview.setText(Constants.mPreferences.getString(Constants.rating,"5"));
        selected_address.setText(Constants.mPreferences.getString(Constants.formattedAddress,"Bishan"));


    }

    public void onClick(View v){
        Intent fromStoreInfo = null;
        switch(v.getId()) {
            case R.id.open_gmaps:
                // implement open google maps
                //geo:latitude,longitude?z=zoom

                //Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=46.414382,10.013988"  );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                Log.i ( Constants.TAG,"Opening Google Maps" );

            case R.id.randomize_again:
                // implement randomize and refresh page. no intents required
                Intent redo = new Intent(this, GetLocation.class);
                startActivity(redo);
                break;

            case R.id.return_menu:
                fromStoreInfo = new Intent(this, MainActivity.class);
                Log.i(Constants.TAG, "Returning to main menu");
                startActivity(fromStoreInfo);
        }
    }
}
