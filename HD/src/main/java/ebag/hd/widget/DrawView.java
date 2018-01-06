package ebag.hd.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ebag.core.util.FileUtil;
import ebag.hd.R;
import ebag.hd.bean.WriteViewBean;


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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawView);
        boolean isEnglish = a.getBoolean(R.styleable.DrawView_isEnglish, false);
        if (isEnglish){
            cacheBitmap = Bitmap.createBitmap(
                    (int) getResources().getDimension(R.dimen.english_draw_view_width),
                    (int) getResources().getDimension(R.dimen.english_draw_view_height),
                    Bitmap.Config.ARGB_4444);// 建立图像缓冲区用来保存图像
        }else{
            cacheBitmap = Bitmap.createBitmap(
                    (int) getResources().getDimension(R.dimen.chinese_draw_view_width),
                    (int) getResources().getDimension(R.dimen.chinese_draw_view_height),
                    Bitmap.Config.ARGB_4444);// 建立图像缓冲区用来保存图像
        }
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
        cacheCanvas.drawColor(Color.TRANSPARENT);
        initPaint();
        a.recycle();
    }

    private void initPaint(){
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿
        paint.setColor(Color.BLACK);// 设置画笔颜色
        paint.setStyle(Paint.Style.STROKE);// 设置画笔的样式
        paint.setStrokeWidth(10);// 设置画笔的粗细
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

    public void setCacheBitmap(Bitmap cacheBitmap){
        cacheCanvas.drawBitmap(cacheBitmap, 0, 0, paint);
    }

    public void setCacheBitmap(String path){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        cacheCanvas.drawBitmap(BitmapFactory.decodeStream(fis), 0, 0, paint);
    }

    public void setPaintSize(int size) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(size);
    }

    public void setEraser(){
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(10);
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

    public WriteViewBean getBitmap(String bagId, String homeworkId, String questionId, int position){
        if (isDraw){
            WriteViewBean bean = new WriteViewBean();
            Bitmap bitmap = cacheBitmap.copy(Bitmap.Config.ARGB_4444, true);
            String fileName = FileUtil.getWriteViewItemPath(bagId + homeworkId + questionId) + position;
            bean.setBitmap(bitmap);
            bean.setPath(fileName);
            File file = new File(fileName);
            try {
                if(file.exists() && file.isFile())
                    FileUtil.deleteFile(fileName);

                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);// 以100%的品质创建png
                // 人走带门
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bean;
        }else{
            return null;
        }
    }
}
