package ebag.hd.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ebag.core.util.L;
import ebag.hd.R;

public class MyLineView extends View {
    List<MyLine> list;
    /**
     * 文本的颜色
     */
    Paint linePaint = new Paint();
    /**
    * 绘制时控制文本绘制的范围
    */
    private Rect mBound;
    private Paint mPaint;

    public MyLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineView(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(5);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds("", 0, 0, mBound);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // TODO Auto-generated method stub
        initLinePaint();
        list = new ArrayList<>();
    }

    private void initLinePaint() {
        // TODO Auto-generated method stub
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

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                drawLine(list.get(i), canvas);
            }
        }
    }

    /**
     * 画线
     * @param position1 左边的position
     * @param position2 右边的position
     * @param leftY 左边的Y坐标
     * @param rightY 右边的Y坐标
     */
    public void setLine(int position1, int position2, int leftY, int rightY) {
        Point pointLeft = new Point(0, leftY);
        Point pointRight = new Point(getWidth(), rightY);
        // 如果没满 或者不存在 那么给他添加 否则 进行设置
        if (!isExists(position1, position2)) {

            if(isSameSide(position1,position2)){
                return;
            }
            String answer = String.valueOf(position1) + "," + String.valueOf(position2);
            L.INSTANCE.d("存入的答案 = " + answer);
            MyLine myLine = new MyLine(pointLeft, pointRight, answer);
            list.add(myLine);
        } else {
            if(isSameSide(position1,position2)){
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                MyLine myLine = list.get(i);
                if (myLine.answer.contains(String.valueOf(position1))
                        || myLine.answer.contains(String.valueOf(position2))) {
                    list.set(i, new MyLine(pointLeft, pointRight,
                                    String.valueOf(position1) + ","
                                            + String.valueOf(position2)));
                }
            }
        }
        invalidate();
    }

    public int[] removeLine(int position){
        if (list == null || list.size() == 0)
            return null;
        int[] positions = new int[2];
        for (int i = 0; i < list.size(); i++) {
            MyLine myLine = list.get(i);
            positions[0] = Integer.parseInt((myLine.getAnswer().split(","))[0]);
            positions[1] = Integer.parseInt((myLine.getAnswer().split(","))[1]);
            if (myLine.answer.contains(String.valueOf(position))) {
                list.remove(i);
                invalidate();
                return positions;
            }
        }
        return null;
    }

    public boolean hasLine(int position){
        if (list == null || list.size() == 0)
            return false;
        for (int i = 0; i < list.size(); i++) {
            MyLine myLine = list.get(i);
            if (myLine.answer.contains(String.valueOf(position)))
                return true;
        }
        return false;
    }


    /**
     * 是否是同一侧
     * @param answer1
     * @param answer2
     * @return
     */
    private boolean isSameSide(int answer1, int answer2) {
        if (answer1 % 2 == 0 && answer2 % 2 == 0) {//同一侧
            // i为偶数
            return true;
        }
        if (answer1 % 2 != 0 && answer2 % 2 != 0) {//同一侧
            // i为奇数
            return true;
        }
        return false;


    }

    /**
     * 根据传入的答案判断 是否已经存入数据
     * 
     * @param anwer1
     * @param anwer2
     * @return
     */
    private boolean isExists(int answer1, int answer2) {
        if (list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            MyLine myLine = list.get(i);
            if (myLine.answer != null
                    && (myLine.answer.contains(String.valueOf(answer1)) || myLine.answer
                            .contains(String.valueOf(answer2)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 画线
     * 
     * @param myLine
     * @param canvas
     */
    private void drawLine(MyLine myLine, Canvas canvas) {
        canvas.drawLine(myLine.getStartX(), myLine.getStartY(),
                myLine.getEndX(), myLine.getEndY(), linePaint);
    }

    class MyLine {

        int startX;
        int startY;
        int endX;
        int endY;
        String answer;

        public MyLine(Point point1, Point point2, String answer1) {
            this.startX = point1.x;
            this.startY = point1.y;
            this.endX = point2.x;
            this.endY = point2.y;
            this.answer = answer1;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public int getEndX() {
            return endX;
        }

        public void setEndX(int endX) {
            this.endX = endX;
        }

        public int getEndY() {
            return endY;
        }

        public void setEndY(int endY) {
            this.endY = endY;
        }

        @Override
        public String toString() {
            return "MyLine [startX=" + startX + ", startY=" + startY
                    + ", endX=" + endX + ", endY=" + endY + ", answer=" + answer
                    + "]";
        }

    }
}