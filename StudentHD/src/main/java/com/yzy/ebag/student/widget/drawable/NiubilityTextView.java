package com.yzy.ebag.student.widget.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yzy.ebag.student.R;

/**
 * Created by unicho on 2017/11/24.
 */

public class NiubilityTextView extends AppCompatTextView {

    private Drawable leftDrawable;
    private Drawable rightDrawable;
    private Drawable topDrawable;
    private Drawable bottomDrawable;

    private Drawable leftClickedDrawable;
    private Drawable leftSelectedDrawable;
    private Drawable rightClickedDrawable;
    private Drawable rightSelectedDrawable;
    private Drawable topClickedDrawable;
    private Drawable topSelectedDrawable;
    private Drawable bottomClickedDrawable;
    private Drawable bottomSelectedDrawable;

    private Drawable leftShowDrawable;
    private Drawable rightShowDrawable;
    private Drawable topShowDrawable;
    private Drawable bottomShowDrawable;

    private Drawable l_dc;
    private Drawable r_dc;
    private Drawable t_dc;
    private Drawable b_dc;

    private Boolean selectable = false;

    private int leftDrawableWidth;
    private int leftDrawableHeight;
    private int rightDrawableWidth;
    private int rightDrawableHeight;
    private int topDrawableWidth;
    private int topDrawableHeight;
    private int bottomDrawableWidth;
    private int bottomDrawableHeight;

    private int defaultTextColor;
    private int selectedTextColor;
    private int clickedTextColor;

    private int selectedColor;
    private int clickedColor;

    private int leftSelfColor;
    private int rightSelfColor;
    private int topSelfColor;
    private int bottomSelfColor;

    private boolean isEasySelectedColor = false;
    private boolean isEasyClickedColor = false;

    public NiubilityTextView(Context context) {
        this(context, null);
    }

    public NiubilityTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NiubilityTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        initSize();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);

        leftDrawable = a.getDrawable(R.styleable.DrawableTextView_leftDrawable) == null ?
                null : a.getDrawable(R.styleable.DrawableTextView_leftDrawable).mutate();
        rightDrawable = a.getDrawable(R.styleable.DrawableTextView_rightDrawable) == null ?
                null : a.getDrawable(R.styleable.DrawableTextView_rightDrawable).mutate();
        topDrawable = a.getDrawable(R.styleable.DrawableTextView_topDrawable) == null ?
                null : a.getDrawable(R.styleable.DrawableTextView_topDrawable).mutate();
        bottomDrawable = a.getDrawable(R.styleable.DrawableTextView_bottomDrawable) == null ?
                null : a.getDrawable(R.styleable.DrawableTextView_bottomDrawable).mutate();

        leftClickedDrawable = a.getDrawable(R.styleable.DrawableTextView_leftClickedDrawable);
        leftSelectedDrawable = a.getDrawable(R.styleable.DrawableTextView_leftSelectedDrawable);
        rightClickedDrawable = a.getDrawable(R.styleable.DrawableTextView_rightClickedDrawable);
        rightSelectedDrawable = a.getDrawable(R.styleable.DrawableTextView_rightSelectedDrawable);
        bottomClickedDrawable = a.getDrawable(R.styleable.DrawableTextView_bottomClickedDrawable);
        bottomSelectedDrawable = a.getDrawable(R.styleable.DrawableTextView_bottomSelectedDrawable);
        topClickedDrawable = a.getDrawable(R.styleable.DrawableTextView_topClickedDrawable);
        topSelectedDrawable = a.getDrawable(R.styleable.DrawableTextView_topSelectedDrawable);

        leftDrawableWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableWidth, 0);
        leftDrawableHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableHeight, 0);
        rightDrawableWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableWidth, 0);
        rightDrawableHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableHeight, 0);
        topDrawableWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableWidth, 0);
        topDrawableHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableHeight, 0);
        bottomDrawableWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableWidth, 0);
        bottomDrawableHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableHeight, 0);

        defaultTextColor = getCurrentTextColor();
        selectedTextColor = a.getColor(R.styleable.DrawableTextView_selectedTextColor, defaultTextColor);
        clickedTextColor = a.getColor(R.styleable.DrawableTextView_clickedTextColor, defaultTextColor);

        selectedColor = a.getColor(R.styleable.DrawableTextView_selectedColor, -1);
        clickedColor = a.getColor(R.styleable.DrawableTextView_clickedColor, -1);
        a.recycle();

        reLoad();
    }

    private void reLoad() {
        leftShowDrawable = leftDrawable;
        rightShowDrawable = rightDrawable;
        topShowDrawable = topDrawable;
        bottomShowDrawable = bottomDrawable;

        if (selectedColor != -1) {
            isEasySelectedColor = true;
        }
        if (clickedColor != -1) {
            isEasyClickedColor = true;
        }

        if (isEasySelectedColor || leftSelectedDrawable != null || rightSelectedDrawable != null ||
                topSelectedDrawable != null || bottomSelectedDrawable != null) {
            selectable = true;
        }
    }

    /**
     * 获取纯色图片颜色
     *
     * @param drawable
     * @return
     */
    private int getDrawableColor(Drawable drawable) {
        int bestColor = 0;
        if (drawable != null) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bitmap = bd.getBitmap();
            int pixelColor;
            int x = bitmap.getWidth();
            int y = bitmap.getHeight();
            for (int i = x / 2; i > 0; i--) {
                for (int j = y / 2; j > 0; j--) {
                    pixelColor = bitmap.getPixel(i, j);
                    int A = Color.alpha(pixelColor);
                    if (A != 0 && A > Color.alpha(bestColor)) {
                        bestColor = pixelColor;
                    }
                    if (A == 255) {
                        return bestColor;
                    }
                }
            }
            for (int i = x / 2; i < x; i++) {
                for (int j = y / 2; j < y; j++) {
                    pixelColor = bitmap.getPixel(i, j);
                    int A = Color.alpha(pixelColor);
                    if (A != 0 && A > Color.alpha(bestColor)) {
                        bestColor = pixelColor;
                    }
                    if (A == 255) {
                        return bestColor;
                    }
                }
            }
        }
        return bestColor;
    }

    private void initSize() {
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawableWidth, leftDrawableHeight);
            leftSelfColor = getDrawableColor(leftDrawable);
            l_dc = DrawableCompat.wrap(leftDrawable);
            DrawableCompat.setTintList(l_dc, new ColorStateList(
                    new int[][]{{android.R.attr.state_selected}, {android.R.attr.state_pressed}, {}},
                    new int[]{selectedColor == -1 ? leftSelfColor : selectedColor,
                            clickedColor == -1 ? leftSelfColor : clickedColor,
                            leftSelfColor}));
        }
        if (leftClickedDrawable != null) {
            leftClickedDrawable.setBounds(0, 0, leftDrawableWidth, leftDrawableHeight);
        }
        if (leftSelectedDrawable != null) {
            leftSelectedDrawable.setBounds(0, 0, leftDrawableWidth, leftDrawableHeight);
        }
        if (leftShowDrawable != null) {
            leftShowDrawable.setBounds(0, 0, leftDrawableWidth, leftDrawableHeight);
        }

        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, rightDrawableWidth, rightDrawableHeight);
            rightSelfColor = getDrawableColor(rightDrawable);
            r_dc = DrawableCompat.wrap(rightDrawable);
            DrawableCompat.setTintList(r_dc, new ColorStateList(
                    new int[][]{{android.R.attr.state_selected}, {android.R.attr.state_pressed}, {}},
                    new int[]{selectedColor == -1 ? rightSelfColor : selectedColor,
                            clickedColor == -1 ? rightSelfColor : clickedColor,
                            rightSelfColor}));
        }
        if (rightClickedDrawable != null) {
            rightClickedDrawable.setBounds(0, 0, rightDrawableWidth, rightDrawableHeight);
        }
        if (rightSelectedDrawable != null) {
            rightSelectedDrawable.setBounds(0, 0, rightDrawableWidth, rightDrawableHeight);
        }
        if (rightShowDrawable != null) {
            rightShowDrawable.setBounds(0, 0, rightDrawableWidth, rightDrawableHeight);
        }

        if (topDrawable != null) {
            topDrawable.setBounds(0, 0, topDrawableWidth, topDrawableHeight);
            topSelfColor = getDrawableColor(topDrawable);
            t_dc = DrawableCompat.wrap(topDrawable);
            DrawableCompat.setTintList(t_dc, new ColorStateList(
                    new int[][]{{android.R.attr.state_selected}, {android.R.attr.state_pressed}, {}},
                    new int[]{selectedColor == -1 ? topSelfColor : selectedColor,
                            clickedColor == -1 ? topSelfColor : clickedColor,
                            topSelfColor}));
        }
        if (topClickedDrawable != null) {
            topClickedDrawable.setBounds(0, 0, topDrawableWidth, topDrawableHeight);
        }
        if (topSelectedDrawable != null) {
            topSelectedDrawable.setBounds(0, 0, topDrawableWidth, topDrawableHeight);
        }
        if (topShowDrawable != null) {
            topShowDrawable.setBounds(0, 0, topDrawableWidth, topDrawableHeight);
        }

        if (bottomDrawable != null) {
            bottomDrawable.setBounds(0, 0, bottomDrawableWidth, bottomDrawableHeight);
            bottomSelfColor = getDrawableColor(bottomDrawable);
            b_dc = DrawableCompat.wrap(bottomDrawable);
            DrawableCompat.setTintList(b_dc, new ColorStateList(
                    new int[][]{{android.R.attr.state_selected}, {android.R.attr.state_pressed}, {}},
                    new int[]{selectedColor == -1 ? bottomSelfColor : selectedColor,
                            clickedColor == -1 ? bottomSelfColor : clickedColor,
                            bottomSelfColor}));
        }
        if (bottomClickedDrawable != null) {
            bottomClickedDrawable.setBounds(0, 0, bottomDrawableWidth, bottomDrawableHeight);
        }
        if (bottomSelectedDrawable != null) {
            bottomSelectedDrawable.setBounds(0, 0, bottomDrawableWidth, bottomDrawableHeight);
        }
        if (bottomShowDrawable != null) {
            bottomShowDrawable.setBounds(0, 0, bottomDrawableWidth, bottomDrawableHeight);
        }

        if (isEasyClickedColor || isEasySelectedColor) {
            leftShowDrawable = l_dc;
            rightShowDrawable = r_dc;
            topShowDrawable = t_dc;
            bottomShowDrawable = b_dc;
        }
        draw();
    }

    private void draw() {
        setCompoundDrawables(this.leftShowDrawable,
                this.topShowDrawable,
                this.rightShowDrawable,
                this.bottomShowDrawable);
        invalidate();
    }

    public void setLeftDrawable(Drawable drawable) {
        this.leftDrawable = drawable;
        reLoad();
        initSize();
    }

    public void setRightDrawable(Drawable drawable) {
        this.rightDrawable = drawable;
        reLoad();
        initSize();
    }

    public void setTopDrawable(Drawable drawable) {
        this.topDrawable = drawable;
        reLoad();
        initSize();
    }

    public void setBottomDrawable(Drawable drawable) {
        this.bottomDrawable = drawable;
        reLoad();
        initSize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isClickable = isClickable();
        if (!isClickable) {
            return isClickable;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    change();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isTouchPointInView(this, (int) event.getRawX(), (int) event.getRawY())) {
                        resume();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    resume();
                    break;
            }
            return super.onTouchEvent(event);
        }
    }

    public void setSelected(boolean selected) {
        if (selectable) {
            if (selected) {
                if (isEasySelectedColor) {
                    setTextColor(selectedColor);
                    if (leftDrawable != null) {
                        super.setSelected(true);
                    }
                } else if (leftSelectedDrawable != null && leftDrawable != null) {
                    setLeftDrawable(leftSelectedDrawable);
                    setTextColor(selectedTextColor);
                }
                if (isEasySelectedColor) {
                    setTextColor(selectedColor);
                    if (rightDrawable != null) {
                        super.setSelected(true);
                    }
                } else if (rightSelectedDrawable != null && rightDrawable != null) {
                    setRightDrawable(rightSelectedDrawable);
                    setTextColor(selectedTextColor);
                }
                if (isEasySelectedColor) {
                    setTextColor(selectedColor);
                    if (bottomDrawable != null) {
                        super.setSelected(true);
                    }
                } else if (bottomSelectedDrawable != null && bottomDrawable != null) {
                    setBottomDrawable(bottomSelectedDrawable);
                    setTextColor(selectedTextColor);
                }
                if (isEasySelectedColor) {
                    setTextColor(selectedColor);
                    if (topDrawable != null) {
                        super.setSelected(true);
                    }
                } else if (topSelectedDrawable != null && topDrawable != null) {
                    setTopDrawable(topSelectedDrawable);
                    setTextColor(selectedTextColor);
                }
            } else {
                if (isEasySelectedColor && leftDrawable != null) {
                    super.setSelected(false);
                } else if (leftSelectedDrawable != null && leftDrawable != null) {
                    setLeftDrawable(leftDrawable);
                }
                if (isEasySelectedColor && rightDrawable != null) {
                    super.setSelected(false);
                } else if (rightSelectedDrawable != null && rightDrawable != null) {
                    setRightDrawable(rightDrawable);
                }
                if (isEasySelectedColor && bottomDrawable != null) {
                    super.setSelected(false);
                } else if (bottomSelectedDrawable != null && bottomDrawable != null) {
                    setBottomDrawable(bottomDrawable);
                }
                if (isEasySelectedColor && topDrawable != null) {
                    super.setSelected(false);
                } else if (topSelectedDrawable != null && topDrawable != null) {
                    setTopDrawable(topDrawable);
                }
                setTextColor(defaultTextColor);
            }
        }
    }

    private void change() {
        if (!selectable) {
            if (isEasyClickedColor && leftDrawable != null) {
                setTextColor(clickedColor);
            } else if (leftClickedDrawable != null && leftDrawable != null) {
                setLeftDrawable(leftClickedDrawable);
                setTextColor(clickedTextColor);
            }
            if (isEasyClickedColor && rightDrawable != null) {
                setTextColor(clickedColor);
            } else if (rightClickedDrawable != null && rightDrawable != null) {
                setRightDrawable(rightClickedDrawable);
                setTextColor(clickedTextColor);
            }
            if (isEasyClickedColor && topDrawable != null) {
                setTextColor(clickedColor);
            } else if (topClickedDrawable != null && topDrawable != null) {
                setTopDrawable(topClickedDrawable);
                setTextColor(clickedTextColor);
            }
            if (isEasyClickedColor && bottomDrawable != null) {
                setTextColor(clickedColor);
            } else if (bottomClickedDrawable != null && bottomDrawable != null) {
                setBottomDrawable(bottomClickedDrawable);
                setTextColor(clickedTextColor);
            }
        }
    }

    private void resume() {
        if (!selectable) {
            if (leftClickedDrawable != null && !isEasyClickedColor && leftDrawable != null) {
                setLeftDrawable(leftDrawable);
            }
            if (rightClickedDrawable != null && !isEasyClickedColor && rightDrawable != null) {
                setRightDrawable(rightDrawable);
            }
            if (topClickedDrawable != null && !isEasyClickedColor && topDrawable != null) {
                setTopDrawable(topDrawable);
            }
            if (bottomClickedDrawable != null && !isEasyClickedColor && bottomDrawable != null) {
                setBottomDrawable(bottomDrawable);
            }
            setTextColor(defaultTextColor);
        }
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

}
