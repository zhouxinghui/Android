package ebag.hd.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by limingxin on 2016-1-2.
 */
public class DrawView2 extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {

    // SurfaceHolder实例
    private SurfaceHolder mSurfaceHolder;
    // Canvas对象
    private Canvas mCanvas;
    // 控制子线程是否运行
    private boolean startDraw;
    // Path实例
    private Path mPath = new Path();
    // Paint实例
    private Paint mpaint = new Paint();

    public DrawView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(); // 初始化
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setColor(Color.BLACK);
        mpaint.setStrokeWidth(10);
        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);

    }

    @Override
    public void run() {
        // 如果不停止就一直绘制
        while (startDraw) {
            // 绘制
            draw();
        }
    }

    /*
     * 创建
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDraw = true;
        new Thread(this).start();
    }

    /*
     * 改变
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    /*
     * 销毁
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        startDraw = false;
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawPath(mPath, mpaint);

        } catch (Exception e) {

        } finally {
            // 对画布内容进行提交
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public void setPaintSize(int size) {
        mpaint.setColor(Color.BLACK);
        mpaint.setStrokeWidth(size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();    //获取手指移动的x坐标
        int y = (int) event.getY();    //获取手指移动的y坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mPath.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                mPath.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    // 重置画布
    public void reset() {
        mPath.reset();
    }
}
