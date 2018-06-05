package com.zdp.sharding.jdbc;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/4
 * Time 下午5:49
 */
public class DBNameHolder {
    private static ThreadLocal<String> HOLDER = new ThreadLocal<>();
    public static void set(String databaseGroupName) {
        HOLDER.set(databaseGroupName);
    }

    public static String get() {
        return HOLDER.get();
    }
}
