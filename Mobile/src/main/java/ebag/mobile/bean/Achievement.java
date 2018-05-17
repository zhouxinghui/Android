package ebag.mobile.bean;

import java.io.Serializable;

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */

public class Achievement implements Serializable {

    private String id;
    private String date;
    private String time;
    private int score;
    private String summary;

    public Achievement(String date, String time, int score, String summary){
        this.date = date;
        this.time = time;
        this.score = score;
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
