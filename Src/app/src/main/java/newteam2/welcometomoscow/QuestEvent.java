package newteam2.welcometomoscow;

/**
 * Created by artem on 10/30/16.
 */


import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class QuestEvent {

    @SerializedName("lat")
    @Expose
    double latitude;

    @SerializedName("long")
    @Expose
    double longitude;

    @SerializedName("desc")
    @Expose
    String text;

    LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    boolean isReady(PlayerData playerData) {
        float result[] = new float[1];
        Location.distanceBetween(playerData.latLng.latitude
                , playerData.latLng.longitude
                , latitude
                , longitude
                , result);
        // result has the distance in meters (if less than X meters, return true)
        //Log.i("iamnp", result[0] + " meters");
        return result[0] < dist;
    }

    @SerializedName("dist")
    @Expose
    int dist;
}