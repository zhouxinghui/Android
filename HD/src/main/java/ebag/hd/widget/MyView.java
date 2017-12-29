package ebag.hd.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ebag.core.util.L;

public class MyView extends View {

    String pointText[] = { "第一", "第二", "第三", "第四" };
    String reaAnswer[] = { "13", "24" };

    List<MyLine> list;
    List<Point> beginList;
    private int selectLine;// 当前画的第几条线
    //
    /**
     * 文本
     */
    private String mTitleText = "aa";
    /**
     * 文本的颜色
     */
    private int mTitleTextColor = Color.BLACK;
    /**
     * 文本的大小
     */
    private int mTitleTextSize = 10;

    Paint linePaint = new Paint();
    private float width, height;

    //
    // /**
    // * 绘制时控制文本绘制的范围
    // */
    private Rect mBound;
    private Paint mPaint;

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 获得我们所定义的自定义样式属性
         */
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
//                R.styleable.MyView, defStyle, 0);
//        int n = a.getIndexCount();
//        for (int i = 0; i < n; i++) {
//            int attr = a.getIndex(i);
//            switch (attr) {
//            case R.styleable.MyView_titleText:
//                mTitleText = a.getString(attr);
//                break;
//            case R.styleable.MyView_titleTextColor:
//                // 默认颜色设置为黑色
//                mTitleTextColor = a.getColor(attr, Color.BLACK);
//                break;
//            case R.styleable.MyView_titleTextSize:
//                // 默认设置为16sp，TypeValue也可以把sp转化为px
//                mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
//                        .applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
//                                getResources().getDisplayMetrics()));
//                break;
//
//            }
//
//        }
//        a.recycle();

        initData();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);


    }

    /**
     * 初始化数据
     */
    private void initData() {
        // TODO Auto-generated method stub
        initLinePaint();
        list = new ArrayList<MyLine>();
        // 初始化
        // 根据答案个数 将自定义view分成几块 然后获取每个选项的x y 点

    }

    private void initLinePaint() {
        // TODO Auto-generated method stub
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(20);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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

        initAnswerPoint();

    }

    private void initAnswerPoint() {
        beginList = new ArrayList<Point>();
        // 根据答案个数 将自定义view分成几块 然后初始化每个选项的x y 点
        L.INSTANCE.d("宽 = " + getMeasuredWidth() + "  高 = " + getMeasuredHeight());
        int size = pointText.length;
        for (int i = 0; i < size; i++) {
            beginList.add(getPoint(i));
        }

    }

    // 根据传入的答案返回返回线的点
    private Point getPoint(final int select) {
        Point point = null;
        // TODO Auto-generated method stub
        int size = pointText.length;
        L.INSTANCE.d("==宽度  = " + getMeasuredWidth() + "      高度  = "
                + getMeasuredHeight());
        int item = getMeasuredHeight() / size;
        int x = 0, y = 0;

        switch (select) {
        case 0:
            x = 0;
            y = item;
            break;
        case 1:
            x = getMeasuredWidth();
            y = item;
            break;
        case 2:
            x = 0;
            y = item * (select + 1);
            break;
        case 3:
            x = getMeasuredWidth();
            y = item * (select);
            break;

        default:
            break;
        }
        point = new Point(x, y);
        return point;
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
     * 根据答案 判断当前选择的是那一条线 并進行設置
     * 
     * @param answer1
     * @param answer2
     */
    public void setLine(int answer1, int answer2) {
        // list.get(location)
        // 如果没满 或者不存在 那么给他添加 否则 进行设置
        if (!isExists(answer1, answer2)) {

            if(isSameSide(answer1,answer2)){
                return;
            }
            // 或者
            String str = String.valueOf(answer1) + String.valueOf(answer2);
            L.INSTANCE.d("存入的答案 = " + str);
            MyLine myLine = new MyLine(getPoint(answer1), getPoint(answer2),
                    str);
            list.add(myLine);
        } else {
            if(isSameSide(answer1,answer2)){
                return;
            }
            for (int i = 0; i < list.size(); i++) {

                MyLine myLine = list.get(i);
                if (myLine.answer.contains(String.valueOf(answer1))
                        || myLine.answer.contains(String.valueOf(answer2))) {
                    list.set(
                            i,
                            new MyLine(getPoint(answer1), getPoint(answer2),
                                    String.valueOf(answer1)
                                            + String.valueOf(answer2)));
                }
            }
        }

        invalidate();
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