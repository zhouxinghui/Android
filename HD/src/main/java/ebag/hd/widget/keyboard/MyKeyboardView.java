package ebag.hd.widget.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;
import java.util.List;

import ebag.hd.R;

/**
 * Created by caoyu on 2017/9/4.
 */

public class MyKeyboardView extends KeyboardView {
    private Rect rect;
    private Paint paint;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSKeyboardView();
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSKeyboardView();
    }

    /**
     * 初始化画笔等
     */
    private void initSKeyboardView() {
        rect = new Rect(0,0,0,0);
        Field field;

        try {
            field = KeyboardView.class.getDeclaredField("mKeyBackground");
            field.setAccessible(true);
            Drawable mKeyBackground = (Drawable) field.get(this);
            mKeyBackground.getPadding(rect);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        paint = new Paint();
        paint.setTextSize(70);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getKeyboard() == null) {
            return;
        }
        drawKeyboard(getKeyboard().getKeys(),canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return super.onTouchEvent(me);
    }

    /**
     * 绘制英文键盘
     *
     * @param keys
     * @param canvas
     */
    private void drawKeyboard(List<Keyboard.Key> keys, Canvas canvas) {
        for (Keyboard.Key key : keys) {
            switch (key.codes[0]){
                case -5:drawKeyBackground(R.drawable.btn_delete, canvas, key);break;//删除键
                case -1:drawKeyBackground(R.drawable.btn_a_capital, canvas, key);break;//大小写切换
                case -2:drawKeyBackground(R.drawable.btn_digital, canvas, key);break;//数字字母切换
                case 58:drawKeyBackground(R.drawable.btn_maohao, canvas, key);break;//冒号
                case 44:drawKeyBackground(R.drawable.btn_comma, canvas, key);break;//逗号
                case 32:drawKeyBackground(R.drawable.btn_space_bar, canvas, key);break;//空格
                case 46:drawKeyBackground(R.drawable.btn_an_end, canvas, key);break;//点
                case 39:drawKeyBackground(R.drawable.btn_shengyinhao, canvas, key);break;//‘
//                case -3:drawKeyBackground(R.drawable.btn_enter_key, canvas, key);break;//换行
                case -11:drawKeyBackground(R.drawable.btn_tone, canvas, key);break;//声调
                case -12:drawKeyBackground(R.drawable.btn_return, canvas, key);break;//返回

                case 43:drawKeyBackground(R.drawable.btn_plus, canvas, key);break;//+
                case 45:drawKeyBackground(R.drawable.btn_minus_sign, canvas, key);break;//-
                case -14:drawKeyBackground(R.drawable.btn_multiplied, canvas, key);break;//*
                case -15:drawKeyBackground(R.drawable.btn_divided_by, canvas, key);break;//÷
                case -13:drawKeyBackground(R.drawable.btn_system, canvas, key);break;//系统键盘
                case -18:drawKeyBackground(R.drawable.btn_system2, canvas, key);break;//系统键盘
                case -19:drawKeyBackground(R.drawable.btn_english, canvas, key);break;//切换英文

                case 48:drawKeyBackground(R.drawable.btn_zero, canvas, key);break;//换行
                case 61:drawKeyBackground(R.drawable.btn_equal, canvas, key);break;//声调
                case 60:drawKeyBackground(R.drawable.btn_less_than, canvas, key);break;//<
                case 62:drawKeyBackground(R.drawable.btn_more_than, canvas, key);break;//>
                case -16:drawKeyBackground(R.drawable.btn_point, canvas, key);break;//.
//                case -17:drawKeyBackground(R.drawable.btn_english, canvas, key);break;//切换英文
                case -17:drawKeyBackground(R.drawable.btn_english1, canvas, key);break;//特殊符号
                case -100:drawKeyBackground(R.drawable.icon_on_normal, canvas, key);break;//特殊符号
                case -101:drawKeyBackground(R.drawable.icon_lower_normal, canvas, key);break;//特殊符号
//                case -21:drawKeyBackground(R.drawable.icon_lower_normal, canvas, key);break;//特殊符号
                case -102:drawKeyBackground(R.drawable.btn_return, canvas, key);break;//特殊符号
                case -103:drawKeyBackground(R.drawable.btn_digital, canvas, key);break;
                case -104:drawKeyBackground(R.drawable.btn_return, canvas, key);break;
                //音标
                case -21:drawKeyText(canvas, key, Color.parseColor("#e08574"));break;//ā
                case -22:drawKeyText(canvas, key, Color.parseColor("#d29964"));break;//ō
                case -23:drawKeyText(canvas, key, Color.parseColor("#9ac160"));break;//ē
                case -24:drawKeyText(canvas, key, Color.parseColor("#82bb99"));break;//ī
                case -25:drawKeyText(canvas, key, Color.parseColor("#66a2ca"));break;//ū
                case -26:drawKeyText(canvas, key, Color.parseColor("#8092c9"));break;//ǖ
                //数字
                case 49:drawKeyText(canvas, key, Color.parseColor("#e08574"));break;//1
                case 50:drawKeyText(canvas, key, Color.parseColor("#cb9b68"));break;//2
                case 51:drawKeyText(canvas, key, Color.parseColor("#dcc348"));break;//3
                case 52:drawKeyText(canvas, key, Color.parseColor("#7e91c8"));break;//4
                case 53:drawKeyText(canvas, key, Color.parseColor("#cc88b1"));break;//5
                case 54:drawKeyText(canvas, key, Color.parseColor("#a4c466"));break;//6
                case 55:drawKeyText(canvas, key, Color.parseColor("#8092c8"));break;//7
                case 56:drawKeyText(canvas, key, Color.parseColor("#66a2c9"));break;//8
                case 57:drawKeyText(canvas, key, Color.parseColor("#76bec4"));break;//9
                //字母
                case 113: case 97: case 122: case 81: case 65: case 90://q a z
                    drawKeyText(canvas, key, Color.parseColor("#e08574"));
                    break;
                case 119: case 115: case 120: case 87: case 83: case 88://wsx
                    drawKeyText(canvas, key, Color.parseColor("#D29964"));
                    break;
                case 101: case 100: case 99: case 69: case 68: case 67://edc
                    drawKeyText(canvas, key, Color.parseColor("#D7c04f"));
                    break;
                case 114: case 102: case 118: case 82: case 70: case 86: case 252://rfvü
                    drawKeyText(canvas, key, Color.parseColor("#9ac160"));
                    break;
                case 112:case 108:case 109:case 80: case 76: case 77://plm
                    drawKeyText(canvas, key, Color.parseColor("#cc88b1"));
                    break;
                case 111: case 107: case 110: case 79: case 75: case 78://okn
                    drawKeyText(canvas, key, Color.parseColor("#958ac6"));
                    break;
                case 105: case 106: case 98: case 73: case 74: case 66://ijb
                    drawKeyText(canvas, key, Color.parseColor("#8092c9"));
                    break;
                case 116: case 103: case 84: case 71://tg
                    drawKeyText(canvas, key, Color.parseColor("#82bb99"));
                    break;
                case 121: case 89://y
                    drawKeyText(canvas, key, Color.parseColor("#66a2ca"));
                    break;
                case 117: case 104: case 85: case 72://uh
                    drawKeyText(canvas, key, Color.parseColor("#66a3ca"));
                    break;
                default:
                    drawKeyText(canvas,key, Color.parseColor("#66a3ca"));
                    break;

            }
        }
    }

    /**
     * 绘制键盘key的背景
     *
     * @param drawableId 将要绘制上去的图标
     * @param canvas
     * @param key        需要绘制的键
     */
    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
//        if(key.icon != null){
//            key.icon.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
////            key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2, key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
////                    key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(), key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
//            key.icon.draw(canvas);
//        }else{
            Drawable npd = getResources().getDrawable(drawableId);
            npd.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
            npd.draw(canvas);
//        }
        //给这个view 增加遮罩层 添加点击效果
         npd = getResources().getDrawable(R.drawable.key_delete_selector);
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        npd.draw(canvas);
    }

    /**
     * 绘制字体
     *
     * @param canvas
     * @param key
     */
    private void drawKeyText(Canvas canvas, Keyboard.Key key, int color) {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(color);
        if (key.label != null) {
            String label = key.label.toString();

            Field field;

            if (label.length() > 1 && key.codes.length < 2) {
                int labelTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    labelTextSize = (Integer) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(labelTextSize);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                int keyTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    keyTextSize = (Integer) field.get(this)*2;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(keyTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            }

            canvas.drawText(label,
                    (key.width - rect.left - rect.right) / 2
                            + rect.left + key.x,
                    key.y + (key.height - rect.top - rect.bottom) / 2
                            + (paint.getTextSize() - paint.descent()) / 2 + rect.top,
                    paint);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
