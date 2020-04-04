package com.gmf.log;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.adapter.AndroidLogAdapter;
import com.gmf.log.adapter.DiskLogAdapter;
import com.gmf.log.adapter.LogAdapter;
import com.gmf.log.core.CrashHandler;
import com.gmf.log.core.Logger;
import com.gmf.log.core.LoggerPrinter;


/**
 * 日志打印
 * gmf
 */
public class GMFLogger {
    private static boolean hasInit = false;
//    /**
//     * 控制台
//     */
//    public static final int CONSOLE = LoggerPrinter.CONSOLE;
    /**
     * 磁盘
     */
    public static final int DISK = LoggerPrinter.DISK;

    /**
     * 服务器
     */
    public static final int SERVER = LoggerPrinter.SERVER;
    /**
     * 磁盘服务器
     */
    public static final int DISK_SERVER = LoggerPrinter.DISK_SERVER;

    private GMFLogger() {
    }

    public static void init(Context context) {
        if (hasInit) {
            return;
        }
        hasInit = true;
        Logger.init(context);
        Logger.addLogAdapter(new DiskLogAdapter());//默认开启
        CrashHandler.getInstance().register();
    }

    /**
     * 默认之初始化一次
     * 初始化发送服务端的
     *
     * @param
     */
    public static void addLogAdapter(LogAdapter logAdapter) {
        Logger.addLogAdapter(logAdapter);

    }

    /**
     * 设置是否开启日志
     *
     * @param enable
     */
    public static void setConsoleEnable(boolean enable) {
        if (enable)
            Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 是否开启磁盘大盘，默认添加
     *
     * @param enable
     */
    public static void setDiskLogEnable(boolean enable) {
        if (enable)
            Logger.addLogAdapter(new DiskLogAdapter());
        else {
            Logger.removeLogAdapter(DISK);
        }
    }


    /**
     * 设置本地磁盘路径
     *
     * @param path
     */
    public void setDiskLogDefaultPath(String path) {
        Logger.setDislDefaultPath(path);
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(@Nullable int level, int priority, @Nullable String message, @Nullable Throwable throwable) {
        Logger.log(level, priority, message, throwable);
    }

    public static void d(@Nullable Object object) {
        Logger.d(object);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        Logger.d(message, args);
    }

    public static void d(@NonNull int level, String message, @Nullable Object... args) {
        Logger.d(level, message, args);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        Logger.e(level, message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        Logger.e(throwable, message, args);
    }


    public static void i(@NonNull String message, @Nullable Object... args) {
        Logger.i(message, args);
    }

    public static void i(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        Logger.i(level, message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        Logger.v(message, args);
    }

    public static void v(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        Logger.v(level, message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        Logger.w(message, args);
    }

    public static void w(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        Logger.w(level, message, args);
    }


    public static void json(@Nullable String json) {
        Logger.json(json);
    }

    public static void xml(@Nullable String xml) {
        Logger.xml(xml);
    }

    public static void commit() {
        Logger.commit();
    }

    public static void release() {
        hasInit = false;
        CrashHandler.getInstance().unregister();
        Logger.release();
    }
}
