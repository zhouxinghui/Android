package ebag.hd.widget.questions.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        initLinePaint();
    }

    /**
     * 初始化数据
     */
    private void initLinePaint() {
        linePaint.setColor(getResources().getColor(R.color.question_bg_pressed));
        linePaint.setStrokeWidth(getResources().getDimension(R.dimen.x3));
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
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

    public String questionId = "";

    /**
     * 检查这个Connection是否正确
     * @param connectionBean
     */
    private void checkConnection(ConnectionView.ConnectionBean connectionBean){
        if(connectionBean.connectionBean == null){
            throw new IllegalArgumentException("请建立连线的关系" + questionId);
        }
        if(connectionBean.isRight == connectionBean.connectionBean.isRight){
            throw new IllegalArgumentException("连线需要连不同的两侧" + questionId);
        }

        if(connectionBean != connectionBean.connectionBean.connectionBean){
            throw new IllegalArgumentException("连线的Item 需要相互关联" + questionId);
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

    public void clearLines(){
        list.clear();
//        invalidate();
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
//        canvas.restore();
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
        canvas.save();
    }
}