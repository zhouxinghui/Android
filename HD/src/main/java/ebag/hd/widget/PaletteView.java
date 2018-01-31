package ebag.hd.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ebag.core.util.AsyncTaskUtil;

/**
 * Created by wensefu on 17-3-21.
 */
public class PaletteView extends View {

    private Paint mPaint;
    private Path mPath;
    private float mLastX;
    private float mLastY;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

    private static final int MAX_CACHE_STEP = 20;

    private List<DrawingInfo> mDrawingList;
    private List<DrawingInfo> mRemovedList;

    private Xfermode mClearMode;
    private float mDrawSize = 8;
    private float mEraserSize = 40;

    private boolean mCanEraser;

    private boolean hasBitmap = false;

    private boolean canDraw = true;

    private Bitmap mFirstLoadBitMap;

    private Callback mCallback;

    public enum Mode {
        DRAW,
        ERASER
    }

    private Mode mMode = Mode.DRAW;

    public PaletteView(Context context) {
        this(context,null);
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        init();
    }

    public interface Callback {
        void onUndoRedoStatusChanged();
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//画笔接洽点类型 如影响矩形但角的外轮廓
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔笔头圆形
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 使用图像抖动处理，更平滑和饱满
        mPaint.setStrokeWidth(mDrawSize);
        mPaint.setColor(0XFF000000);

        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    private void initBuffer(){
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 设置首次加载的绘画轨迹（实际为bitmap）,如果设置进来的bitmap为mull，所有东西将会被清空
     * @param mCacheBitmap
     */
    public void setFirstLoadBitmap(Bitmap mCacheBitmap){
        if(mCacheBitmap == null){
            hasBitmap = false;
            mBufferBitmap = null;
            mBufferCanvas = null;
            if (mDrawingList != null) {
                mDrawingList.clear();
            }
            if (mRemovedList != null) {
                mRemovedList.clear();
            }
            this.mFirstLoadBitMap = null;
        }else{
            hasBitmap = true;
            this.mFirstLoadBitMap = mCacheBitmap;
        }

        invalidate();
    }

    public void setFirstLoadBitmap(String path){
        new MyAsyncTask(this).execute(path);
    }

    private abstract static class DrawingInfo {
        Paint paint;
        abstract void draw(Canvas canvas);
    }

    private static class PathDrawingInfo extends DrawingInfo{

        Path path;

        @Override
        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }

    public Mode getMode() {
        return mMode;
    }

    /**
     * 画笔或者橡皮檫
     * @param mode
     */
    public void setMode(Mode mode) {
        if (mode != mMode) {
            mMode = mode;
            if (mMode == Mode.DRAW) {
                mPaint.setXfermode(null);
            } else {
                mPaint.setXfermode(mClearMode);
            }
        }
    }

    public void setEraserSize(float size) {
        mEraserSize = size;
        mPaint.setStrokeWidth(mEraserSize);
    }

    public void setPenRawSize(float size) {
        mDrawSize = size;
        mPaint.setStrokeWidth(mDrawSize);
    }

    public void setPenColor(int color) {
        mPaint.setColor(color);
    }

    public void setPenAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }



    private void reDraw(){
        if (mDrawingList != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (DrawingInfo drawingInfo : mDrawingList) {
                drawingInfo.draw(mBufferCanvas);
            }
            invalidate();
        }
    }

    public boolean canRedo() {
        return mRemovedList != null && mRemovedList.size() > 0;
    }

    public boolean canUndo(){
        return mDrawingList != null && mDrawingList.size() > 0;
    }

    public void redo() {
        int size = mRemovedList == null ? 0 : mRemovedList.size();
        if (size > 0) {
            DrawingInfo info = mRemovedList.remove(size - 1);
            mDrawingList.add(info);
            mCanEraser = true;
            reDraw();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public void undo() {
        int size = mDrawingList == null ? 0 : mDrawingList.size();
        if (size > 0) {
            DrawingInfo info = mDrawingList.remove(size - 1);
            if (mRemovedList == null) {
                mRemovedList = new ArrayList<>(MAX_CACHE_STEP);
            }
            if (size == 1) {
                mCanEraser = false;
            }
            mRemovedList.add(info);
            reDraw();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public void clear() {
        if (mBufferBitmap != null) {
            hasBitmap = false;
            if (mDrawingList != null) {
                mDrawingList.clear();
            }
            if (mRemovedList != null) {
                mRemovedList.clear();
            }
            mCanEraser = false;
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public Bitmap buildBitmap() {
        if(hasBitmap || (mDrawingList != null && !mDrawingList.isEmpty())){
            Bitmap bm = getDrawingCache();
            Bitmap result = Bitmap.createBitmap(bm);
            destroyDrawingCache();
            return result;
        }else{
            return null;
        }
    }

    private void saveDrawingPath(){
        if (mDrawingList == null) {
            mDrawingList = new ArrayList<>(MAX_CACHE_STEP);
        } else if (mDrawingList.size() == MAX_CACHE_STEP) {
            mDrawingList.remove(0);
        }
        Path cachePath = new Path(mPath);
        Paint cachePaint = new Paint(mPaint);
        PathDrawingInfo info = new PathDrawingInfo();
        info.path = cachePath;
        info.paint = cachePaint;
        mDrawingList.add(info);
        mCanEraser = true;
        if (mCallback != null) {
            mCallback.onUndoRedoStatusChanged();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mFirstLoadBitMap != null){
            mBufferBitmap = Bitmap.createBitmap(mFirstLoadBitMap,0,0,getWidth(),getHeight()).copy(Bitmap.Config.ARGB_8888, true);
            mBufferCanvas = new Canvas(mBufferBitmap);
           if(mRemovedList != null)
               mDrawingList.clear();
           if(mRemovedList != null)
               mRemovedList.clear();
            mFirstLoadBitMap = null;
        }
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
    }

    public void setCanDraw(boolean canDraw){
        this.canDraw = canDraw;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canDraw){
            return false;
        }
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final float x = event.getX();
        final float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                if(mBufferBitmap == null)
                    mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                if(mBufferCanvas == null)
                    mBufferCanvas = new Canvas(mBufferBitmap);
                if (mMode == Mode.ERASER && !hasBitmap && !mCanEraser) {
                    break;
                }
                mBufferCanvas.drawPath(mPath,mPaint);

                invalidate();
                mLastX = x;
                mLastY = y;
                if (onFingerMoveListener != null){
                    onFingerMoveListener.onFingerMove();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mMode == Mode.DRAW || mCanEraser || hasBitmap) {
                    saveDrawingPath();
                }
                mPath.reset();
                break;
        }
        return true;
    }
    private OnFingerMoveListener onFingerMoveListener;
    public void setOnFingerMoveListener(OnFingerMoveListener onFingerMoveListener){
        this.onFingerMoveListener = onFingerMoveListener;
    }
    public interface OnFingerMoveListener {
        void onFingerMove();
    }

    private static class MyAsyncTask extends AsyncTaskUtil<String, Void, Bitmap, PaletteView>{

        public MyAsyncTask(PaletteView pWeakTarget) {
            super(pWeakTarget);
        }

        @Override
        protected Bitmap doInBackground(PaletteView pTarget, String... strings) {
            return BitmapFactory.decodeFile(strings[0]);
        }

        @Override
        protected void onPostExecute(PaletteView pTarget, Bitmap pResult) {
            if(pResult != null){
                pTarget.setFirstLoadBitmap(pResult);
            }
        }
    }
}