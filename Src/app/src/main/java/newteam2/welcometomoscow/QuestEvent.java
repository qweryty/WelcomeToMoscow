package newteam2.welcometomoscow;

/**
 * Created by artem on 10/30/16.
 */


import com.google.android.gms.maps.model.LatLng;



public class QuestEvent {
    LatLng mapLatLng;
    /**
     * Text about the place and guide to the next place.
     */
    String text;
    /**
     * Call this to check if its time to execute the quest event.
     */
    QuestEventPredicate isReady;

    QuestEvent(String text, LatLng latlng, QuestEventPredicate checkReady) {
        this.text = text;
        this.mapLatLng = latlng;
        isReady = checkReady;
    }
}
