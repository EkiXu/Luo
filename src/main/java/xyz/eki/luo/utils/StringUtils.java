package xyz.eki.luo.utils;

public class StringUtils {
    /**
     * 是否为空
     * null或者空字符串都会判定为true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
