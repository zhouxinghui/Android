package ebag.hd.widget.questions.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import ebag.hd.R;

/**
 * fix EditText lineHeight and cursor effect when set lineSpaceExtra or lineSpaceMult
 * Created by hanks on 16/7/2.
 */
public class CursorEditText extends AppCompatEditText {

    private float mSpacingMult = 1f;
    private float mSpacingAdd = 0f;
    private int cursorColor = Color.RED;
    private int cursorWidth = 6;
    private int cursorHeight = 60;

    private float cursorScale = 1.2f;

    public CursorEditText(Context context) {
        this(context, null);
    }

    public CursorEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public CursorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        cursorColor = getColorAccent(context);
        cursorHeight = (int) (cursorScale * getTextSize());
        cursorWidth = dp2px(2);
        getLineSpacingAddAndLineSpacingMult();
        setTextCursorDrawable();
        listenTextChange();
    }

    private int dp2px(float dpValue){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    private void listenTextChange() {
        addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CursorEditText.super.setLineSpacing(0f, 1f);
                CursorEditText.super.setLineSpacing(mSpacingAdd, mSpacingMult);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingAdd = add;
        mSpacingMult = mult;
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        cursorHeight = (int) (cursorScale * getTextSize());
        setTextCursorDrawable();
        invalidate();
    }

    @SuppressLint("PrivateApi")
    private void setTextCursorDrawable() {
        try {
            Method method = TextView.class.getDeclaredMethod("createEditorIfNeeded");
            method.setAccessible(true);
            method.invoke(this);
            Field field1 = TextView.class.getDeclaredField("mEditor");
            Field field2 = Class.forName("android.widget.Editor").getDeclaredField("mCursorDrawable");
            field1.setAccessible(true);
            field2.setAccessible(true);
            Object arr = field2.get(field1.get(this));
            Array.set(arr, 0, new CursorDrawable(cursorColor, cursorWidth, cursorHeight));
            Array.set(arr, 1, new CursorDrawable(cursorColor, cursorWidth, cursorHeight));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void getLineSpacingAddAndLineSpacingMult() {
        try {
            Field mSpacingAddField = TextView.class.getDeclaredField("mSpacingAdd");
            Field mSpacingMultField = TextView.class.getDeclaredField("mSpacingMult");
            mSpacingAddField.setAccessible(true);
            mSpacingMultField.setAccessible(true);
            mSpacingAdd = mSpacingAddField.getFloat(this);
            mSpacingMult = mSpacingMultField.getFloat(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
        setTextCursorDrawable();
        invalidate();
    }

    public void setCursorHeight(int cursorHeight) {
        this.cursorHeight = cursorHeight;
        setTextCursorDrawable();
        invalidate();
    }

    public void setCursorWidth(int cursorWidth) {
        this.cursorWidth = cursorWidth;
        setTextCursorDrawable();
        invalidate();
    }

    private class CursorDrawable extends ShapeDrawable {
        private int mHeight;

        CursorDrawable(int cursorColor, int cursorWidth, int cursorHeight) {
            mHeight = cursorHeight;
            setDither(false);
            getPaint().setColor(cursorColor);
            setIntrinsicWidth(cursorWidth);
        }

        public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
            super.setBounds(paramInt1, paramInt2, paramInt3, this.mHeight + paramInt2);
        }

    }
}