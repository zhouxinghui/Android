package ebag.mobile.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/29.
 */

public class ReaderBean implements Serializable {
    private String imagePath;

    private String notePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNotePath() {
        return notePath;
    }

    public void setNotePath(String notePath) {
        this.notePath = notePath;
    }
}
