package ebag.hd.widget.questions.completion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.style.ImageSpan;

/**
 * Created by 90323 on 2017/8/14.
 */

public class TypeImageSpan extends ImageSpan {
    private String type = "";
    public TypeImageSpan(Bitmap b) {
        super(b);
    }

    public TypeImageSpan(Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public TypeImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public TypeImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public TypeImageSpan(Drawable d) {
        super(d);
    }

    public TypeImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public TypeImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public TypeImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public TypeImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public TypeImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public TypeImageSpan(Context context, @DrawableRes int resourceId) {
        super(context, resourceId);
    }

    public TypeImageSpan(Context context, @DrawableRes int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
