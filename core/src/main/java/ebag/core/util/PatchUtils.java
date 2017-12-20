package ebag.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;

import java.io.File;

/**
 * Created by unicho on 2017/12/19.
 */

public class PatchUtils {
    static {
        System.loadLibrary("eBagPatch");
    }

    /**
     * 提取当前应用的apk
     * @param context
     * @return
     */
    public static String extract(Context context) {
        context = context.getApplicationContext();
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String apkPath = applicationInfo.sourceDir;
        return apkPath;
    }

    /**
     * 根据路径去安装apk
     * @param context
     * @param apkPath
     */
    public static void install(Context context, String apkPath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    public static native int patch(String oldApkPath, String newApkPath, String patchPath);
}
