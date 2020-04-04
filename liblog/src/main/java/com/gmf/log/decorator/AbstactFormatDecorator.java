package com.gmf.log.decorator;

import androidx.annotation.NonNull;

import com.gmf.log.GMFLogger;
import com.gmf.log.core.Logger;
import com.gmf.log.core.LoggerPrinter;

import static com.gmf.log.core.Utils.checkNotNull;

/**
 * Created by gmf on 2019/11/4/10:47
 */
public abstract class AbstactFormatDecorator implements FormatDecorator {
    private static final int MIN_STACK_OFFSET =6;

    protected StackTraceElement getStackTraceElement() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(stackTraceElements);
        StackTraceElement stackTraceElement = stackTraceElements[stackOffset];
        return stackTraceElement;
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(@NonNull StackTraceElement[] trace) {
        checkNotNull(trace);
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(GMFLogger.class.getName()) && !name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return i;
            }
        }
        return -1;
    }
}
