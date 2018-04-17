package ebag.core.widget.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import ebag.core.R;


/**
 * Created by caoyu on 2017/11/24.
 */

public class DrawableUtil {

    private int leftDrawableWidth, leftDrawableHeight;  //左图标宽高
    private int topDrawableWidth, topDrawableHeight;  //顶图标宽高
    private int rightDrawableWidth, rightDrawableHeight;  //右图标宽高
    private int bottomDrawableWidth, bottomDrawableHeight;  //底图标宽高
    private final int DEFAULT_SIZE = -0x1;

    private DrawableUtil(){
    }

    public static DrawableUtil get(){
        return new DrawableUtil();
    }

    public Drawable[] getDrawable(Context context, AttributeSet attrs){
        Drawable[] drawables = new Drawable[4];

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);

        drawables[0] = array.getDrawable(R.styleable.DrawableTextView_leftDrawable);
        leftDrawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableWidth, DEFAULT_SIZE);
        leftDrawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableHeight, DEFAULT_SIZE);

        drawables[1] = array.getDrawable(R.styleable.DrawableTextView_topDrawable);
        topDrawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableWidth, DEFAULT_SIZE);
        topDrawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableHeight, DEFAULT_SIZE);

        drawables[2] = array.getDrawable(R.styleable.DrawableTextView_rightDrawable);
        rightDrawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableWidth, DEFAULT_SIZE);
        rightDrawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableHeight, DEFAULT_SIZE);

        drawables[3] = array.getDrawable(R.styleable.DrawableTextView_bottomDrawable);
        bottomDrawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableWidth, DEFAULT_SIZE);
        bottomDrawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableHeight, DEFAULT_SIZE);

        array.recycle();


        setDrawableBounds(drawables);

        return drawables;
    }

    void setDrawableBounds(Drawable[] drawable) {
        if(drawable[0] != null)
            drawable[0].setBounds(0,0,retestDrawableWidth(drawable[0], leftDrawableWidth),
                    retestDrawableHeight(drawable[0], leftDrawableHeight));
        if(drawable[1] != null)
            drawable[1].setBounds(0,0,retestDrawableWidth(drawable[1], topDrawableWidth),
                    retestDrawableHeight(drawable[1], topDrawableHeight));
        if(drawable[2] != null)
            drawable[2].setBounds(0,0,retestDrawableWidth(drawable[2], rightDrawableWidth),
                    retestDrawableHeight(drawable[2], rightDrawableHeight));
        if(drawable[3] != null)
            drawable[3].setBounds(0,0,retestDrawableWidth(drawable[3], bottomDrawableWidth),
                    retestDrawableHeight(drawable[3], bottomDrawableHeight));
    }

    /**
     * 重新测量图标的宽度
     * @param drawable  图标
     * @param drawableWidth  图标宽度
     * @return
     */
    private int retestDrawableWidth(Drawable drawable, int drawableWidth){
        if(drawable != null){
            if(drawableWidth == DEFAULT_SIZE) {
                drawableWidth = drawable.getIntrinsicWidth();
            }
            return drawableWidth;
        }
        return 0;
    }

    /**
     * 重新测量图标的高度
     * @param drawable  图标
     * @param drawableHeight  图标高度
     * @return
     */
    private int retestDrawableHeight(Drawable drawable, int drawableHeight){
        if(drawable != null){
            if(drawableHeight == DEFAULT_SIZE) {
                drawableHeight = drawable.getIntrinsicHeight();
            }
            return drawableHeight;
        }
        return 0;
    }

}
