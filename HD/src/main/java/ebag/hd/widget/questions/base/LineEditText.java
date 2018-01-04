package ebag.hd.widget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import ebag.hd.R;

public class LineEditText extends CursorEditText {
    private Paint mPaint;

    public LineEditText(Context context) {
        this(context, null);
    }

    public LineEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.question_normal));
    }

    protected void onDraw(Canvas canvas) {

        int c = getLineCount();
        int j = getHeight();
        int k = getLineHeight();
        int m = 1 + j / k;
        if (c < m)
            c = m;
        int n = getCompoundPaddingTop();
        int o = (int) (getLineSpacingExtra() / 2);
        for (int i = 0; i < c ; i++) {
            n += k;
            canvas.drawLine(0.0F, n - o, getRight(), n - o, mPaint);
            canvas.save();
        }
        super.onDraw(canvas);
    }
}
