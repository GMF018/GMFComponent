package com.gmf.log.strategy;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.interfaces.ICommitable;
import com.gmf.log.interfaces.ILifeStyle;
import com.tencent.mars.log.Xlog;

public class DiskLogStrategy implements LogStrategy, ICommitable, ILifeStyle {
    public DiskLogStrategy(String logPath, String fileName) {
        Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppenderModeAsync, "", logPath, fileName, 0, "");
    }

    @Override
    public void log(int level, @Nullable String tag, @NonNull String message) {
        Xlog.log(level, tag, message);
    }

    @Override
    public void commit() {
        Xlog.appenderFlush(false);
    }

    @Override
    public void release() {
        commit();
        Xlog.appenderClose();
    }
}
