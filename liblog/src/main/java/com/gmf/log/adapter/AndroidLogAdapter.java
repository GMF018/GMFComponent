package com.gmf.log.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.BuildConfig;
import com.gmf.log.core.LoggerPrinter;
import com.gmf.log.decorator.FormatDecorator;
import com.gmf.log.decorator.PrettyFormatDecorator;

import static com.gmf.log.core.LoggerPrinter.*;
import static com.gmf.log.core.Utils.checkNotNull;

/**
 * Android terminal log output implementation for {@link LogAdapter}.
 * <p>
 * Prints output to LogCat with pretty borders.
 *
 * <pre>
 *  ┌──────────────────────────
 *  │ Method stack history
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Log message
 *  └──────────────────────────
 * </pre>
 */
public class AndroidLogAdapter implements LogAdapter {
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int ASSERT = 7;
    @NonNull
    private final FormatDecorator formatDecorator;

    public AndroidLogAdapter() {
        this.formatDecorator = PrettyFormatDecorator.newBuilder().build();
    }

    public AndroidLogAdapter(@NonNull FormatDecorator formatDecorator) {
        this.formatDecorator = checkNotNull(formatDecorator);
    }

    @Override
    public int tag() {
        return LoggerPrinter.CONSOLE;
    }

    @Override
    public boolean isLoggable(int priority, @Nullable int level) {
        return BuildConfig.DEBUG;
    }

    @Override
    public void log(int priority, @NonNull String message) {
        formatDecorator.log(transfer(priority), message);
    }

    /**
     * 与Android 控制台对应
     *
     * @param priority
     * @return
     */
    private int transfer(int priority) {
        switch (priority) {
            case LOGGER_LEVEL_DEBUG:
                return DEBUG;
            case LOGGER_LEVEL_INFO:
                return INFO;
            case LOGGER_LEVEL_WARN:
                return WARN;
            case LOGGER_LEVEL_ERROR:
                return ERROR;
            case LOGGER_LEVEL_VERBOSE:
                return VERBOSE;
            case LOGGER_LEVEL_ASSERT:
                return ASSERT;
        }
        return INFO;
    }

}
