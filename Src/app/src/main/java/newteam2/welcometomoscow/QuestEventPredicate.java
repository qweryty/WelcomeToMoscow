package newteam2.welcometomoscow;

/**
 * Created by artem on 10/30/16.
 */

import com.google.android.gms.maps.model.LatLng;

/**
 * Each quest event has conditions that must be full filled before the event is triggered.
 * Usually this condition is = the player has arrived to some spot.
 * Use this function to check if it is time to run the next quest event.
 */

public interface QuestEventPredicate {
    boolean test(PlayerData playerData);
}
