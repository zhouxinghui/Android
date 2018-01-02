package ebag.core.util;

import java.util.Arrays;

/**
 * Created by YZY on 2018/1/2.
 */

public class ArrayUtil {
    public static boolean containsSame(Object[] array1, Object[] array2){
        Arrays.sort(array1);
        Arrays.sort(array2);
        return Arrays.equals(array1, array2);
    }
}
