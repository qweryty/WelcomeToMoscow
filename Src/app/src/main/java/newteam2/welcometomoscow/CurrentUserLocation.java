package newteam2.welcometomoscow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;

/**
 * Created by artem on 10/17/16.
 */

class CurrentUserLocation implements LocationListener {
    private static final String TAG = CurrentUserLocation.class.getSimpleName();

    private LocationManager locationManager;
    private Location currentLocation;
    private String providerName;
    private Predicate<String> checkPermissionFunc;
    private Action updateMapMarker;

    // minimum time interval between location updates, in milliseconds
    private static final int min_time_delay = 700;
    // minimum distance between location updates, in meters
    private static final int min_distance = 2;

    CurrentUserLocation(LocationManager loc_service
            , Predicate<String> check_permission
            , Action update_location)
    {
        // Get the location manager
        locationManager = loc_service;
        checkPermissionFunc = check_permission;
        updateMapMarker = update_location;
        findBestProvider();
        Log.d(TAG,"constructor has run ok");
    }

    /**
     * Return the last known/current location
     */
    public LatLng getLatLng() {
        if (currentLocation == null) {
            // close to moscow, but obviously not moscow
            return new LatLng(50.0, 30.0);
        }
        else {
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }


    /**
     * Start querying for location updates. Call this in onResume of containing Activity
     */
    @SuppressWarnings("MissingPermission")   // The permissions are checked, but android studio can not track this
    public void Resume() {
        if (checkPermissionFunc.test(Manifest.permission.ACCESS_FINE_LOCATION)
                || checkPermissionFunc.test(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            // request initial location
            if (currentLocation == null) {
                currentLocation = locationManager.getLastKnownLocation(providerName);
            }
            // and sign up for updates
            locationManager.requestLocationUpdates(providerName, min_time_delay, min_distance, this);
        }
    }

    /**
     * Pause querying for location updates. Call this in onPause of Activity
     */
    @SuppressWarnings("MissingPermission")   // The permissions are checked, but android studio can not track this
    public void Pause() {
        if (checkPermissionFunc.test(Manifest.permission.ACCESS_FINE_LOCATION)
                || checkPermissionFunc.test(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            locationManager.removeUpdates(this);
        }
    }

    /**
     * Called when the location has changed.
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "GOT NEW LOCATION " + location);
        currentLocation = location;
        updateMapMarker.run();
    }

    /**
     * Called when provider status changes.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider == this.providerName) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
                // if the provider is out of service, and this is not expected to change in the near future
                // TODO: show some thing on screen to tell user that location is unavailiable
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
     * Tries to find the best provider for location.
     */
    private void findBestProvider() {
        // Define the criteria how to select the locatioin provider -> use default
        Criteria criteria = new Criteria();
        providerName = locationManager.getBestProvider(criteria, false);
    }

}
