package newteam2.welcometomoscow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private String currentQuestName;
    private GoogleMap mMap;
    private LocationManager locationService;
    private Marker userMapMarker;
    private Location currentLocation;
    private String providerName;
    private QuestInfo currentQuestInfo;
    private PlayerData playerData;
    private int EventCounter = 0;

    // minimum time interval between latLng updates, in milliseconds
    private static final int min_time_delay = 700;
    // minimum distance between latLng updates, in meters
    private static final int min_distance = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationService = (LocationManager) getSystemService(LOCATION_SERVICE);

        findBestProvider();

        // To avoid: http://www.developerphil.com/dont-store-data-in-the-application-object/
        // Get quest info, from choose quest activity
        MainApplication app = (MainApplication) getApplication();
        currentQuestInfo = app.getCurrentQuestInfo();
        if (currentQuestInfo == null) {
            // There is no QuestInfo, just go back to "choose quest menu"
            finish();
            return;
        }
        playerData = app.getCurrentPlayerData();
        if (playerData == null) {
            // There is no PlayerData, just go back to "choose player name" or something
            finish();
            return;
        }
        currentQuestName = currentQuestInfo.name;
    }


    @Override
    protected void onPause() {
        super.onPause();
        Pause();
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
        Resume();
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
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // add current position marker
        LatLng user_loc = getLatLng();
        MarkerOptions user_opts = new MarkerOptions()
                .position(user_loc)
                .title("You")
                .snippet(currentQuestName)
                .rotation(180.0f);
        userMapMarker = mMap.addMarker(user_opts);
        // shift camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_loc));
    }


    /**
     * Method to perform permission checks for latLng access.
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
     * Calleback to track user latLng changes.
     */
    private void UpdateLocationMarker() {
        playerData.latLng = getLatLng();
        userMapMarker.setPosition(playerData.latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(playerData.latLng));

        // check for next quest
        if (EventCounter < currentQuestInfo.events.size()) {
            final QuestEvent nextEv = currentQuestInfo.events.get(EventCounter);
            if (nextEv.isReady.test(playerData)) {
                // player is ready for the next event
                EventCounter++;

                // add marker on map
                LatLng qmLatLng = nextEv.mapLatLng;
                MarkerOptions questMark = new MarkerOptions()
                        .position(qmLatLng)
                        .title("QUEST")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                Marker m = mMap.addMarker(questMark);
                m.setTag(nextEv); // pass quest event as a marker Tag

                // move camera to center on new marker
                mMap.moveCamera(CameraUpdateFactory.newLatLng(qmLatLng));

                // set callback to start new activity
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Get ready to share info
                        MainApplication app = (MainApplication) getApplication();
                        // update global player data
                        app.setCurrentPlayerData(playerData);
                        app.setCurrentQuestEvent((QuestEvent) marker.getTag());
                        // start activity
                        Intent i = new Intent(MapsActivity.this, QuestEventActivity.class);
                        startActivity(i);
                        // Apply default maps behaviour to marker
                        return false;
                    }
                });
            }
        }
        else {
            // TODO: if there are no locations, show the ending screen
        }
    }

    /**
     * Return the last known/current latLng
     */
    public LatLng getLatLng() {
        if (currentLocation == null) {
            return new LatLng(0.0, 0.0);
        }
        else {
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }


    /**
     * Start querying for latLng updates. Call this in onResume of containing Activity
     */
    public void Resume() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                || checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            // request initial latLng
            if (currentLocation == null) {
                currentLocation = locationService.getLastKnownLocation(providerName);
            }
            // and sign up for updates
            locationService.requestLocationUpdates(providerName, min_time_delay, min_distance, (android.location.LocationListener) this);
        }
    }

    /**
     * Pause querying for latLng updates. Call this in onPause of Activity
     */
    public void Pause() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                || checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            locationService.removeUpdates(this);
        }
    }

    /**
     * Called when the latLng has changed.
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        UpdateLocationMarker();
    }

    /**
     * Called when provider status changes.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider == this.providerName) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
                // if the provider is out of service, and this is not expected to change in the near future
                // TODO: show some thing on screen to tell user that latLng is unavailiable
            }
        }
    }

    /**
     * Called when the provider is enabled by the user.
     */
    @Override
    public void onProviderEnabled(String provider) {
        findBestProvider();
    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called immediately.
     */
    @Override
    public void onProviderDisabled(String provider) {
        // try to find another provider
        findBestProvider();
    }

    /**
     * Tries to find the best provider for latLng.
     */
    private void findBestProvider() {
        // Define the criteria how to select the locatioin provider -> use default
        Criteria criteria = new Criteria();
        providerName = locationService.getBestProvider(criteria, false);
    }
}

