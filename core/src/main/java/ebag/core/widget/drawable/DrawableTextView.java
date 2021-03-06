package ebag.core.widget.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Created by caoyu on 2017/11/22.
 */

public class DrawableTextView extends AppCompatTextView {


    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable[] drawables = DrawableUtil.get().getDrawable(context, attrs);

        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

}