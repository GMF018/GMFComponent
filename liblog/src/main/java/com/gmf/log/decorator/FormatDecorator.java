package com.gmf.log.decorator;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Used to determine how messages should be printed or saved.
 *
 * @see PrettyFormatDecorator
 * @see DiskFormatDecorator
 */
public interface FormatDecorator {

    void log(int priority, @NonNull String message);

}
