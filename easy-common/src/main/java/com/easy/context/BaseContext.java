package com.easy.context;

public class BaseContext {

    // ThreadLocal to hold the current ID
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * Sets the current ID in ThreadLocal
     *
     * @param id The ID to set
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * Retrieves the current ID from ThreadLocal
     *
     * @return The current ID
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * Removes the current ID from ThreadLocal
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
