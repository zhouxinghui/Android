package ebag.hd.widget.keyboard;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by caoyu on 2017/9/4.
 */

public class EditTtext extends AppCompatEditText {
    public EditTtext(Context context) {
        super(context);
        hideSystemSofeKeyboard(this);
    }

    public EditTtext(Context context, AttributeSet attrs) {
        super(context, attrs);
        hideSystemSofeKeyboard(this);
    }

    public EditTtext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hideSystemSofeKeyboard(this);
    }

    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    public void hideSystemSofeKeyboard(EditText editText) {
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
