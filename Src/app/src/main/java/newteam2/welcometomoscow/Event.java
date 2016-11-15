package newteam2.welcometomoscow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Джал on 11/6/2016.
 */

public class Event {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("long")
    @Expose
    private String $long;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("dist")
    @Expose
    private String dist;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("answer")
    @Expose
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get$long() {
        return $long;
    }

    public void set$long(String $long) {
        this.$long = $long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
