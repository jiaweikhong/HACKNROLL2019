package com.example.asus.hacknroll;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        new GetTask(this).execute();

        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.cuteloading);

    }
}

class GetTask extends AsyncTask<Object, Void, String> {

    Context context;

    GetTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... params) {
        // insert code to find the place
        try { Thread.sleep(3000); }
        catch (InterruptedException ex) { android.util.Log.d("YourApplicationName", ex.toString()); }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // go to the page
        Intent fromLoadScreen = new Intent(context, MainActivity.class);
        Log.i(Constants.TAG,"going to MainActivity");
        context.startActivity(fromLoadScreen);
    }
}
