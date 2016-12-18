package newteam2.welcometomoscow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Джал on 12/18/2016.
 */

public class ResponseQ {
    @SerializedName("response")
    @Expose
    private ArrayList<QuestInfo> questList = new ArrayList<>();

    public ArrayList<QuestInfo> getQuestList() {
        return questList;
    }
}
