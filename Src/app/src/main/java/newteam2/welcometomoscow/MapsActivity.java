package newteam2.welcometomoscow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

interface Predicate<T> {
    boolean test(T value);
}

interface Action {
    void run();
}

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CurrentUserLocation userLocation;
    private LocationManager locationService;
    private Marker userMapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationService = (LocationManager) getSystemService(LOCATION_SERVICE);
        Predicate<String> permission = new Predicate<String>() {
            public boolean test(String permission_name) {
                return checkPermission(permission_name);
            }
        };
        Action location_changed = new Action() {
            @Override
            public void run() {
                UpdateLocationMarker();
            }
        };
        userLocation = new CurrentUserLocation(locationService, permission, location_changed);
    }


    @Override
    protected void onPause() {
        super.onPause();
        userLocation.Pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean enabled = locationService.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // ask to turn on GPS if its not enabled
        if (!enabled) {
            DialogFragment d = new DialogToEnableGPS();
            d.show(getSupportFragmentManager(), "gps_dialog");
        }
        userLocation.Resume();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // change map type
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // add current position marker
        LatLng user_loc = userLocation.getLatLng();
        MarkerOptions user_opts = new MarkerOptions()
                .position(user_loc)
                .title("You")
                .rotation(180.0f);
        userMapMarker = mMap.addMarker(user_opts);
        // add random marker
        LatLng TutorialsPoint = new LatLng(user_loc.latitude + 1.0, user_loc.longitude + 1.0);
        MarkerOptions speaker_opts = new MarkerOptions()
                .position(TutorialsPoint)
                .title("Super Sonic")
                .snippet("This stuff is cool");
        mMap.addMarker(speaker_opts);
        // Add a marker in Moscow and move the camera
        LatLng moscow_loc = new LatLng(user_loc.latitude - 1.0, user_loc.longitude - 1.0);
        MarkerOptions moscow_opts = new MarkerOptions()
                .position(moscow_loc)
                .title("Marker in Moscow");
        mMap.addMarker(moscow_opts);
        // shift camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_loc));
    }

    /**
     * Method to perform permission checks for location access.
     * @param perm_name Name of the permission. Either: ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION
     * @return True if enabled.
     */
    private boolean checkPermission(String perm_name) {
        if (BuildConfig.DEBUG) {
            //noinspection ConstantConditions
            if (perm_name != Manifest.permission.ACCESS_COARSE_LOCATION
                    && perm_name != Manifest.permission.ACCESS_FINE_LOCATION)
            {
                throw new AssertionError("This permission name should not be here");
            }
        }
        return ActivityCompat.checkSelfPermission(this, perm_name) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Calleback to track user location changes.
     */
    private void UpdateLocationMarker() {
        LatLng coords = userLocation.getLatLng();
        userMapMarker.setPosition(coords);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
    }
}

