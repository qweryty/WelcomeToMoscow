package newteam2.welcometomoscow;

import android.app.Application;

/**
 * Created by artem on 10/22/16.
 */

public class MainApplication extends Application {
    private QuestInfo currentQuest;
    private QuestEvent currentEvent;
    private PlayerData playerData;

    public void setCurrentQuestInfo(QuestInfo qi) {
        currentQuest = qi;
    }
    public QuestInfo getCurrentQuestInfo() {
        return currentQuest;
    }

    public void setCurrentQuestEvent(QuestEvent qe) {
        currentEvent = qe;
    }
    public QuestEvent getCurrentQuestEvent() {
        return currentEvent;
    }

    public void setCurrentPlayerData(PlayerData pd) {
         playerData = pd;
    }
    public PlayerData getCurrentPlayerData() {
        return playerData;
    }

}
