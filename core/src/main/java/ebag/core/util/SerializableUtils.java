package ebag.core.util;


import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

/**
 * 序列化工具类
 * Created by lym on 2016/4/9.
 */
public class SerializableUtils {

    /**
     * 序列化数据
     * @param fileName
     * @param t
     */
    public static <T> void setSerializable(String fileName, T t) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(new File(FileUtil.getSerializablePath() +
                md5(fileName)));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            close(oos);
        }
    }

    /**
     * 反序列化数据
     * @param fileName 缓存文件名
     * @param <T>
     * @return
     */
    public static <T> T getSerializable(String fileName) {
        ObjectInputStream oos = null;
        try {
            File file = new File(FileUtil.getSerializablePath() + md5(fileName));
            if (!file.exists()){
                file.createNewFile();
            }

            if (file.length() == 0){
                return null;
            }

            FileInputStream fis = new FileInputStream(file);
            oos = new ObjectInputStream(fis);
            return (T)oos.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(oos);
        }
        return null;
    }
    /**
     * 删除序列化数据
     * @param fileName
     */
    public static boolean deleteSerializable(String fileName){
        File file = new File(FileUtil.getSerializablePath() + md5(fileName));
        if (file.exists()){
            return file.delete();
        }
        return false;
    }

    /**
     * 返回md5后的值
     * @param url
     * @return
     */
    private static String md5(String url) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String t = Integer.toHexString(0xFF & messageDigest[i]);
                if (t.length() == 1) {
                    hexString.append("0" + t);
                } else {
                    hexString.append(t);
                }
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭IO输入流
     * @param closeable
     */
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred", e);
            }
        }
    }

}
