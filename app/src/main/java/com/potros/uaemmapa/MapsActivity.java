package com.potros.uaemmapa;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private Marker marker;
    private double lat, lng;
    private LocationManager locationManager;
    private String provider;
    private boolean mapReady;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private Button salir;
    private StringRequest getRequest;
    private LatLngBounds edificioAbounds, edificioBbounds, edificioCbounds, edificioDbounds;
    private GroundOverlayOptions edificioAmap, edificioBmap, edificioCmap, edificioDmap;
    private boolean visibleA, visibleB, visibleC, visibleD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final RequestQueue queue = Volley.newRequestQueue(this);
        //salir = (Button) findViewById(R.id.salir);



        //Inicializar el manager que nos va a dar la geoposición en base al GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        /*salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desconectar("https://controlescolar.uaemex.mx/dce/sicde/publico/alumnos/site/logoff.jsp", queue);
                finish();
            }
        });*/

    }

    /*private void desconectar(String url, RequestQueue queue) {
        getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                // response
                new MapsActivity.NotifyCierre().execute((Void[])null);
                Log.d("Response", response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", error.toString());
            }
        });
        queue.add(getRequest);
    }*/
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near position, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        edificioAbounds = new LatLngBounds(
                new LatLng(19.297876, -98.955012),       // South west corner
                new LatLng(19.298146, -98.954491));      // North east corner*/
        edificioAmap = new GroundOverlayOptions()
                .transparency((float) 0.5)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.edificioa))
                .positionFromBounds(edificioAbounds)
                .visible(false);
        mMap.addGroundOverlay(edificioAmap);
        visibleA = false;

        edificioBbounds = new LatLngBounds(
                new LatLng(19.298265, -98.954936),       // South west corner
                new LatLng(19.298533, -98.954422));      // North east corner
        edificioBmap = new GroundOverlayOptions()
                .transparency((float) 0.5)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.edificiob))
                .positionFromBounds(edificioBbounds)
                .visible(false);
        mMap.addGroundOverlay(edificioBmap);
        visibleB = false;

        edificioCbounds = new LatLngBounds(
                new LatLng(19.298430, -98.956069),       // South west corner
                new LatLng(19.298705, -98.955554));      // North east corner
        edificioCmap = new GroundOverlayOptions()
                .transparency((float) 0.5)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.edificioc))
                .positionFromBounds(edificioCbounds)
                .visible(false);
        mMap.addGroundOverlay(edificioCmap);
        visibleC = false;

        edificioDbounds = new LatLngBounds(
                new LatLng(19.299157, -98.955951),       // South west corner
                new LatLng(19.299412, -98.955510));      // North east corner
        edificioDmap = new GroundOverlayOptions()
                .transparency((float) 0.5)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.edificiod))
                .positionFromBounds(edificioDbounds)
                .visible(false);
        mMap.addGroundOverlay(edificioDmap);
        visibleD = false;

        LatLng position = new LatLng(lat, lng);
        markerOptions = new MarkerOptions().position(position).title("Ubicación actual").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        marker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));

        mMap.setMinZoomPreference(18); // Set a preference for minimum zoom (Zoom out).
        mMap.setMaxZoomPreference(21); // Set a preference for maximum zoom (Zoom In).
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider,0/*milisegundos de update*/,  0/*metros de recorrido del usuario*/, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        //double alt = location.getAltitude();
        if (mapReady) {
            // Add a marker in position and move the camera
            LatLng position = new LatLng(lat, lng);
            marker.setPosition(position);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

            if ((lng >= edificioAbounds.southwest.longitude && lng <= edificioAbounds.northeast.longitude)
                    && (lat >= edificioAbounds.southwest.latitude && lat <= edificioAbounds.northeast.latitude)){
                edificioAmap.visible(true);
            }
            else {
                edificioAmap.visible(false);
            }

            if (edificioAmap.isVisible() != visibleA) {
                visibleA = edificioAmap.isVisible();
                mMap.clear();
                mMap.addGroundOverlay(edificioAmap);
                marker = mMap.addMarker(markerOptions);
            }

            if ((lng >= edificioBbounds.southwest.longitude && lng <= edificioBbounds.northeast.longitude)
                    && (lat >= edificioBbounds.southwest.latitude && lat <= edificioBbounds.northeast.latitude)){
                edificioBmap.visible(true);
            }
            else {
                edificioBmap.visible(false);
            }

            if (edificioBmap.isVisible() != visibleB) {
                visibleB = edificioBmap.isVisible();
                mMap.clear();
                mMap.addGroundOverlay(edificioBmap);
                marker = mMap.addMarker(markerOptions);
            }

            if ((lng >= edificioCbounds.southwest.longitude && lng <= edificioCbounds.northeast.longitude)
                    && (lat >= edificioCbounds.southwest.latitude && lat <= edificioCbounds.northeast.latitude)){
                edificioCmap.visible(true);
            }
            else {
                edificioCmap.visible(false);
            }

            if (edificioCmap.isVisible() != visibleC) {
                visibleC = edificioCmap.isVisible();
                mMap.clear();
                mMap.addGroundOverlay(edificioCmap);
                marker = mMap.addMarker(markerOptions);
            }

            if ((lng >= edificioDbounds.southwest.longitude && lng <= edificioDbounds.northeast.longitude)
               && (lat >= edificioDbounds.southwest.latitude && lat <= edificioDbounds.northeast.latitude)){
                edificioDmap.visible(true);
            }
            else {
                edificioDmap.visible(false);
            }

            if (edificioDmap.isVisible() != visibleD) {
                visibleD = edificioDmap.isVisible();
                mMap.clear();
                mMap.addGroundOverlay(edificioDmap);
                marker = mMap.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private class NotifyCierre extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... param) {
            //Do some work
            return null;
        }

        protected void onPostExecute(Void param) {
            //Print Toast or open dialog
            Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        }


    }
}
