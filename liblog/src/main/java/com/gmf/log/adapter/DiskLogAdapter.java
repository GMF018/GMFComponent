package com.gmf.log.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.core.LoggerPrinter;
import com.gmf.log.consts.LogConsts;
import com.gmf.log.decorator.DiskFormatDecorator;
import com.gmf.log.decorator.FormatDecorator;
import com.gmf.log.interfaces.ICommitable;
import com.gmf.log.interfaces.ILifeStyle;

import static com.gmf.log.core.Utils.checkNotNull;

/**
 * This is used to saves log messages to the disk.
 * By default it uses {@link DiskFormatDecorator} to translates text message into CSV format.
 */
public class DiskLogAdapter implements LogAdapter, ICommitable, ILifeStyle {

    @NonNull
    private final FormatDecorator formatDecorator;

    public DiskLogAdapter() {
        formatDecorator = DiskFormatDecorator.newBuilder().diskPath(LogConsts.localDiskPath).build();
    }

    public DiskLogAdapter(@NonNull FormatDecorator formatDecorator) {
        this.formatDecorator = checkNotNull(formatDecorator);
    }

    @Override
    public int tag() {
        return LoggerPrinter.DISK;
    }

    @Override
    public boolean isLoggable(int priority, @Nullable int level) {
        return level == LoggerPrinter.DISK || level == LoggerPrinter.DISK_SERVER;
    }

    @Override
    public void log(int priority, @NonNull String message) {
        formatDecorator.log(transfer(priority), message);
    }
    private int transfer(int priority) {
        if (priority == LoggerPrinter.LOGGER_LEVEL_VERBOSE) {
            return 0;
        }
        return priority;
    }
    @Override
    public void commit() {
        ((ICommitable) formatDecorator).commit();
    }

    @Override
    public void release() {
        if (formatDecorator == null)
            return;
        ((ILifeStyle) formatDecorator).release();
    }
}
