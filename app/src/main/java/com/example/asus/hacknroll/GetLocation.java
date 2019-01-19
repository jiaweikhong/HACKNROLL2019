package com.example.asus.hacknroll;

import android.os.Bundle;
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


public class GetLocation extends AppCompatActivity implements OnMapReadyCallback{


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

    public static final String GOOGLE_BROWSER_API_KEY = "AIzaSyAYtGuKBzhpwXPyStjEskz7IQS6S3YSrPY";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int PROXIMITY_RADIUS = 500;



    public GoogleMap mMap;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ( )
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );
        loadNearByPlaces(1.299101, 103.845679);
    }

    @Override
    public void onMapReady ( GoogleMap googleMap ) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLngBounds Singapore = new LatLngBounds( new LatLng(1.214452, 103.603346), new LatLng(1.455876, 104.041428));
        mMap.moveCamera ( CameraUpdateFactory.newLatLngBounds ( Singapore, 2 ) );

    }



    private void loadNearByPlaces(double latitude, double longitude) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works

        String type = "food";
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,googlePlacesUrl.toString(),null,

                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        Log.i(TAG, "onResponse: Result= " + result.toString());
                        parseLocationResult(result);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Error= " + error);
                        Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    id = place.getString(SUPERMARKET_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)

                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)

                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);

                    MarkerOptions markerOptions = new MarkerOptions ();
                    LatLng latLng = new LatLng (latitude, longitude);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    Log.i(TAG, markerOptions.getTitle());

                    mMap.addMarker(markerOptions);

                }
                Log.i(TAG, jsonArray.getString(0));
                Log.i(TAG, jsonArray.length() + " Supermarkets found!");
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Log.i(TAG,"No Supermarket found in 5KM radius!!!");
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }
}
