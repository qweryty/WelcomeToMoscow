package newteam2.welcometomoscow;

import android.app.Application;

/**
 * Created by artem on 10/22/16.
 */

public class MainApplication extends Application {
    private QuestInfo currentQuest;

    public void setCurrentQuestInfo(QuestInfo qi) {
        currentQuest = qi;
    }

    public QuestInfo getCurrentQuestInfo() {
        return currentQuest;
    }
}
