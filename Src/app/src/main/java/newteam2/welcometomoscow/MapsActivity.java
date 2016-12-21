package newteam2.welcometomoscow;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
    private boolean cameraIsBusy = false;
    private FloatingActionButton achievementsButton;
    private FloatingActionButton questButton;
    private DrawerLayout drawerLayout;
    private View questsListDrawer;
    private View achievementsListDrawer;
    private QuestsListFragment questsListFragment;
    private boolean askedForPermissions;
    private static final int MY_PERMISSIONS_REQUEST_GPS = 99;
    private boolean GPSPermsAllowed;

    // minimum time interval between latLng updates, in milliseconds
    private static final int min_time_delay = 700;
    // minimum distance between latLng updates, in meters
    private static final int min_distance = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        playerData = new PlayerData();
        MainApplication app = (MainApplication) getApplication();
        app.setCurrentPlayerData(playerData);

        questsListFragment = (QuestsListFragment) getFragmentManager().findFragmentById(R.id.quests_list_fragment);

        locationService = (LocationManager) getSystemService(LOCATION_SERVICE);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        achievementsListDrawer = findViewById(R.id.achievments_list_drawer);
        questsListDrawer = findViewById(R.id.quests_list_drawer);
        questButton = (FloatingActionButton)findViewById(R.id.quests_button);
        questButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(questsListDrawer);
            }
        });
        achievementsButton = (FloatingActionButton)findViewById(R.id.achievements_button);
        achievementsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(achievementsListDrawer);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        PauseGPSTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // ask to turn on GPS if its not enabled
        if (!locationService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            DialogFragment d = new DialogToEnableGPS();
            d.show(getSupportFragmentManager(), "gps_dialog");
            return;
        }

        if (!askedForPermissions) {
            askedForPermissions = true;
            if (needToRequestGPSPermissions()) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GPS);
                return;
            } else {
                GPSPermsAllowed = true;
            }
        }

        ResumeGPSTracking();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            || (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                        GPSPermsAllowed = true;
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                break;
        }
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
                .snippet(currentQuestName);
        userMapMarker = mMap.addMarker(user_opts);

        // set up camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(user_loc)      // Sets the center of the map
                .zoom(17)              // Sets the zoom
                .bearing(0)           // Sets the orientation of the camera
                .tilt(20)              // Sets the tilt of the camera, gives 3D effect
                .build();              // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Set Idle listener. Now we know when camera is busy.
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                cameraIsBusy = false;
            }
        });
        // Set Busy listener.Now we know when camera is busy.
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                cameraIsBusy = true;
            }
        });

        // set listener, to respond to mark clicks.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTag() instanceof QuestEvent) {
                    // when its a quest marker, start new activity with photos/story
                    MainApplication app = (MainApplication) getApplication();
                    app.setCurrentQuestEvent((QuestEvent) marker.getTag());
                    Intent i = new Intent(MapsActivity.this, QuestEventActivity.class);
                    startActivity(i);
                }
                // Apply default maps behaviour to marker
                return false;
            }
        });
    }


    private boolean needToRequestGPSPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED;
    }

    /**
     * Move camera to some position.
     */
    public void animateCameraTo(final LatLng target)
    {
        CameraPosition camPosition = mMap.getCameraPosition();
        // To avoid: http://stackoverflow.com/questions/14816475/android-google-maps-v2-camera-animation
        // Check if this positions are the same.
        if (!((Math.floor(camPosition.target.latitude * 100) / 100) == (Math.floor(target.latitude * 100) / 100) && (Math.floor(camPosition.target.longitude * 100) / 100) == (Math.floor(target.longitude * 100) / 100)))
        {
            // temporarily pause scroll gestures
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.animateCamera( CameraUpdateFactory.newLatLng(target)
                    , new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish()
                {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                }

                @Override
                public void onCancel()
                {
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                }
            });
        }
    }

    /**
     * Play animation to fade the marker into the map.
     * @param m Marker to fade.
     * @param time_millisec Time in milliseconds for the animation.
     */
    ObjectAnimator MakeMarkerFadeAnimation(Marker m, int time_millisec) {
        // Set up marker animation
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(m, "Alpha", 0f, 1f);
        fadeAnim.setDuration(time_millisec);
        return fadeAnim;
    }

    /**
     * Calleback to track user latLng changes.
     */
    private void UpdateLocationMarker() {
        playerData.latLng = getLatLng();
        // if there is no userMarker than we haven't selceted any questa yet
        if (userMapMarker == null) {
            return;
        }
        userMapMarker.setPosition(playerData.latLng);
        // only move the camera to player position, when its free
        if (! cameraIsBusy) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(playerData.latLng));
        }

        // check for next quest
        if (EventCounter < currentQuestInfo.events.size()) {
            final QuestEvent nextEv = currentQuestInfo.events.get(EventCounter);
            if (nextEv.isReady(playerData)) {
                // player is ready for the next event
                EventCounter++;

                // add marker on map
                LatLng qmLatLng = nextEv.getLatLng();
                MarkerOptions questMark = new MarkerOptions()
                        .position(qmLatLng)
                        .title("QUEST")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_quest_event_map_click));
                Marker m = mMap.addMarker(questMark);

                // pass quest event as a marker Tag
                m.setTag(nextEv);

                ObjectAnimator markerFadeInto = MakeMarkerFadeAnimation(m, 400);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(markerFadeInto).after(50);
                animatorSet.start();

                // if camera is free also add camera animation
                if (! cameraIsBusy) {
                    animateCameraTo(qmLatLng);
                }
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
    @SuppressWarnings("MissingPermission")
    public void ResumeGPSTracking() {
        if (GPSPermsAllowed)
        {
            findBestProvider();
            // request initial latLng
            if (currentLocation == null) {
                currentLocation = locationService.getLastKnownLocation(providerName);
            }
            // and sign up for updates
            locationService.requestLocationUpdates(providerName, min_time_delay, min_distance, this);
        }
    }

    /**
     * Pause querying for latLng updates. Call this in onPause of Activity
     */
    @SuppressWarnings("MissingPermission")
    public void PauseGPSTracking() {
        if (GPSPermsAllowed)
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

    public void ButtonClickViewMap(View view) {
        questsListFragment.ButtonClickViewMap(view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // To avoid: http://www.developerphil.com/dont-store-data-in-the-application-object/
        // Get quest info, from choose quest activity
        MainApplication app = (MainApplication) getApplication();
        currentQuestInfo = app.getCurrentQuestInfo();
        if (currentQuestInfo == null) {
            // There is no QuestInfo, just go back to "choose quest menu"
            return;
        }
        currentQuestName = currentQuestInfo.name;
        drawerLayout.closeDrawer(questsListDrawer);
    }
}

