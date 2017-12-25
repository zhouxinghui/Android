package ebag.hd.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ebag.core.util.FileUtil;
import ebag.hd.R;


public class DrawView extends View {
    private static final String TAG = "DrawView";
    private Bitmap cacheBitmap;// 画纸
    private Canvas cacheCanvas;// 创建画布、画家
    private Path path;// 绘图的路径
    public Paint paint;// 画笔
    private float preX, preY;// 之前的XY的位置，用于下面的手势移动
    private boolean isDraw = true;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawView);
        boolean isEnglish = a.getBoolean(R.styleable.DrawView_isEnglish, false);
        Drawable drawable;
        if (isEnglish) {
            drawable = getResources().getDrawable(R.drawable.write_english_big);
        }
        else {
            drawable = getResources().getDrawable(R.drawable.write_chinese_big);
        }
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        cacheBitmap = Bitmap.createBitmap(
                (int)getResources().getDimension(R.dimen.x300),
                (int)getResources().getDimension(R.dimen.x300),
                config);
        cacheCanvas = new Canvas(cacheBitmap);
        cacheCanvas.drawColor(Color.WHITE);
        drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.x300), (int)getResources().getDimension(R.dimen.x300));
        drawable.draw(cacheCanvas);

        a.recycle();
    }

    private void initPaint(){
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿
        paint.setColor(Color.BLACK);// 设置画笔颜色
        paint.setStyle(Paint.Style.STROKE);// 设置画笔的样式
        paint.setStrokeWidth(4);// 设置画笔的粗细
        paint.setStrokeJoin(Paint.Join.ROUND); //画笔接洽点类型 如影响矩形但角的外轮廓
        paint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔笔头圆形
        paint.setDither(true);// 使用图像抖动处理，更平滑和饱满
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cacheBitmap, 0, 0, paint);// 把cacheBitmap画到DrawView上
    }

    // 清除画板
    public void clearPaint() {
        isDraw = false;
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cacheCanvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        invalidate();
    }

    public void setPaintSize(int size) {
        paint.setStrokeWidth(size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 获取触摸位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {// 获取触摸的各个瞬间
            case MotionEvent.ACTION_DOWN:// 手势按下
                getParent().requestDisallowInterceptTouchEvent(true);
                path.moveTo(x, y);// 绘图的起始点
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx > 5 || dy > 5) {// 用户要移动超过5像素才算是画图，免得手滑、手抖现象
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                    cacheCanvas.drawPath(path, paint);// 绘制路径
                    isDraw = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    public String getBitmapPath(String bagId, String homeworkId, String questionId, int position){
        if (isDraw){
            Bitmap bitmap = cacheBitmap.copy(Bitmap.Config.ARGB_4444, true);
            String fileName = FileUtil.getWriteViewItemPath(bagId + homeworkId + questionId) + File.separator + position;
            File file = new File(fileName);
            try {
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);// 以100%的品质创建png
                // 人走带门
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileName;
        }else{
            return null;
        }
    }
}
