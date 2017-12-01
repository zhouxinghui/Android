package ebag.core.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by unicho on 17-9-23.
 */

public class IOUtil {

    public static void closeAll(Closeable... closeables){
        if(closeables == null){
            return;
        }
        for (Closeable closeable : closeables) {
            if(closeable!=null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
