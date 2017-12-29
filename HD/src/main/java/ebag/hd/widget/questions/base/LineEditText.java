package ebag.hd.widget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class LineEditText extends AppCompatEditText {
	private Paint linePaint;
    private float margin;
    private int paperColor;

    public LineEditText(Context paramContext){
        this(paramContext,null);
    }

    public LineEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.linePaint = new Paint();
        linePaint.setAntiAlias(true);
    }

    protected void onDraw(Canvas paramCanvas) {
        paramCanvas.drawColor(this.paperColor);
        int i = getLineCount();
        int j = getHeight();
        int k = getLineHeight();
        int m = 1 + j / k;
        if (i < m)
            i = m;
        int n = getCompoundPaddingTop();
        int o = (int) getLineSpacingExtra() - 10;
        paramCanvas.drawLine(0.0F, n-o, getRight(), n-o, this.linePaint);
        for (int i2 = 0; ; i2++) {
            if (i2 >= i) {
                setPadding(10 + (int) this.margin, 0, 0, 0);
                super.onDraw(paramCanvas);
                paramCanvas.restore();
                return;
            }
            n += k;
            paramCanvas.drawLine(0.0F, n-o, getRight(), n-o, this.linePaint);
            paramCanvas.save();
        }
    }
}
