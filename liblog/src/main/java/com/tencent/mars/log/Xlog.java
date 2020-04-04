package com.tencent.mars.log;

import android.os.Looper;
import android.os.Process;

/**
 * 如果你的程序使用了多进程，不要把多个进程的日志输出到同一个文件中，保证每个进程独享一个日志文件。
 * 保存 log 的目录请使用单独的目录，不要存放任何其他文件防止被 xlog 自动清理功能误删。
 * debug 版本下建议把控制台日志打开，日志级别设为 Verbose 或者 Debug, release 版本建议把控制台日志关闭，日志级别使用 Info.
 * cachePath这个参数必传，而且要data下的私有文件目录，例如 /data/data/packagename/files/xlog，
 * mmap文件会放在这个目录，如果传空串，可能会发生 SIGBUS 的crash。
 */
public class Xlog /*implements Log.LogImp */{

    public static final int LEVEL_ALL = 0;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_NONE = 6;

    public static final int AppenderModeAsync = 0;
    public static final int AppenderModeSync = 1;

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    static class XLoggerInfo {
        public int level;
        public String tag;
        public String filename;
        public String funcname;
        public int line;
        public long pid;
        public long tid;
        public long maintid;
    }

    public static void open(boolean isLoadLib, int level, int mode, String cacheDir, String logDir, String nameprefix, String pubkey) {
        appenderOpen(level, mode, cacheDir, logDir, nameprefix, 0, pubkey);
    }


    public static void log(int level, String tag, String message) {
        logWrite2(level, tag, "", "", 0, Process.myPid(), Process.myTid(), Looper.getMainLooper().getThread().getId(), message);

    }

    public static native void logWrite(XLoggerInfo logInfo, String log);

    public static native void logWrite2(int level, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log);

//    @Override
    public native int getLogLevel();

    public static native void setLogLevel(int logLevel);

    public static native void setAppenderMode(int mode);

    public static native void setConsoleLogOpen(boolean isOpen);    //set whether the console prints log

    public static native void setErrLogOpen(boolean isOpen);    //set whether the  prints err log into a separate file

    public static native void appenderOpen(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays, String pubkey);

    public static native void setMaxFileSize(long size);

    /**
     * should be called before appenderOpen to take effect
     *
     * @param duration alive seconds
     */
    public static native void setMaxAliveTime(long duration);


    public static native void appenderClose();


    public static native void appenderFlush(boolean isSync);


}
