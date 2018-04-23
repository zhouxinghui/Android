package ebag.mobile.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import ebag.mobile.R;


/**
 * @author hiphonezhu@gmail.com
 * @version [ViewDragHelperDemo, 16/10/25 15:23]
 */

public class ReaderDragLayout extends ConstraintLayout {
    ScrollView topView;
    TextView dragBtn;
    FrameLayout bottomView;

    int dragBtnHeight;
    private OnBottomHiddenChange onBottomHiddenChange;

    public ReaderDragLayout(Context context) {
        super(context);
        init();
    }

    public ReaderDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReaderDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ViewDragHelper dragHelper;
    final int MIN_TOP = 100; // 距离顶部最小的距离
    void init()
    {
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == dragBtn; // 只处理dragBtn
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top > getHeight() - dragBtnHeight) // 底部边界
                {
                    top = getHeight() - dragBtnHeight;
                }
                else if (top < MIN_TOP) // 顶部边界
                {
                    top = MIN_TOP;
                }
                return top;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                // 改变底部区域高度
                LayoutParams bottomViewLayoutParams = (LayoutParams)bottomView.getLayoutParams();
                bottomViewLayoutParams.height = bottomViewLayoutParams.height + dy * -1;
                bottomView.setLayoutParams(bottomViewLayoutParams);
                if (onBottomHiddenChange != null){
                    onBottomHiddenChange.onHiddenChange(bottomViewLayoutParams.height != 0);
                }
                // 改变顶部区域高度
                /*LayoutParams topViewLayoutParams = (ConstraintLayout.LayoutParams)topView.getLayoutParams();
                topViewLayoutParams.height = topViewLayoutParams.height + dy;
                topView.setLayoutParams(topViewLayoutParams);*/
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

//        topView = (ScrollView)findViewById(R.id.topView);
        dragBtn = findViewById(R.id.dragView);
        bottomView = findViewById(R.id.noteLayout);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dragBtnHeight = dragBtn.getMeasuredHeight();
    }

    public void setOnBottomHiddenChange(OnBottomHiddenChange onBottomHiddenChange){
        this.onBottomHiddenChange = onBottomHiddenChange;
    }

    public interface OnBottomHiddenChange {
        void onHiddenChange(boolean isShow);
    }
}