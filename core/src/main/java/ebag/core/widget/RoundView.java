package ebag.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ebag.core.R;


public class RoundView extends View {
    /**
     * ���ʶ��������
     */
    private Paint paint;

    /**
     * Բ������ɫ
     */
    private int roundColor;

    /**
     * Բ����ȵ���ɫ
     */
    private int roundProgressColor;

    /**
     * �м��Ȱٷֱȵ��ַ����ɫ
     */
    private int textColor;

    /**
     * �м��Ȱٷֱȵ��ַ������
     */
    private float textSize;

    /**
     * Բ���Ŀ��
     */
    private float roundWidth;

    /**
     * �����
     */
    private int max;

    /**
     * ��ǰ���
     */
    private int progress;
    /**
     * �Ƿ���ʾ�м�Ľ��
     */
    private boolean textIsDisplayable;

    /**
     * ��ȵķ��ʵ�Ļ��߿���
     */
    private int style;

    private String title;//中间的文字

    public void setTitle(String title) {
        this.title = title;
    }

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundView);

        // ��ȡ�Զ������Ժ�Ĭ��ֵ
        roundColor = mTypedArray.getColor(
                R.styleable.RoundView_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(
                R.styleable.RoundView_roundProgressColor, Color.BLACK);
        textColor = mTypedArray.getColor(
                R.styleable.RoundView_textColor, Color.BLACK);
        textSize = mTypedArray.getDimension(
                R.styleable.RoundView_textSize, 15);
        roundWidth = mTypedArray.getDimension(
                R.styleable.RoundView_roundWidth, 5);
        progress = mTypedArray.getInteger(
                R.styleable.RoundView_progress, 30);
        max = mTypedArray.getInteger(R.styleable.RoundView_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(
                R.styleable.RoundView_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundView_style, 0);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * �������Ĵ�Բ��
         */
        int centre = getWidth() / 2; // ��ȡԲ�ĵ�x���
        int radius = (int) (centre - roundWidth / 2); // Բ���İ뾶
        paint.setColor(roundColor); // ����Բ������ɫ
        paint.setStyle(Paint.Style.STROKE); // ���ÿ���
        paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
        paint.setAntiAlias(true); // �����
        canvas.drawCircle(centre, centre, radius, paint); // ����Բ��

        Log.e("log", centre + "");

        /**
         * ����Ȱٷֱ�
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT); // ��������
        paint.setStyle(Paint.Style.FILL); //设置实心
        int percent = (int) (((float) progress / (float) max) * 100); // �м�Ľ�Ȱٷֱȣ���ת����float�ڽ��г����㣬��Ȼ��Ϊ0
        float textWidth_percent = paint.measureText(percent + ""); // ���������ȣ�������Ҫ�������Ŀ��������Բ���м�

        /*if (textIsDisplayable && style == STROKE) {
            if (TextUtils.isEmpty(title)) {
                canvas.drawText(percent + "", centre - textWidth_percent / 2, centre
                        + textSize / 2, paint); // ������Ȱٷֱ�
            } else {
                float textWidth_title = paint.measureText(title);
                canvas.drawText(title, centre - textWidth_title / 2, centre
                        + textSize / 2, paint); // ������Ȱٷֱ�
            }
        }*/

        if (!TextUtils.isEmpty(title)) {
            paint.setStrokeWidth(2);
            paint.setColor(0x80ffffff);
            RectF rectF = new RectF(centre - radius + roundWidth + 5, centre - radius + roundWidth + 5, centre
                    + radius - roundWidth - 5, centre + radius - roundWidth - 5);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectF, 0, 360, false, paint);
        }

        /**
         * ��Բ�� ����Բ���Ľ��
         */

        // ���ý����ʵ�Ļ��ǿ���
        paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
        paint.setColor(roundProgressColor); // ���ý�ȵ���ɫ
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, 360 * progress / max, false, paint); // ��ݽ�Ȼ�Բ��
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, -90, 360 * progress / max, true, paint); // ��ݽ�Ȼ�Բ��
                break;
            }
        }

    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * ���ý�ȵ����ֵ
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * ��ȡ���.��Ҫͬ��
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * ���ý�ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ�� ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
//            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

}
