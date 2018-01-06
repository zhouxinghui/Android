package ebag.core.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * Created by YZY on 2017/12/25.
 */

public class FileUtil {
    /**
     * 删除文件 或 文件夹下所有文件（删除之后马上添加同名文件不会失败）
     * @param fileName 文件路径
     */
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        File to = new File(fileName + System.currentTimeMillis());
        file.renameTo(to);//需要重命名之后再删除，防止删除之后马上创建时，出现创建失败的现象
        delete(to);
    }

    /**
     * 删除文件 或 文件夹下所有文件
     * @param file 需要被删除的文件
     */
    private static void delete(File file){
        if (file.isFile()) {
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static String getSerializablePath(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "eBag"
                + File.separator
                + "serializable"
                + File.separator;
        if (!isFileExists(path))
            createDir(path);
        return path;
    }

    /**
     * 录音文件保存路径
     * @return 文件路径
     */
    public static String getRecorderPath(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "eBag"
                + File.separator
                + "recorder"
                + File.separator;
        if (!isFileExists(path))
            createDir(path);
        return path;
    }

    /**
     * 书写作业图片保存路径
     * @return 路径
     */
    public static String getWriteViewItemPath(String id){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "eBag"
                + File.separator
                + "writePic"
                + File.separator
                + id
                + File.separator;
        if (!isFileExists(path))
            createDir(path);
        return path;
    }

    /**
     * 获取书写作业文件
     * @param bagId 书包号
     * @param homeworkId 作业id
     * @param questionId 试题id
     * @return 作业路径集合
     */
    public static List<String> getWriteViewItemFiles(String bagId, String homeworkId, String questionId){
        File file = new File(getWriteViewItemPath(bagId + homeworkId + questionId));
        File[] childFiles = file.listFiles();
        List<String> paths = new ArrayList<>();
        if (childFiles == null || childFiles.length == 0){
            return null;
        }else{
            for (File childFile : childFiles){
                paths.add(childFile.getAbsolutePath());
            }
            return paths;
        }
    }

    public static boolean isFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    public static File createDir(String fullDir) {
        File file = new File(fullDir + File.separator);
        if (!file.exists()) {
            boolean isSucceed = file.mkdirs();
            if (!isSucceed) {
                L.INSTANCE.e("FileUtils: creat  dir failed " + fullDir);
            }
        }
        return file;
    }
}
