package com.example.asus.hacknroll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.concurrent.ThreadLocalRandom;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response.ErrorListener;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class GetLocation extends AppCompatActivity implements OnMapReadyCallback {


    public static final String TAG = "gplaces";

    public static final String RESULTS = "results";
    public static final String STATUS = "status";

    public static final String OK = "OK";
    public static final String ZERO_RESULTS = "ZERO_RESULTS";
    public static final String REQUEST_DENIED = "REQUEST_DENIED";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    //    Key for nearby places json from google

    public static final String GEOMETRY = "geometry";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String ICON = "icon";
    public static final String SUPERMARKET_ID = "id";
    public static final String NAME = "name";
    public static final String PLACE_ID = "place_id";
    public static final String REFERENCE = "reference";
    public static final String VICINITY = "vicinity";
    public static final String PLACE_NAME = "place_name";

    // remember to change the browser api key

    public static final String GOOGLE_BROWSER_API_KEY = "AIzaSyCtT7nSyTOaTKv5IQSRZ6WkUiBwR69zvcw";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int PROXIMITY_RADIUS = 200;
    final StringBuilder original = new StringBuilder ( "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" );


    public GoogleMap mMap;
    public boolean nextpage = false;
    public String nextjson = null;

    public HashMap <Integer, MarkerOptions> a = new HashMap <Integer, MarkerOptions> ( );
    public HashMap <Integer, String> b = new HashMap <Integer, String> ( );

    public LatLng currentLocation;
    public Integer radius = 200;
    public int j = 0;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        j = 0;

        setContentView(R.layout.activity_loading_screen);


        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.cuteloading);

        //Intent intent = getIntent();
        String location = Constants.mPreferences.getString(Constants.selLocation,"DhobyGhaut");
        location = getString(getResources().getIdentifier(location, "string", getPackageName()));
        String[] locationsplit = location.split(",");
        currentLocation = new LatLng(Double.parseDouble(locationsplit[0]), Double.parseDouble(locationsplit[1]));
        Log.i(TAG, currentLocation.toString());
        radius = Constants.mPreferences.getInt(Constants.selRadius,200);

        //setContentView ( R.layout.activity_maps );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ( )
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );
        new GetTask(this).execute();
    }

    @Override
    public void onMapReady ( GoogleMap googleMap ) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLngBounds Singapore = new LatLngBounds( currentLocation, currentLocation);
        mMap.moveCamera ( CameraUpdateFactory.newLatLngZoom ( Singapore.getCenter(), 14 ) );
        /*
        for(int distance=200; distance<=radius; distance+=200 ) {
            loadNearByPlaces(currentLocation.latitude, currentLocation.longitude, distance);
        }
        */

    }

    class GetTask extends AsyncTask<Object, Void, String>

    {

        Context context;

        GetTask(Context context) {
        this.context = context;
    }

        @Override
        protected String doInBackground(Object... params) {
        // insert code to find the place
            Log.i(TAG, "this is the radius: " + radius);
            for(int distance=200; distance<=radius; distance+=200 ) {
                loadNearByPlaces(currentLocation.latitude, currentLocation.longitude, distance);
            }
            return a.toString();
    }

        @Override
        protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(TAG, result);
        // go to the page
        Intent fromLoadScreen = new Intent(context, StoreInfoActivity.class);
        Log.i(Constants.TAG,"going to store info page");
        context.startActivity(fromLoadScreen);
    }
    }

    private void loadNearByPlaces ( double latitude, double longitude, Integer radius ) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works

        String type = "restaurant";
        final StringBuilder googlePlacesUrl = new StringBuilder ( "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" );
        googlePlacesUrl.append ( "location=" ).append ( latitude ).append ( "," ).append ( longitude );
        googlePlacesUrl.append ( "&radius=" ).append ( radius );
        googlePlacesUrl.append ( "&types=" ).append ( type );
        googlePlacesUrl.append ( "&sensor=true" );
        googlePlacesUrl.append ( "&key=" + GOOGLE_BROWSER_API_KEY );

        JsonObjectRequest request = new JsonObjectRequest ( Request.Method.GET, googlePlacesUrl.toString ( ), null,

                new com.android.volley.Response.Listener <JSONObject> ( ) {
                    @Override
                    public void onResponse ( JSONObject result ) {
                        Log.i ( TAG, "onResponse: Result= " + result.toString ( ) );
                        parseLocationResult ( result );
                    }
                },
                new com.android.volley.Response.ErrorListener ( ) {
                    @Override
                    public void onErrorResponse ( VolleyError error ) {
                        Log.e ( TAG, "onErrorResponse: Error= " + error );
                        Log.e ( TAG, "onErrorResponse: Error= " + error.getMessage ( ) );
                    }
                } );

        AppController.getInstance ( ).addToRequestQueue ( request );
    }


    private void loadNearByPlaces ( String jsonurl ) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works


        JsonObjectRequest request = new JsonObjectRequest ( Request.Method.GET, jsonurl, null,

                new com.android.volley.Response.Listener <JSONObject> ( ) {
                    @Override
                    public void onResponse ( JSONObject result ) {
                        Log.i ( TAG, "Checking for result " + result.toString ( ) );
                        //parseLocationResult(result);
                        parsePlaceID ( result );
                    }
                },
                new com.android.volley.Response.ErrorListener ( ) {
                    @Override
                    public void onErrorResponse ( VolleyError error ) {
                        Log.e ( TAG, "onErrorResponse: Error= " + error );
                        Log.e ( TAG, "onErrorResponse: Error= " + error.getMessage ( ) );
                    }
                } );

        AppController.getInstance ( ).addToRequestQueue ( request );
    }

    private void parseLocationResult ( JSONObject result ) {


        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray ( "results" );
//            HashMap<Integer, MarkerOptions> a = new HashMap <Integer, MarkerOptions> ( jsonArray.length () );
//            if(result.has("next_page_token")) {
//                nextjson = result.getString("next_page_token");
//                Log.i(TAG, nextjson);
//            } else {
//                nextjson = null;
//            }

            if (result.getString ( STATUS ).equalsIgnoreCase ( OK )) {


                for (int i = 0; i < jsonArray.length ( ); i++) {
                    JSONObject place = jsonArray.getJSONObject ( i );

                    id = place.getString ( SUPERMARKET_ID );
                    place_id = place.getString ( PLACE_ID );
                    if (!place.isNull ( NAME )) {
                        placeName = place.getString ( NAME );
                    }
                    if (!place.isNull ( VICINITY )) {
                        vicinity = place.getString ( VICINITY );
                    }
                    latitude = place.getJSONObject ( GEOMETRY ).getJSONObject ( LOCATION )

                            .getDouble ( LATITUDE );
                    longitude = place.getJSONObject ( GEOMETRY ).getJSONObject ( LOCATION )

                            .getDouble ( LONGITUDE );
                    reference = place.getString ( REFERENCE );
                    icon = place.getString ( ICON );

                    MarkerOptions markerOptions = new MarkerOptions ( );
                    LatLng latLng = new LatLng ( latitude, longitude );
                    markerOptions.position ( latLng );
                    markerOptions.title ( placeName + " : " + vicinity );
                    Log.i ( TAG, markerOptions.getTitle ( ) );
                    mMap.clear ( );
                    a.put ( i, markerOptions );
                    b.put ( i, place_id );
                    //mMap.addMarker ( markerOptions );

                }
                int min = 0;
                int max = a.size ( ) - 1;
                Log.i ( TAG, "Size of hashmap: " + a.size ( ) );


                int k = (int) (Math.random ( ) * ((max - min) + 1)) + min;
                Log.i ( TAG, "Random Number: " + String.valueOf ( k ) );
                MarkerOptions m = a.get ( k );
                String placeid = b.get ( k );


                Log.i ( TAG, placeid );


                String detailsurl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeid+"&fields=name,rating,formatted_phone_number,formatted_address&key="+GOOGLE_BROWSER_API_KEY;
                loadNearByPlaces ( detailsurl );


                mMap.addMarker ( m );

                Log.i ( TAG, jsonArray.length ( ) + " Supermarkets found!" );
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Log.i(TAG,"No restaurants found in radius!");
            }

        } catch (JSONException e) {

            e.printStackTrace ( );
            Log.e ( TAG, "parseLocationResult: Error=" + e.getMessage ( ) );
        }
    }


    private void parsePlaceID ( JSONObject result ) {
        try {
            String pageName = result.getJSONObject ( "result" ).getString("name");
            String rating = result.getJSONObject ( "result" ).getString ( "rating" );
            String formattedAddress = result.getJSONObject ( "result" ).getString ( "formatted_address" );

            SharedPreferences.Editor editor =Constants.mPreferences.edit();
            editor.putString(Constants.pageName, pageName);
            editor.putString(Constants.rating, rating);
            editor.putString(Constants.formattedAddress, formattedAddress);
            editor.apply();

            Log.i ( Constants.TAG,pageName );
            Log.i(Constants.TAG,rating);
            Log.i(Constants.TAG,formattedAddress);
        } catch (JSONException e) {
            e.printStackTrace ( );
            Log.i(Constants.TAG,"Error");
        }
    }
}
