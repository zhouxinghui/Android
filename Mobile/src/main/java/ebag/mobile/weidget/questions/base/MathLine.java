package ebag.mobile.weidget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ebag.mobile.R;


/**
 * Created by caoyu on 2018/1/5.
 */

public class MathLine extends View{

    //横线
    public final static int HORIZONTAL_LINE = 1;
    //竖线
    public final static int VERTICAL_LINE = 2;
    //左边角
    public final static int LEFT_HORN = 3;
    //右边角
    public final static int RIGHT_HORN = 4;

    //底部横线
    public final static int BOTTOM_HORIZONTAL_LINE = 5;

    //底部左边角
    public final static int BOTTOM_LEFT_HORN = 6;
    //底部右边角
    public final static int BOTTOM_RIGHT_HORN = 7;

    private Paint linePaint = new Paint();

    private int lineColor = getResources().getColor(R.color.question_normal);
    private float lineWidth = getResources().getDimension(R.dimen.question_math_line);
    private int style = HORIZONTAL_LINE;

    public MathLine(Context context) {
        this(context, null);
    }

    public MathLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MathLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLinePaint();
    }

    private void initLinePaint() {
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (style){
            case HORIZONTAL_LINE:
                canvas.drawLine(0, getHeight() / 2,
                        getWidth(), getHeight() / 2, linePaint);
                canvas.save();
                break;
            case VERTICAL_LINE:
                canvas.drawLine(getWidth()  / 2, 0,
                        getWidth() / 2, getHeight(), linePaint);
                canvas.save();
                break;
            case LEFT_HORN:
                //竖
                canvas.drawLine(getWidth() / 2, 0,
                        getWidth() / 2, getHeight() / 2, linePaint);
                canvas.save();
                //横
                canvas.drawLine(getWidth() / 2, getHeight() / 2,
                        getWidth(), getHeight() / 2, linePaint);
                canvas.save();
                break;
            case RIGHT_HORN:
                //竖
                canvas.drawLine(getWidth() / 2, 0,
                        getWidth() / 2, getHeight() / 2, linePaint);
                canvas.save();
                //横
                canvas.drawLine(0, getHeight() / 2,
                        getWidth() / 2, getHeight() / 2, linePaint);
                canvas.save();
                break;
            case BOTTOM_HORIZONTAL_LINE:
                canvas.drawLine(0, getHeight() - lineWidth / 2,
                        getWidth(), getHeight() - lineWidth / 2, linePaint);
                canvas.save();
                break;
            case BOTTOM_LEFT_HORN:
                //竖
                canvas.drawLine(getWidth() / 2, 0,
                        getWidth() / 2, getHeight(), linePaint);
                canvas.save();
                //横
                canvas.drawLine(getWidth() / 2, getHeight()- lineWidth / 2,
                        getWidth(), getHeight()- lineWidth / 2, linePaint);
                canvas.save();
                break;
            case BOTTOM_RIGHT_HORN:
                //竖
                canvas.drawLine(getWidth() / 2, 0,
                        getWidth() / 2, getHeight(), linePaint);
                canvas.save();
                //横
                canvas.drawLine(0, getHeight()- lineWidth / 2,
                        getWidth() / 2, getHeight()- lineWidth / 2, linePaint);
                canvas.save();
                break;
        }

        canvas.restore();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        linePaint.setColor(lineColor);
        invalidate();
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        linePaint.setStrokeWidth(lineWidth);
        invalidate();
    }

    public void setStyle(int style) {
        this.style = style;
        invalidate();
    }
}
