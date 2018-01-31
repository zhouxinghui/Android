package ebag.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caoyu
 * @date 2018/1/29
 * @description
 */

public class FoldChartView extends View {

    // 坐标轴 刻度值
    private List<String> xMarkTexts = new ArrayList<>();
    private List<String> yMarkTexts = new ArrayList<>();

    // 曲线数据，可绘制多条曲线 并且为每一条曲线设置不同的颜色
    private List<List<Integer>> points = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();

    private int fullSize = 100;                 // Y轴最高刻度对应的值

    private boolean showValue = true;

    private Paint textPaint;                    // 文字画笔
    private Paint linePaint;                    // 线画笔
    private Paint rectFPaint;                    // 绘制背景

    // 原点坐标
    private int leftX;                              // 原点 在这个View中的 X轴坐标
    private int bottomY;                            // 原点 在这个View中的 Y轴坐标
    private int rightX;                             // 表格的X轴上的终点
    private int topY;                               // 表格的Y轴上的终点

    private int foldLineWidth = 2;                  // 曲线宽度
    private int axisLineWidth = 2;                  // 坐标轴线宽度
    private int tableLineWidth = 1;                 // 表格线宽度

    private int foldLineColor = 0xFF59B29C;         // 默认曲线颜色
    private int axisLineColor = 0xFFA6A6A6;         // 坐标轴颜色
    private int tableLineColor = 0xFFEBEBEB;        // 表格线颜色

    private String xAxisText = "";                  // X轴的特征名
    private String yAxisText = "";                  // Y轴的特征名

    private int xAxisTextSize = 10;                  // X轴特征名文字的大小
    private int xAxisTextColor = 0xFF4A4A4A;         // X轴特征名文字的颜色

    private int yAxisTextSize = 10;                  // Y轴特征名文字的大小
    private int yAxisTextColor = 0xFF4A4A4A;         // Y轴特征名文字的颜色

    private int xMarkTextSize = 10;                  // X轴刻度文字的大小
    private int yMarkTextSize = 10;                  // Y轴刻度文字的大小

    private int xMarkTextColor = 0xFF4A4A4A;         // X轴刻度文字的颜色
    private int yMarkTextColor = 0xFF4A4A4A;         // Y轴刻度文字的颜色

    private int distanceXMark;                  // 每个X轴上刻度间的距离
    private int distanceYMark;                  // 每个Y轴上刻度间的距离

    // set
    private int xDistanceAxisTextLastMarkText = 10;      // X轴特征名和最右一个刻度文字的距离
    private int yDistanceAxisTextLastMarkText = 10;      // Y轴特征名和最高一个刻度文字的距离

    //
    private int xDistanceAxisTextLastMark;      // X轴特征名和最右一个刻度的距离
    private int yDistanceAxisTextLastMark;      // Y轴特征名和最高一个刻度的距离

    private int xDistanceMarkAxis = 10;          // X轴和刻度之间的距离
    private int yDistanceMarkAxis = 10;          // Y轴和刻度之间的距离

    private float xAxisTextHeight;           // X轴特征名文字的高度
    private float yAxisTextHeight;           // Y轴特征名文字的高度

    private float xMarkTextHeight;           // 坐标轴刻度文字的高度

    private int valueTextSize = 10;
    private int valueTextColor = 0xff4a4a4a;
    private int valueBackgroundColor = 0xff59b29c;
    private int valueBackgroundRadius = 10;

    public FoldChartView(Context context) {
        this(context, null);
    }

    public FoldChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * 初始化数据值和画笔
     */
    public void init() {
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
//        PathEffect pathEffect = new CornerPathEffect(25);
//        linePaint.setPathEffect(pathEffect);
    }

    public void show(){
        invalidate();
    }

    public void clear(){
        xMarkTexts.clear();
        yMarkTexts.clear();
        points.clear();
        colors.clear();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureBaseData();
    }

    private void measureBaseData(){
        float yMaxTextWidth = 0;
        textPaint.setTextSize(yMarkTextSize);
        // 计算Y轴 刻度中文字的 最大宽度
        if(yMarkTexts.size() > 0){
            for(String str : yMarkTexts){
                if(str != null){
                    yMaxTextWidth = Math.max(yMaxTextWidth, textPaint.measureText(str));
                }
            }
        }

        // 计算Y轴特征名和最高一个刻度的距离
        textPaint.setTextSize(yMarkTextSize);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        yDistanceAxisTextLastMark = (int) (yDistanceAxisTextLastMarkText + metrics.descent - metrics.ascent);

        // 计算Y轴特征名文字宽度和高度
        float yAxisTextWidth;
        if(yAxisText == null || yAxisText.length() == 0){
            yAxisTextWidth = 0;
            yAxisTextHeight = 0;
        }else{
            textPaint.setTextSize(yAxisTextSize);
            metrics = textPaint.getFontMetrics();
            yAxisTextHeight = metrics.descent - metrics.ascent;
            yAxisTextWidth = textPaint.measureText(yAxisText);
        }

        // 计算X轴刻度的文字高度
        textPaint.setTextSize(xMarkTextSize);
        metrics = textPaint.getFontMetrics();
        xMarkTextHeight = metrics.descent - metrics.ascent;

        // 计算X轴特征名的文字高度和宽度
        float xAxisTextWidth;
        if(xAxisText == null || xAxisText.length() == 0){
            xAxisTextHeight = 0;
            xAxisTextWidth = 0;
        }else{
            textPaint.setTextSize(xAxisTextSize);
            metrics = textPaint.getFontMetrics();
            xAxisTextHeight = metrics.descent - metrics.ascent;
            xAxisTextWidth = textPaint.measureText(xAxisText);
        }

        // 计算X轴特征名和最右一个刻度的距离
        String lastXScaleString = xMarkTexts.get(xMarkTexts.size() - 1);
        if(lastXScaleString != null){
            xDistanceAxisTextLastMark =
                    (int) (xDistanceAxisTextLastMarkText + textPaint.measureText(lastXScaleString)/2);
        }

        // 计算表格四个角的坐标
        // Y轴所有文字的最大宽度
        yMaxTextWidth = Math.max(yMaxTextWidth, yAxisTextWidth);
        // PaddingLeft + Y轴所有文字中的最大文字宽度 + 文字和坐标轴之间的距离
        leftX = (int) (getPaddingLeft() + yMaxTextWidth + yDistanceMarkAxis);
        // X轴所有文字中的最大文字高度
        float maxHeight = Math.max(xMarkTextHeight, xAxisTextHeight);
        // 总高度 - PaddingBottom - X轴所有文字中的最大文字高度 - 文字和坐标轴之间的距离
        bottomY = (int) (this.getHeight() - getPaddingBottom() - maxHeight - xDistanceMarkAxis);

        // 总宽度 - PaddingRight - X轴特征名称的宽度 - 特征名距离最右刻度的距离
        rightX = (int) (this.getWidth() - getPaddingRight() - xAxisTextWidth - xDistanceAxisTextLastMark);
        // paddingTop + Y轴特征名称的高度 + 特征名距离最高刻度的距离
        topY = (int) (getPaddingTop() + yAxisTextHeight + yDistanceAxisTextLastMark);


        distanceXMark = (rightX - leftX) / (xMarkTexts.size() - 1);
        distanceYMark = (bottomY - topY) / (yMarkTexts.size() - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 白色背景
//        canvas.drawColor(0xffffffff);



        drawTable(canvas);
        drawAxisLine(canvas);
        drawAxisText(canvas);
        drawCurves(canvas);
        canvas.restore();
    }

    /**
     * 绘制表格
     */
    private void drawTable(Canvas canvas) {

        linePaint.setColor(tableLineColor);
        linePaint.setStrokeWidth(tableLineWidth);
        // 横向线
        for(int i = 0; i < yMarkTexts.size() - 1; i++){
            int y = topY + i * distanceYMark;
            canvas.drawLine(leftX, y, rightX, y, linePaint);
        }
        // 纵向线
        for (int i = 1; i <= xMarkTexts.size() - 1; i++) {
            int x = leftX + i * distanceXMark;
            canvas.drawLine(x, topY, x, bottomY, linePaint);
        }
    }

    /**
     * 绘制坐标轴
     */
    private void drawAxisLine(Canvas canvas) {
        linePaint.setStrokeWidth(axisLineWidth);
        linePaint.setColor(axisLineColor);
        // X
        canvas.drawLine(leftX, bottomY, rightX + xDistanceAxisTextLastMark, bottomY, linePaint);
        // Y
        canvas.drawLine(leftX, bottomY, leftX, topY - yDistanceAxisTextLastMark, linePaint);
    }

    /**
     * 绘制刻度
     */
    private void drawAxisText(Canvas canvas) {
        textPaint.setColor(xMarkTextColor);
        textPaint.setTextSize(xMarkTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        String name;
        // X轴刻度
        for (int i = 0; i < xMarkTexts.size(); i++) {
            int startX = leftX + i * distanceXMark;
            name = xMarkTexts.get(i);
            if (name != null)
                canvas.drawText(name, startX, bottomY + xMarkTextHeight + xDistanceMarkAxis, textPaint);
        }

        textPaint.setColor(yMarkTextColor);
        textPaint.setTextSize(yMarkTextSize);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        // Y轴刻度
        for (int i = 0; i < yMarkTexts.size(); i++) {
            int startY = bottomY - i * distanceYMark;
            name = yMarkTexts.get(i);
            if (name != null)
                // x默认是字符串的左边在屏幕的位置，y默认是字符串是字符串的baseline在屏幕上的位置
                canvas.drawText(name, leftX - yDistanceMarkAxis, startY, textPaint);
        }

        if(xAxisText != null && xAxisText.length() != 0){
            //X轴特征名
            textPaint.setColor(xAxisTextColor);
            textPaint.setTextSize(xAxisTextSize);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(xAxisText, getWidth(),
                    bottomY + xAxisTextHeight + xDistanceMarkAxis, textPaint);
        }

        if(yAxisText != null && yAxisText.length() != 0){
            // Y轴特征名
            textPaint.setColor(yAxisTextColor);
            textPaint.setTextSize(yAxisTextSize);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(yAxisText, leftX - yDistanceMarkAxis,
                    getPaddingTop() + yAxisTextHeight, textPaint);
        }
    }

    private List<List<RectF>> rectList = new ArrayList<>();

    private void drawCurves(Canvas canvas){

        linePaint.setStrokeWidth(foldLineWidth);

        //记录 绘制记录点 的区域
        rectList.clear();
        for(int i = 0; i < points.size(); i++){
            if(colors.get(i) == null){
                linePaint.setColor(foldLineColor);
            }else{
                linePaint.setColor(colors.get(i));
            }

            if(showValue){
                textPaint.setTextSize(valueTextSize);
                textPaint.setColor(valueTextColor);
                textPaint.setTextAlign(Paint.Align.CENTER);

                List<RectF> lineRectFs = new ArrayList<>();
                rectList.add(lineRectFs);
                drawCurve(canvas, points.get(i), lineRectFs);
            }else{
                drawCurve(canvas, points.get(i), null);
            }

        }

    }

    private void drawCurve(Canvas canvas, List<Integer> data, List<RectF> lineRectFs){
        if(data == null) return;
        Path path = new Path();
        boolean isMove = false;
        int tableHeight = bottomY - topY;

        for (int i = 0; i < xMarkTexts.size(); i++) {
            if(data.get(i) == null)
                continue;
            float x = leftX + i * distanceXMark;
            float y = bottomY - data.get(i) * tableHeight / fullSize;

            if (!isMove) {
                isMove = true;
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            // 绘制数值
            if(lineRectFs != null){
                drawValue(canvas, data.get(i), (int)x, (int)y, lineRectFs);
            }
        }
        canvas.drawPath(path, linePaint);
    }

    private void drawValue(Canvas canvas, int data, int x, int y, List<RectF> lineRectFs){
        RectF rect;

        float width;
        float height ;
        String str = String.valueOf(data);

        Rect text = new Rect();
        textPaint.getTextBounds(str,0,str.length(), text);
        float textHeight = text.height();
        float valueY;
        if(valueDrawable != null) {

            rect = new RectF(
                    x - drawableWidth / 2,
                    y - drawableHeight,
                    x + drawableWidth / 2,
                    y
            );

            valueDrawable.setBounds(
                    x - drawableWidth / 2,
                    y - drawableHeight,
                    x + drawableWidth / 2,
                    y
            );

            valueDrawable.draw(canvas);
            Rect rect1 = new Rect();
            boolean isRect = valueDrawable.getPadding(rect1);
            if(isRect){//判断是不是.9图
                valueY = y - rect1.bottom - (drawableHeight - rect1.bottom - rect1.top - textHeight)/2;
            }else{
                valueY = y - (drawableHeight - textHeight) / 2;
            }
        }else{
            if(rectFPaint == null){
                rectFPaint = new Paint();
                rectFPaint.setStyle(Paint.Style.FILL);
                rectFPaint.setDither(true);
                rectFPaint.setAntiAlias(true);
                rectFPaint.setStrokeWidth(3);
            }
            rectFPaint.setColor(valueBackgroundColor);
            width = textPaint.measureText(String.valueOf(data)) + valuePaddingLeft + valuePaddingRight;
            height = textHeight + valuePaddingTop + valuePaddingBottom;
            valueY = y - valuePaddingBottom;

            rect = new RectF(
                    x - width / 2,
                    y - height,
                    x + width / 2,
                    y
            );
            canvas.drawRoundRect(rect, valueBackgroundRadius, valueBackgroundRadius, rectFPaint);
        }

        canvas.drawText(String.valueOf(data), x, valueY, textPaint);
        lineRectFs.add(rect);
    }

    float downX = 0;
    float downY = 0;
    long pressTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                pressTime = System.currentTimeMillis();
                break;
//            case MotionEvent.ACTION_MOVE:
//                isMoved = true;
//                break;
            case MotionEvent.ACTION_UP:
                if(downX == event.getX() && downY == event.getY() // 抬起的点和按下的点是否一致
                        && System.currentTimeMillis() - pressTime <= 300){//按下的时间小于300毫秒

                    for(int i = 0; i < rectList.size(); i++){
                        if(rectList.get(i) != null)
                            for(int j = 0; j < rectList.get(i).size(); j++){
                                if(rectList.get(i).get(j) != null)
                                    if(rectList.get(i).get(j).contains(downX, downY)){
                                        if(onValueItemClickListener != null){
                                            onValueItemClickListener.valueClick(i,j);
                                        }
                                    }
                            }
                    }
                }
                break;
        }
        return true;
    }

    interface OnValueItemClickListener{
        void valueClick(int lineIndex, int position);
    }

    private OnValueItemClickListener onValueItemClickListener;

    public void setOnValueItemClickListener(OnValueItemClickListener onValueItemClickListener) {
        this.onValueItemClickListener = onValueItemClickListener;
    }

    private Drawable valueDrawable;
    private int drawableWidth;
    private int drawableHeight;

    public void setValueBackground(Drawable drawable, int drawableWidth, int drawableHeight){
        this.drawableHeight = retestDrawableHeight(drawable, drawableHeight);
        this.drawableWidth = retestDrawableWidth(drawable,drawableWidth);
        this.valueDrawable = drawable;
    }

    public void setValueBackground(int color, int radius){
        this.valueBackgroundColor = color;
        this.valueBackgroundRadius = radius;
    }

    private int valuePaddingLeft = 10;
    private int valuePaddingTop = 10;
    private int valuePaddingRight = 10;
    private int valuePaddingBottom = 10;
    public void setValuePadding(int left, int top, int right, int bottom){
        this.valuePaddingLeft = left;
        this.valuePaddingTop = top;
        this.valuePaddingRight = right;
        this.valuePaddingBottom = bottom;
    }


    private final int DEFAULT_SIZE = -0x1;
    /**
     * 重新测量图标的宽度
     * @param drawable  图标
     * @param drawableWidth  图标宽度
     * @return
     */
    private int retestDrawableWidth(Drawable drawable, int drawableWidth){
        if(drawable != null){
            if(drawableWidth == DEFAULT_SIZE) {
                drawableWidth = drawable.getIntrinsicWidth();
            }
            return drawableWidth;
        }
        return 0;
    }

    /**
     * 重新测量图标的高度
     * @param drawable  图标
     * @param drawableHeight  图标高度
     * @return
     */
    private int retestDrawableHeight(Drawable drawable, int drawableHeight){
        if(drawable != null){
            if(drawableHeight == DEFAULT_SIZE) {
                drawableHeight = drawable.getIntrinsicHeight();
            }
            return drawableHeight;
        }
        return 0;
    }

    /**
     * 设置横坐标轴的刻度名 和 坐标轴名字
     * @param xMarkText
     * @param xAxisText
     */
    public void setXAxis(List<String> xMarkText, String xAxisText) {
        if(xMarkText == null) xMarkText = new ArrayList<>();
        this.xMarkTexts = xMarkText;
        this.xAxisText = xAxisText;
    }

    /**
     * 设置纵坐标轴的刻度名 和 坐标轴名字
     * @param yMarkText
     * @param yAxisText
     */
    public void setYAxis(List<String> yMarkText, String yAxisText) {
        if(yMarkText == null) yMarkText = new ArrayList<>();
        this.yMarkTexts = yMarkText;
        this.yAxisText = yAxisText;
    }

    /**
     * 设置这个折线图的 文字大小
     * @param axisTextSize 坐标轴特征名的文字大小
     * @param markTextSize 坐标轴刻度值的文字大小
     */
    public void setTextSize(int markTextSize, int axisTextSize){
        this.setTextSize(markTextSize, axisTextSize, markTextSize, axisTextSize);
    }

    public void setValueTextSize(int valueTextSize){
        this.valueTextSize = valueTextSize;
    }

    public void setValueTextColor(int valueTextColor){
        this.valueTextColor = valueTextColor;
    }

    /**
     * 设置这个折线图的 文字大小
     * @param xAxisTextSize X轴特征名的文字大小
     * @param xMarkTextSize X轴刻度值的文字大小
     * @param yAxisTextSize Y轴特征名的文字大小
     * @param yMarkTextSize Y轴刻度值的文字大小
     */
    public void setTextSize(int xMarkTextSize, int xAxisTextSize, int yMarkTextSize, int yAxisTextSize){
        this.xAxisTextSize = xAxisTextSize;
        this.yAxisTextSize = yAxisTextSize;
        this.xMarkTextSize = xMarkTextSize;
        this.yMarkTextSize = yMarkTextSize;
    }

    /**
     * 设置这个折线图的 文字的颜色
     * @param markTextColor 坐标轴名称的文字颜色
     * @param axisTextColor 坐标轴刻度值的文字颜色
     */
    public void setTextColor(int markTextColor, int axisTextColor){
        this.setTextColor(markTextColor, axisTextColor, markTextColor, axisTextColor);
    }

    /**
     * 设置这个折线图的 文字的颜色
     * @param xMarkTextColor X轴刻度值的文字颜色
     * @param xAxisTextColor X轴名称的文字颜色
     * @param yMarkTextColor Y轴刻度值的文字颜色
     * @param yAxisTextColor Y轴名称的文字颜色
     */
    public void setTextColor(int xMarkTextColor, int xAxisTextColor, int yMarkTextColor, int yAxisTextColor){
        this.xMarkTextColor = xMarkTextColor;
        this.yMarkTextColor = yMarkTextColor;
        this.xAxisTextColor = xAxisTextColor;
        this.yAxisTextColor = yAxisTextColor;
    }

    /**
     * 设置这个折线图 线条的 宽度
     * @param foldLineWidth 折线
     * @param axisLineWidth 坐标轴
     * @param tableLineWidth 表格
     */
    public void setLineWidth(int foldLineWidth, int axisLineWidth, int tableLineWidth){
        this.foldLineWidth = foldLineWidth;
        this.axisLineWidth = axisLineWidth;
        this.tableLineWidth = tableLineWidth;
    }

    /**
     * 设置这个折线图 线条的 颜色
     * @param foldLineColor 折线
     * @param axisLineColor 坐标轴
     * @param tableLineColor 表格
     */
    public void setLineColor(int foldLineColor, int axisLineColor, int tableLineColor){
        this.foldLineColor = foldLineColor;
        this.axisLineColor = axisLineColor;
        this.tableLineColor = tableLineColor;
    }

    /**
     * 坐标轴轴特征名和最边缘一个刻度文字的距离
     * @param xDistance
     * @param yDistance
     */
    public void setDistanceAxisTextLastMarkText(int xDistance, int yDistance) {
        this.xDistanceAxisTextLastMarkText = xDistance;
        this.yDistanceAxisTextLastMarkText = yDistance;
    }

    /**
     * 坐标轴和刻度之间的距离
     * @param xDistance
     * @param yDistance
     */
    public void setDistanceMarkAxis(int xDistance, int yDistance) {
        this.xDistanceMarkAxis = xDistance;
        this.yDistanceMarkAxis = yDistance;
    }

    /**
     * 设置 最大刻度对应的 数值
     * @param fullSize
     */
    public void setFullSize(int fullSize) {
        this.fullSize = fullSize;
    }

    /**
     * 添加一条曲线
     * @param points
     * @param color
     */
    public void addPoints(List<Integer> points, Integer color){
        this.points.add(points);
        this.colors.add(color);
    }

}
