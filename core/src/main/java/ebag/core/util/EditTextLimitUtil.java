package ebag.core.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liyimin on 2017/6/8.
 */

public class EditTextLimitUtil {
    private static final String MATCH_CN_EN = "^[\\u4E00-\\u9FA5A-Za-z\\s*]+$";
    private static final String MATCH_CN_EN_NUM = "^[\\u4E00-\\u9FA5A-Za-z0-9\\s*]+$";
    private static final String MATCH_PHONE_NUM = "^[0-9]+$";
    private static final String NOT_CHINESE = "^[^\\u4e00-\\u9fa5]";

    /***
     * 限制用户只能输入数字
     * @param editText
     * @param context
     * @param maxLength EditText输入字符最大长度
     */
    public static void inputChineseAndEnglish(EditText editText, Context context, int maxLength) {
        if (editText.getText().length() > 0) {
            limitResult(context, editText, maxLength, MATCH_CN_EN, "请输入汉字或字母");
        }
    }

    /**
     * 限制用户只能输入数字
     */
    public static void inputPhoneNum(EditText editText, Context context, int maxLength) {
        limitResult(context, editText, maxLength, MATCH_PHONE_NUM, "请输入您的手机号码");
    }

    /**
     * 限制用户只能输入汉字、字母、数字
     *
     * @param editText
     * @param context
     * @param maxLength
     */
    public static void forbidIllegalChar(final EditText editText, final Context context, int maxLength) {
        limitResult(context, editText, maxLength, MATCH_CN_EN_NUM, "请输入汉字、字母或数字");
    }

    /**
     * 禁止输入中文
     */
    public static void forbidChineseCharacter(final EditText editText, final Context context, int maxLength) {
        limitResult(context, editText, maxLength, NOT_CHINESE, "禁止输入中文");
    }

    private static void limitResult(final Context context, final EditText editText, final int maxLength,
                                    final String result, final String message) {
        InputFilter[] inputFilters = new InputFilter[3];
        inputFilters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                String speChat = result;
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return null;
                } else {
                    showIllegalCharacterToast(context, message);
                    return "";
                }
            }
        };
        inputFilters[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                int destCount = dest.toString().length()
                        + getChineseCount(dest.toString());
                int sourceCount = source.toString().length()
                        + getChineseCount(source.toString());
                if (destCount + sourceCount > maxLength) {
                    T.INSTANCE.show(context, "超出最大输入范围");
                    return "";

                } else {
                    return source;
                }
            }

            private int getChineseCount(String str) {
                int count = 0;
                Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
                Matcher m = p.matcher(str);
                while (m.find()) {
                    for (int i = 0; i <= m.groupCount(); i++) {
                        count = count + 1;
                    }
                }
                return count;
            }
        };
        //限制用户输入空格
        inputFilters[2] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(inputFilters);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (editText.getText().toString().length() == 0) {
                        canShow = true;
                    } else {
                        canShow = false;
                    }
                } else {
                    canShow = true;
                }

                return false;
            }
        });
    }

    private static boolean canShow = true;

    private static void showIllegalCharacterToast(Context context, String message) {
        if (canShow) {
            T.INSTANCE.show(context, message);
        }
    }

    /***
     * 设置EditText输入字符最大长度
     * @param editText
     * @param maxLength
     */
    public static void setEditMaxLength(EditText editText, int maxLength) {
        InputFilter inputFilter = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(new InputFilter[]{inputFilter});
    }
    /*--------------------------------限制用户输入非法字符End-------------------------------------*/
}
