package com.leyou.threadLoads;

/**
 * @author Nie ZongXin
 * @date 2019/9/21 22:14
 */
public class UserHolder {
    private static final ThreadLocal<Long> TL = new ThreadLocal<>();

    public static void setUser(Long id) {
        TL.set(id);
    }

    public static Long getUser() {
        return TL.get();
    }

    public static void remove() {
        TL.remove();
    }
}
