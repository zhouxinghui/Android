package ebag.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.ChannelReader;
import com.ywl5320.bspatchywl5320.BsPatchYwl5320Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by caoyu on 2017/12/19.
 */

public class PatchUtils {
    /**
     * 复制文件
     * @param fromFile
     * @param toFile
     * <br/>
     * 2016年12月19日  下午3:31:50
     * @throws IOException
     */
    public static boolean copyFile(File fromFile,File toFile){
        FileInputStream ins = null;
        FileOutputStream out = null;
        boolean success = false;
        try {
            ins = new FileInputStream(fromFile);
            out = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int n=0;
            while((n=ins.read(b))!=-1){
                out.write(b, 0, n);
            }
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(ins != null){
                    ins.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return success;
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

    /**
     * get channel
     *
     * @param context context
     * @return channel, null if not fount
     */
    @Nullable
    public static String getChannel(@NonNull final Context context) {
        return getChannel(context, null);
    }

    /**
     * get channel or default
     *
     * @param context context
     * @param defaultChannel default channel
     * @return channel, default if not fount
     */
    @Nullable
    public static String getChannel(@NonNull final Context context, @NonNull final String defaultChannel) {
        final ChannelInfo channelInfo = getChannelInfo(context);
        if (channelInfo == null) {
            return defaultChannel;
        }
        return channelInfo.getChannel();
    }

    /**
     * get channel info (include channle & extraInfo)
     *
     * @param context context
     * @return channel info
     */
    @Nullable
    public static ChannelInfo getChannelInfo(@NonNull final Context context) {
        final String apkPath = getApkPath(context);
        if (TextUtils.isEmpty(apkPath)) {
            return null;
        }
        return ChannelReader.get(new File(apkPath));
    }

    /**
     * get value by key
     *
     * @param context context
     * @param key     the key you store
     * @return value
     */
    @Nullable
    public static String get(@NonNull final Context context, @NonNull final String key) {
        final Map<String, String> channelMap = getChannelInfoMap(context);
        if (channelMap == null) {
            return null;
        }
        return channelMap.get(key);
    }

    /**
     * get all channl info with map
     *
     * @param context context
     * @return map
     */
    @Nullable
    public static Map<String, String> getChannelInfoMap(@NonNull final Context context) {
        final String apkPath = getApkPath(context);
        if (TextUtils.isEmpty(apkPath)) {
            return null;
        }
        return ChannelReader.getMap(new File(apkPath));
    }

    @Nullable
    private static String getApkPath(@NonNull final Context context) {
        String apkPath = null;
        try {
            final ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo == null) {
                return null;
            }
            apkPath = applicationInfo.sourceDir;
        } catch (Throwable e) {
        }
        return apkPath;
    }

    public static int patch(String oldApkPath, String newApkPath, String patchPath){
        return BsPatchYwl5320Util.getInstance().bsPatch(oldApkPath,newApkPath,patchPath);
    }
}
