package newteam2.welcometomoscow;

/**
 * Created by artem on 10/20/16.
 */


import java.util.List;

/**
 *  Holds all the data bout one single quest.
 *  All events to visit
 *  All the story lines.
 *  All special images/ all text.
 *  All time/place values.
 */
class QuestInfo {
    String name;
    String info_short;
    String info_long;
    List<QuestEvent> events;
    int iconId;
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
}
