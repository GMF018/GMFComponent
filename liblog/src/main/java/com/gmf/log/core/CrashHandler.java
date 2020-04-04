package com.gmf.log.core;


import androidx.annotation.NonNull;

import com.gmf.log.GMFLogger;
import com.gmf.log.consts.LogConsts;

import static com.gmf.log.core.Utils.checkNotNull;

/**
 * Created by ccy on 2017/12/4.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String PACKAGE_NAME = "talkfun";

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;
    private Throwable currentThrowable;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     */
    public void register() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void unregister() {
        if (mDefaultHandler != null)
            Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
        mDefaultHandler = null;
        INSTANCE = null;
    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        try {
            GMFLogger.e(GMFLogger.DISK, getErrorMessage(ex));
            GMFLogger.commit();
            if (mDefaultHandler != null) {
                //如果用户没有处理则让系统默认的异常处理器来处理
                //RemoteLogUtil.getInstance().e(ex.toString());
                mDefaultHandler.uncaughtException(thread, ex);
            }
        } catch (Exception e) {
        }
    }

    private String getErrorMessage(Throwable ex) {
        StackTraceElement stackTraceElement = parseThrowable(ex);
        StringBuilder builder = new StringBuilder();
        if (stackTraceElement != null) {
            builder.append(LogConsts.CRASH_PREFIX)
                    .append(getSimpleClassName(stackTraceElement.getClassName()))
                    .append(".")
                    .append(stackTraceElement.getMethodName())
                    .append(" ")
                    .append("(")
                    .append(stackTraceElement.getFileName())
                    .append(":")
                    .append(stackTraceElement.getLineNumber())
                    .append(")")
                    .append(" ");
        }
        builder.append(ex.getMessage());
        return builder.toString();
    }

    private StackTraceElement parseThrowable(Throwable ex) {
        if (ex == null || ex.getStackTrace() == null || ex.getStackTrace().length == 0) return null;
        StackTraceElement element;
        for (StackTraceElement ele : ex.getStackTrace()) {
            if (ele.getClassName().contains(PACKAGE_NAME)) {
                element = ele;
                return element;
            }
        }
        element = ex.getStackTrace()[0];
        return element;
    }

    private String getSimpleClassName(@NonNull String name) {
        checkNotNull(name);
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }
}