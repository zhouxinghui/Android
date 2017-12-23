/**
 * Project Name:exsd-android.
 * Package Name:com.cn.exsd.common.util.
 * Date:2017/2/8 16:45. Copyright (c) 2017, 深圳易享时代网络科技有限公司.
 */

package ebag.core.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;


/**
 * 静态handler，用来避免handler leak；对外部对象弱引用，方便使用
 * <p>
 * 例子:
 * import android.app.Activity;
 * <p>
 * public class MainActivity extends Activity {
 * private static class MyHandler extends HandlerUtil<MainActivity> {
 * <p>
 * public MyHandler(MainActivity activity) {
 * super(activity);
 * }
 *
 * @Override public void handleMessage(MainActivity activity, common_icon_message msg) {
 * if (activity == null) {
 * common_icon_back;
 * }
 * <p>
 * // do something with activity
 * // handle message...
 * }
 * }
 * <p>
 * private Handler mHandler = new MyHandler(MainActivity.this);
 * <p>
 * // ...
 * }
 */

/**
 * @author dell.
 * @ClassName: HandlerUtil.
 * @Description: 静态handler.
 * @Date: 2017/2/8 16:45.
 */
public abstract class HandlerUtil<T> extends Handler {

    private WeakReference<T> referent;

    public HandlerUtil(T object) {
        super();
        referent = new WeakReference<T>(object);
        checkStatic();
    }

    public HandlerUtil(T object, Callback callback) {
        super(callback);
        referent = new WeakReference<T>(object);
        checkStatic();
    }

    public HandlerUtil(T object, Looper looper) {
        super(looper);
        referent = new WeakReference<T>(object);
        checkStatic();
    }

    public HandlerUtil(T object, Looper looper, Callback callback) {
        super(looper, callback);
        referent = new WeakReference<T>(object);
        checkStatic();
    }

    private void checkStatic() {
        Class<?> clazz = getClass();
        if (!Modifier.isStatic(clazz.getModifiers()) && clazz.getName().indexOf('$') > 0) {
            // 非静态的内部类
            throw new RuntimeException("handler not static");
        }
    }

    @Override
    public final void handleMessage(Message msg) {
        T t = referent.get();
        if (null != t) {
            handleMessage(t, msg);
        }
    }

    @Override
    public final void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
    }

    /**
     * @param object handler引用的对象；可能为null
     * @param msg    消息
     */
    public abstract void handleMessage(T object, Message msg);
}
