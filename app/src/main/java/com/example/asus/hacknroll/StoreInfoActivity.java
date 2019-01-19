package com.example.asus.hacknroll;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StoreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton open_gmaps;
    private ImageButton randomize_again;
    private ImageButton return_menu;

    private TextView selected_location;
    private TextView rating_textview;
    private TextView selected_address;
    private ImageView store_icon;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        Log.i(Constants.TAG,"in store info");

        open_gmaps = findViewById(R.id.open_gmaps);
        open_gmaps.setOnClickListener(this);
        randomize_again = findViewById(R.id.randomize_again);
        randomize_again.setOnClickListener(this);
        return_menu = findViewById(R.id.return_menu);
        return_menu.setOnClickListener(this);

        selected_location = findViewById(R.id.selected_location);
        rating_textview = findViewById(R.id.rating_textview);
        selected_address = findViewById(R.id.selected_address);
        store_icon = findViewById(R.id.store_icon);

        selected_location.setText(Constants.mPreferences.getString(Constants.pageName,"Toast Box"));
        rating_textview.setText(Constants.mPreferences.getString(Constants.rating,"5"));
        selected_address.setText(Constants.mPreferences.getString(Constants.formattedAddress,"Bishan"));

        Log.i(Constants.TAG,"start trying to convert url");
        try{
            String string_url = Constants.mPreferences.getString(Constants.icon,"");
            Log.i(Constants.TAG,"successfully loaded url");
            URL url = new URL(string_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.i(Constants.TAG,"succesfully http");
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            Log.i(Constants.TAG,"succesfully loaded in");
            bitmap = BitmapFactory.decodeStream(in);
            Log.i(Constants.TAG,"bitmap decoded");
            store_icon.setImageBitmap(bitmap);
            Log.i(Constants.TAG,"placed icon");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(Constants.TAG,"Malformed Error");
            store_icon.setImageResource(R.drawable.toastbox);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(Constants.TAG,"IOE");
            store_icon.setImageResource(R.drawable.toastbox);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(Constants.TAG,"Unknown Error");
            store_icon.setImageResource(R.drawable.toastbox);
        }


    }

    public void onClick(View v){
        Intent fromStoreInfo = null;
        switch(v.getId()) {
            case R.id.open_gmaps:
                // implement open google maps
                String name = Constants.mPreferences.getString(Constants.pageName,"Toast Box");
                String address = Constants.mPreferences.getString(Constants.formattedAddress,"Bishan");
                Log.i(Constants.TAG, address);
                Log.i ( Constants.TAG,"open maps clicked" );
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + name + " " + address );
                Log.i ( Constants.TAG,"url loaded" );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                Log.i ( Constants.TAG,"Before opening gmaps" );
                startActivity(mapIntent);
                break;

            case R.id.randomize_again:
                // implement randomize and refresh page. no intents required
                Intent redo = new Intent(this, GetLocation.class);
                startActivity(redo);
                break;

            case R.id.return_menu:
                fromStoreInfo = new Intent(this, MainActivity.class);
                Log.i(Constants.TAG, "Returning to main menu");
                startActivity(fromStoreInfo);
                break;
        }
    }
}
