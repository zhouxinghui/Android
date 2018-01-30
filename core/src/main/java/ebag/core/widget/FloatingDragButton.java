package ebag.core.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FloatingDragButton extends FloatingActionButton implements View.OnTouchListener{
    int lastX, lastY;
    int originX, originY;
    private ArrayList<FloatingActionButton> floatingActionButtons = new ArrayList<FloatingActionButton>();

    public FloatingDragButton(Context context) {
        this(context, null);
    }

    public FloatingDragButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingDragButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }
    //注册归属的FloatingActionButton 
    public void registerButton(FloatingActionButton floatingActionButton){
        floatingActionButtons.add(floatingActionButton);
    }
    public ArrayList<FloatingActionButton> getButtons(){
        return floatingActionButtons;
    }
    public int getButtonSize(){
        return floatingActionButtons.size();
    }
    //是否可拖拽  一旦展开则不允许拖拽
    public boolean isDraftable(){
        for(FloatingActionButton btn:floatingActionButtons){
            if(btn.getVisibility() == View.VISIBLE ){
                return false;
            }
        }
        return true;
    }
    //当被拖拽后其所属的FloatingActionButton 也要改变位置
    private void slideButton(int l, int t, int r, int b){
        for(FloatingActionButton floatingActionButton:floatingActionButtons){
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) floatingActionButton.getLayoutParams();
            lp.setMargins(l, t,r,b);
            floatingActionButton.setLayoutParams(lp);
        }
    }

    int startX;
    int startY;
    int l;
    int b;
    int r;
    int t;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!isDraftable()){
            return false;
        }
        int ea = event.getAction();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        int screenWidth = ((ViewGroup)getParent()).getWidth();
        int screenHeight = ((ViewGroup)getParent()).getHeight();
        switch (ea) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                lastY = (int) event.getRawY();
                originX = lastX;
                originY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                l = v.getLeft() + dx;
                b = v.getBottom() + dy;
                r = v.getRight() + dx;
                t = v.getTop() + dy;
                // 下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0;
                    r = l + v.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + v.getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - v.getWidth();
                }
                if (b > screenHeight) {
                    b = screenHeight;
                    t = b - v.getHeight();
                }
                v.layout(l, t, r, b);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                v.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                int distance = (int) event.getRawX() - originX + (int)event.getRawY() - originY;
                Log.e("DIstance",distance+"");
                if (Math.abs(distance)<20) {
                    //当变化太小的时候什么都不做 OnClick执行
                }else {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) getLayoutParams();
                    int bottomMargin = lp.bottomMargin;
                    int rightMargin = lp.rightMargin;
                    bottomMargin += (startY - y);
                    rightMargin += (startX - x);
                    if (r == screenWidth)
                        rightMargin = 0;
                    if (b == screenHeight)
                        bottomMargin = 0;
                    lp.setMargins(l, t, rightMargin, bottomMargin);
                    slideButton(l, t, rightMargin, bottomMargin);
                    setLayoutParams(lp);
                    return true;
                }
                break;
        }
        return false;
    }
}