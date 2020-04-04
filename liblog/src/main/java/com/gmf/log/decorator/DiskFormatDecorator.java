package com.gmf.log.decorator;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.core.Utils;
import com.gmf.log.consts.LogConsts;
import com.gmf.log.interfaces.ICommitable;
import com.gmf.log.interfaces.ILifeStyle;
import com.gmf.log.strategy.DiskLogStrategy;
import com.gmf.log.strategy.LogStrategy;

import static com.gmf.log.core.Utils.checkNotNull;

/**
 * CSV formatted file logging for Android.
 * Writes to CSV the following data:
 * epoch timestamp, ISO8601 timestamp (human-readable), log level, tag, log message.
 */
public class DiskFormatDecorator extends AbstactFormatDecorator implements ICommitable, ILifeStyle {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = ",";
    private static final int MIN_STACK_OFFSET = 5;
    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;

    private DiskFormatDecorator(@NonNull Builder builder) {
        checkNotNull(builder);
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @NonNull String message) {
        checkNotNull(message);
        String detailedMessage = getDetailedMessage(message);
        logStrategy.log(priority, tag, detailedMessage);
    }

    private String getDetailedMessage(@NonNull String message) {
        if (message.contains(LogConsts.CRASH_PREFIX)) {
            return message.replace(LogConsts.CRASH_PREFIX, "");
        }
        StackTraceElement stackTraceElement = getStackTraceElement();
        StringBuilder builder = new StringBuilder();
        if (stackTraceElement != null) {
            builder.append(getSimpleClassName(stackTraceElement.getClassName()))
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
        builder.append(message);
        return builder.toString();
    }

    @Override
    public void commit() {
        ((ICommitable) logStrategy).commit();
    }


    private String getSimpleClassName(@NonNull String name) {
        checkNotNull(name);
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    @Nullable
    private String formatTag(@Nullable String tag) {
        if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static final class Builder {
        LogStrategy logStrategy;
        String tag = "tflog";
        String diskPath;
        String fileName = "tflog";

        private Builder() {
        }

        @NonNull
        public Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        public Builder diskPath(@Nullable String diskPath) {
            this.diskPath = diskPath;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull
        public DiskFormatDecorator build() {
            if (logStrategy == null) {
                logStrategy = new DiskLogStrategy(this.diskPath, fileName);
            }
            return new DiskFormatDecorator(this);
        }
    }

    @Override
    public void release() {
        if (logStrategy == null) {
            return;
        }
        ((ILifeStyle) logStrategy).release();
    }

}
