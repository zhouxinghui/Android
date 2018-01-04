package ebag.hd.widget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ebag.hd.R;
import ebag.hd.widget.questions.ConnectionView;

public class ConnectionLineView extends View {
    private List<ConnectionView.ConnectionBean> list = new ArrayList<>();
    /**
     * 文本的颜色
     */
    private Paint linePaint = new Paint();
    /**
    * 绘制时控制文本绘制的范围
    */
    private Rect mBound;
    private Paint mPaint;

    /**
     * 答案是否正确画的线
     */
    private boolean isNormal = true;

    public ConnectionLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConnectionLineView(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ConnectionLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
//        mPaint.setTextSize(5);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds("", 0, 0, mBound);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initLinePaint();
    }

    private void initLinePaint() {
        linePaint.setColor(getResources().getColor(R.color.question_bg_pressed));
        linePaint.setStrokeWidth(getResources().getDimension(R.dimen.x5));
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//         super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
        case MeasureSpec.EXACTLY:// 明确指定了
            height = getPaddingTop() + getPaddingBottom() + specSize;
            break;
        case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
            height = getPaddingTop() + getPaddingBottom() + mBound.height();
            break;
        }

        setMeasuredDimension(width, height);

    }

    public void setWrongLine(ConnectionView.ConnectionBean connectionBean){
        setLine(connectionBean, false);
    }

    /**
     * 画线
     */
    public void setLine(ConnectionView.ConnectionBean connectionBean,boolean isNormal) {

        checkConnection(connectionBean);
        //设置左边的item 为默认 item
        if(connectionBean.isRight)
            connectionBean = connectionBean.connectionBean;

        if(!list.contains(connectionBean)){
            list.add(connectionBean);
        }
        this.isNormal = isNormal;
        invalidate();
    }

    public void removeLine(ConnectionView.ConnectionBean connectionBean){
        if(!isConnectionRegular(connectionBean)){
            return;
        }
        //设置左边的item 为默认 item
        if(connectionBean.isRight)
            connectionBean = connectionBean.connectionBean;
        for (int i = 0; i < list.size(); i++) {
            if(connectionBean.equals(list.get(i))){
                list.remove(connectionBean);
                invalidate();
                return;
            }
        }
    }

    /**
     * 检查这个Connection是否正确
     * @param connectionBean
     */
    private void checkConnection(ConnectionView.ConnectionBean connectionBean){
        if(connectionBean.connectionBean == null){
            throw new IllegalArgumentException("请建立连线的关系");
        }
        if(connectionBean.isRight == connectionBean.connectionBean.isRight){
            throw new IllegalArgumentException("连线需要连不同的两侧");
        }

        if(connectionBean != connectionBean.connectionBean.connectionBean){
            throw new IllegalArgumentException("连线的Item 需要相互关联");
        }
    }

    public boolean hasLine(ConnectionView.ConnectionBean connectionBean){
        //设置左边的item 为默认 item
        if(connectionBean.isRight)
            connectionBean = connectionBean.connectionBean;

        if(isConnectionRegular(connectionBean)){
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            if(connectionBean.equals(list.get(i))){
                return true;
            }
        }
        return false;
    }

    public static boolean isConnectionRegular(ConnectionView.ConnectionBean connectionBean){
        if(connectionBean.connectionBean == null //空判断
                || connectionBean.isRight == connectionBean.connectionBean.isRight//同侧判断
                || connectionBean != connectionBean.connectionBean.connectionBean){//相互关联判断
            return false;
        }
        return true;
    }

    public List<ConnectionView.ConnectionBean> getLines(){
        return list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        mPaint.setColor(Color.TRANSPARENT);
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                drawLine(list.get(i), canvas);
            }
        }
    }

    /**
     * 画线
     * 
     * @param connectionBean
     * @param canvas
     */
    private void drawLine(ConnectionView.ConnectionBean connectionBean, Canvas canvas) {

        if(connectionBean.isRight)
            connectionBean = connectionBean.connectionBean;

        if(isNormal)//普通
            linePaint.setColor(getResources().getColor(R.color.question_bg_pressed));
        else if (connectionBean.isCorrect)//下面是setwrong 时做的判断
            linePaint.setColor(getResources().getColor(R.color.question_bg_pressed));
        else
            linePaint.setColor(getResources().getColor(R.color.question_bg_error));

        canvas.drawLine(0, connectionBean.pointY,
                getWidth(), connectionBean.connectionBean.pointY, linePaint);
    }
}