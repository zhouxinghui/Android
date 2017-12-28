package ebag.hd.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by YZY on 2017/12/27.
 */

public class WriteViewBean implements Serializable {
    String path;
    Bitmap bitmap;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
