package ebag.hd.widget.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ebag.core.util.L;
import ebag.hd.R;

/**
 * Created by caoyu on 2017/9/8.
 */

public class KeyBoardView extends RelativeLayout implements KeyboardView.OnKeyboardActionListener {

    private MyKeyboardView mKeyboardView;
    private Context mContext;
    private Keyboard mNumberKeyboard; // 数字键盘
    private Keyboard mLetterKeyboard; // 字母小写键盘
    private Keyboard mChLetterKeyboard; // 中文字母
    private Keyboard mChDiaoKeyboard; // 中文音调
    private Keyboard mM_special_numeric;//数学特殊字符。
    private Keyboard mM_special_numeric_right;//数学特殊字符右边（上移，下移，）。
    private Keyboard m_number_ABC;//

    private LinearLayout mLinearlayou_keyboard;
    private KeyBoardView mKey_board_special_numeric_left;
    private NestedScrollView mNestedScrollView_1;
    private int mScrollY = 0;

    private boolean isUpper = true;   // 是否大写
    private EditText mEditText;

    private int lastKeyboard = 1;
    private int currentKeyboard = 1;
    public final static int eng_keyboard = 1;
    public final static int ch_keyboard = 2;
    public final static int ch_diao_keyboard = 3;
    public final static int number_keyboard = 4;
    public final static int m_special_numeric = 5;
    public final static int m_special_numeric_right = 6;
    public final static int number_keyboard_ABC = 7;
    public final static int letterBig = 10;
    public final static int m_number_chinese = 11;
    private boolean isSystemInput = false;
    public static boolean isboolean;
    private Keyboard mLetterBig;
    private Keyboard mM_number_chinese;

    public KeyBoardView(Context context) {
        super(context);
        init(context);
    }

    public KeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mKeyboardView = (MyKeyboardView) LayoutInflater.from(getContext()).inflate(R.layout.layout_keyboard, null);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(this);
        RelativeLayout.LayoutParams keyLP = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        keyLP.addRule(RelativeLayout.BELOW, R.id.top_image);
        addView(mKeyboardView, keyLP);
    }

    private List<WeakReference<EditText>> editTexts = new ArrayList<>();

    public void bindEditText(
            EditText editText,
            final int keyBoardType,
            final LinearLayout mLinearlayou_keyboard,
            final NestedScrollView mNestedScrollView_1,
            final KeyBoardView mKey_board_special_numeric_left) {
        this.mLinearlayou_keyboard = mLinearlayou_keyboard;
        this.mKey_board_special_numeric_left = mKey_board_special_numeric_left;
        this.mNestedScrollView_1 = mNestedScrollView_1;
        this.mNestedScrollView_1.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mScrollY = scrollY;
                L.INSTANCE.e("mScrollY======"+mScrollY);
                L.INSTANCE.e("oldScrollY======"+oldScrollY);
            }
        });

        WeakReference<EditText> weakReference = new WeakReference<EditText>(editText);
        editTexts.add(weakReference);
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isUpper = true;
                if (!isSystemInput && KeyBoardView.this.getVisibility() == GONE) {
                    KeyBoardView.this.setVisibility(
                            VISIBLE);
                    showKeyboard(keyBoardType);
                }
                mLinearlayou_keyboard.setVisibility(VISIBLE);
                mNestedScrollView_1.setVisibility(VISIBLE);
                if (isboolean) {
                    mLinearlayou_keyboard.setVisibility(GONE);
                } else {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                mKey_board_special_numeric_left.showKeyboard(m_special_numeric);
                return false;
            }
        });
    }

    public void setSystemInput(boolean systemInput) {
        L.INSTANCE.e("keyboard", "" + systemInput);
        isSystemInput = systemInput;
    }

    public void showChineseLetter() {
        showKeyboard(ch_keyboard);
    }

    public void showEngLetter() {
        showKeyboard(eng_keyboard);
    }

    public void showNumber() {
        showKeyboard(number_keyboard);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int sp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    public void showKeyboard(int keyboardType) {
        currentKeyboard = keyboardType;
        switch (keyboardType) {
            case letterBig:
                mLetterBig = new Keyboard(mContext, R.xml.letter_big);
                mKeyboardView.setKeyboard(mLetterBig);
                break;
            case m_special_numeric_right:
                mM_special_numeric_right = new Keyboard(mContext, R.xml.m_special_numeric_right);
                mKeyboardView.setKeyboard(mM_special_numeric_right);
                break;
            case m_special_numeric:
                mM_special_numeric = new Keyboard(mContext, R.xml.m_special_numeric);
                mKeyboardView.setKeyboard(mM_special_numeric);
                break;
            case ch_keyboard:
                mChLetterKeyboard = new Keyboard(mContext, R.xml.m_chinese_letter);
                mKeyboardView.setKeyboard(mChLetterKeyboard);
                break;
            case ch_diao_keyboard:
                mChDiaoKeyboard = new Keyboard(mContext, R.xml.m_phonogram_letter);
                mKeyboardView.setKeyboard(mChDiaoKeyboard);
                break;
            case number_keyboard:
                mNumberKeyboard = new Keyboard(mContext, R.xml.m_number);
                mKeyboardView.setKeyboard(mNumberKeyboard);
                break;
            case number_keyboard_ABC:
                m_number_ABC = new Keyboard(mContext, R.xml.m_number_abc);
                mKeyboardView.setKeyboard(m_number_ABC);
                break;
            case m_number_chinese:
                mM_number_chinese = new Keyboard(mContext, R.xml.m_number_chinese);
                mKeyboardView.setKeyboard(mM_number_chinese);
                break;
            case eng_keyboard:
            default:
                mLetterKeyboard = new Keyboard(mContext, R.xml.m_letter);
                mKeyboardView.setKeyboard(mLetterKeyboard);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

//            if(primaryCode == -9)//中文音标
//                return;
        switch (primaryCode) {
            case Keyboard.KEYCODE_SHIFT://大小写切换
                changeKeyboart();
                return;
            case Keyboard.KEYCODE_MODE_CHANGE:// 数字与字母键盘互换
//                lastKeyboard = currentKeyboard;
                isUpper = true;
                showKeyboard(number_keyboard_ABC);
                return;
            case -11://语文键盘声调
                showKeyboard(ch_diao_keyboard);
                return;
            case -12://语文键盘
                showKeyboard(ch_keyboard);
                return;
            case -17:
                mNestedScrollView_1.setVisibility(VISIBLE);
                mKey_board_special_numeric_left.showKeyboard(m_special_numeric);
                showKeyboard(m_special_numeric_right);
//                showKeyboard(lastKeyboard);
                return;
            case -19:
                showKeyboard(lastKeyboard);
                return;
            case -100://上
                mScrollUp();
                return;
            case -101://下
                mScrollDown();
                return;
            case -102://返回
                mNestedScrollView_1.setVisibility(GONE);
                showKeyboard(number_keyboard);
                return;
            case -103:
                showKeyboard(m_number_chinese);
                return;
            case -104:
                showKeyboard(ch_keyboard);
                return;
        }

        if (!findFocusEdit()) {
            return;
        }

        Editable editable = mEditText.getText();
        int start = mEditText.getSelectionStart();
        switch (primaryCode) {
            case Keyboard.KEYCODE_CANCEL: // cancel
                editable.insert(start, "\n");
                break;
            case Keyboard.KEYCODE_DELETE:// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
                break;
            case -13:
            case -18:
//                setVisibility(View.GONE);
                mLinearlayou_keyboard.setVisibility(GONE);
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
                /*InputMethodManager inputMethodManager =
                        (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mEditText.requestFocus();*/
                break;//系统键盘
            default:// 输入键盘值
                editable.insert(start, Character.toString((char) primaryCode));
        }
    }

    private boolean findFocusEdit() {
        for (WeakReference<EditText> edit : editTexts) {
            if (edit.get() != null && edit.get().isFocused()) {
                mEditText = edit.get();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onText(CharSequence text) {
        if (!findFocusEdit()) {
            return;
        }

        Editable editable = mEditText.getText();
        int start = mEditText.getSelectionStart();
        editable.insert(start, text);
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void changeKeyboart() {
        if (isUpper){
            isUpper = !isUpper;
            showKeyboard(letterBig);
        }else {
            isUpper = !isUpper;
            showKeyboard(eng_keyboard);
        }
    }



    private boolean isLetter(int primaryCode) {
        return isUpperLetter(primaryCode) || isLowerLetter(primaryCode)
                || isPhonogram(primaryCode) || primaryCode == 252;//ü
    }

    /**
     * 音标
     */
    private boolean isPhonogram(int primaryCode) {
        return primaryCode > -27 && primaryCode < -20;
    }

    /**
     * 大写字母
     */
    private boolean isUpperLetter(int primaryCode) {
        return primaryCode > 64 && primaryCode < 91;
    }

    /**
     * 小写字母
     */
    private boolean isLowerLetter(int primaryCode) {
        return primaryCode > 96 && primaryCode < 123;
    }

    /**
     * 判断是否是字母
     */
    private boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }

    public static boolean isSHowKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }

    private void mScrollUp(){
        if (mNestedScrollView_1 != null){
            mNestedScrollView_1.scrollTo(0, 0);
        }
    }

    private void mScrollDown(){
        if (mNestedScrollView_1 != null){
            mNestedScrollView_1.smoothScrollTo(0, mScrollY + mNestedScrollView_1.getHeight() / 2);
        }
    }

    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    protected void hideSystemSofeKeyboard(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }
}
