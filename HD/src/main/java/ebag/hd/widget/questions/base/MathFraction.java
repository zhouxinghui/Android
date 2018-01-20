package ebag.hd.widget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import ebag.hd.R;

/**
 * Created by caoyu on 2018/1/6.
 */

public class MathFraction extends ViewGroup {

    public static final int NONE = -1;
    public static final int TOP = 0;
    public static final int BOTTOM = 1;

    private int type = NONE;

    private Paint linePaint = new Paint();

    private int lineColor = getResources().getColor(R.color.question_normal);
    private float lineWidth = getResources().getDimension(R.dimen.question_math_line);


    public MathFraction(Context context) {
        this(context, null);
    }

    public MathFraction(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MathFraction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        setWillNotDraw(false);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount() > 1){
            throw new IllegalStateException("这个View  只能有一个 ChildView");
        }
        View child = getChildAt(0);
        measureChild(child,widthMeasureSpec,heightMeasureSpec);
        int cWidth = child.getMeasuredWidth();
        int cHeight = child.getMeasuredHeight();
        MarginLayoutParams cParams = (MarginLayoutParams) child.getLayoutParams();

        setMeasuredDimension(cParams.leftMargin + cParams.rightMargin + cWidth
                , cParams.topMargin + cParams.bottomMargin + cHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(getChildCount() > 1){
            throw new IllegalStateException("这个View  只能有一个 ChildView");
        }
        View child = getChildAt(0);
        int cWidth = child.getMeasuredWidth();
        int cHeight = child.getMeasuredHeight();
        MarginLayoutParams cParams = (MarginLayoutParams) child.getLayoutParams();

        child.layout(cParams.leftMargin, cParams.topMargin, cWidth + cParams.rightMargin, cHeight + cParams.topMargin);

    }

    public void setLineType(int type){
        this.type = type;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (type){
            case TOP:
                canvas.drawLine(0, 0,
                        getWidth(), 0, linePaint);
                canvas.save();
                break;
            case BOTTOM:
                canvas.drawLine(0, getHeight(),
                        getWidth(), getHeight(), linePaint);
                canvas.save();
                break;
        }
//        super.onDraw(canvas);
    }
}
