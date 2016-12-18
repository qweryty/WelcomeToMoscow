package newteam2.welcometomoscow;

/**
 * Created by artem on 10/20/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Holds all the data bout one single quest.
 * All events to visit
 * All the story lines.
 * All special images/ all text.
 * All time/place values.
 */
class QuestInfo {
    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("desc")
    @Expose
    String info_short;

    @SerializedName("long_desc")
    @Expose
    String info_long;

    @SerializedName("events")
    @Expose
    List<QuestEvent> events;

    @SerializedName("icon_id")
    @Expose
    int iconId;

    @SerializedName("id")
    @Expose
    long id;

    QuestInfo(String name, String info_short, String info_long, int icon
            , long id, List<QuestEvent> event) {
        this.name = name;
        this.info_short = info_short;
        this.info_long = info_long;
        this.iconId = icon;
        this.id = id;
        this.events = event;
    }

    public int getIconId() {
        return iconId;
    }

    public long getId() {
        return id;
    }

    public List<QuestEvent> getEvents() {
        return events;
    }

    public String getInfo_long() {
        return info_long;
    }

    public String getInfo_short() {
        return info_short;
    }

    public String getName() {
        return name;
    }
}
