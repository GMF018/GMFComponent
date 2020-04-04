package com.gmf.log.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provides a common interface to emits logs through. This is a required contract for Logger.
 *
 * @see AndroidLogAdapter
 * @see DiskLogAdapter
 */
public interface LogAdapter {
    /**
     * 区分不同的log 类型
     * @return
     */
    int tag();
    /**
     * Used to determine whether log should be printed out or not.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param level    is log local console
     * @return is used to determine if log should printed.
     * If it is true, it will be printed, otherwise it'll be ignored.
     */
    boolean isLoggable(int priority, @Nullable int level);

    /**
     * Each log will use this pipeline
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param message  is the given message for the log message.
     */
    void log(int priority, @NonNull String message);

}