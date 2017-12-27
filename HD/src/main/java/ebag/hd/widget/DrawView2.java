package ebag.hd.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ebag.hd.R;


/**
 * Created by limingxin on 2016-1-2.
 */
public class DrawView2 extends View {
    private static final String TAG = "DrawView";
    Path mPath;
    Canvas mCanvas;
    Paint mPaint;
    int width;
    int height;
    int color;

    float mX = 0.0f;//上一个点的坐标
    float mY = 0.0f;//上一个点的坐标
    private Bitmap cachebBitmap;
    private Bitmap mBackgroud;
    private boolean isDraw;

    private int viewWidth;
    private int viewHeight;

    private int topLocation;//画板在屏幕中顶部的坐标
    private StringBuilder sbuilder = new StringBuilder();//记录画笔轨迹

    public void setTopLocation(int topLocation) {
        this.topLocation = topLocation;
    }

    public DrawView2(Context context) {
        this(context, null);
    }

    public DrawView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawView);
        boolean isEnglish = a.getBoolean(R.styleable.DrawView_isEnglish, false);
        if (isEnglish)
            mBackgroud = BitmapFactory.decodeResource(getResources(),
                    R.drawable.write_chinese_big);// 获取背景图片
        else
            mBackgroud = BitmapFactory.decodeResource(getResources(),
                    R.drawable.write_chinese_big);// 获取背景图片

        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setColor(Color.BLACK);// 设置画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);// 设置画笔的样式
        mPaint.setStrokeWidth(4);// 设置画笔的粗细
        mPaint.setStrokeJoin(Paint.Join.ROUND); //画笔接洽点类型 如影响矩形但角的外轮廓
        mPaint.setStrokeCap(Cap.ROUND);// 设置画笔笔头圆形
        mPaint.setDither(true);// 使用图像抖动处理，更平滑和饱满
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            viewWidth = mBackgroud.getWidth();
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            viewHeight = mBackgroud.getHeight();
        }
        cachebBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_4444);
        mCanvas = new Canvas(cachebBitmap);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectf = new RectF(0, 0, viewWidth, viewHeight);
        canvas.drawBitmap(mBackgroud, null, rectf, mPaint);
        canvas.drawBitmap(cachebBitmap, null, rectf, null);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = x;
                mY = y;
                sbuilder.append((int) (x)).append(",").append((int) (y - topLocation)).append(",");
                mPath.moveTo(x, y);
                isDraw = true;
                break;
            case MotionEvent.ACTION_MOVE:
                sbuilder.append((int) (x)).append(",").append((int) (y - topLocation)).append(",");
                //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                break;
        }
        invalidate();
        return true;
    }

    public String getTrack() {
        if (sbuilder.length() > 0) {
            sbuilder.append("-1,").append("0,");
            return sbuilder.toString();
        } else {
            return "还没有写字";
        }
    }

    public void deleteSb() {
        sbuilder.delete(0, sbuilder.length());
    }


    // 清除画板
    public void clearPaint() {
        isDraw = false;
        //init();
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        mCanvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        invalidate();
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }

    public void setPaintSize(int size) {
        mPaint.setStrokeWidth(size);
    }

    // 橡皮擦
    public void eraser() {
        mPaint.setAlpha(0); // 设置透明度为0
        mPaint.setColor(Color.TRANSPARENT);// 必须设置成透明色
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); // 设置两图相交时的模式，那相交处同
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 消除拉动，使画面圓滑
        mPaint.setStyle(Paint.Style.STROKE); // 设置画笔为空心，否则会是首尾连起来多边形内一块为透明。
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 结合方式，平滑
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆头
        mPaint.setStrokeWidth(15);// 设置空心边框宽
    }

    // 得到图片
    public Bitmap getBitmap() {
        if (isDraw) {
            // Matrix matrix = new Matrix();
            // matrix.postScale(0.5f, 0.5f);
//            Bitmap copyBitmap = Bitmap.createScaledBitmap(cachebBitmap,
//                    cachebBitmap.getWidth(), cachebBitmap.getHeight(),true);
            Bitmap copy = cachebBitmap.copy(Bitmap.Config.ARGB_4444, true);
            return copy;
        } else {
            return null;
        }
    }

}
