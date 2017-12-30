package ebag.hd.widget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import ebag.hd.R;

public class LineEditText extends CursorEditText {
    private Rect mRect;
    private Paint mPaint;

    public LineEditText(Context context) {
        this(context, null);
    }

    public LineEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.question_normal));
    }

    protected void onDraw(Canvas canvas) {
        int count = getLineCount();
        int lineSpacing = (int) (getLineSpacingExtra() / 2);
        for (int i = 0; i < count; i++) {
            int baseline = getLineBounds(i, mRect);
            canvas.drawLine(mRect.left, baseline + lineSpacing , mRect.right, baseline + lineSpacing, mPaint);
        }
        super.onDraw(canvas);
    }
}
