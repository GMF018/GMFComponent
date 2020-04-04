package com.gmf.log.interfaces;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.adapter.LogAdapter;


/**
 * A proxy interface to enable additional operations.
 * Contains all possible Log message usages.
 */
public interface Printer {

    void addAdapter(@NonNull LogAdapter adapter);

    Printer t(@Nullable String tag);

    void d(@Nullable Object object);

    void d(@NonNull String message, @Nullable Object... args);

    void d(@NonNull int level, @NonNull String message, @Nullable Object... args);

    void e(@NonNull String message, @Nullable Object... args);

    void e(@NonNull int level, @NonNull String message, @Nullable Object... args);

    void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args);


    void w(@NonNull String message, @Nullable Object... args);

    void w(@NonNull int level, @NonNull String message, @Nullable Object... args);

    void i(@NonNull String message, @Nullable Object... args);

    void i(@NonNull int level, @NonNull String message, @Nullable Object... args);


    void v(@NonNull String message, @Nullable Object... args);

    void v(@NonNull int level, @NonNull String message, @Nullable Object... args);

    void a(@NonNull String message, @Nullable Object... args);

    void a(@NonNull int level, @NonNull String message, @Nullable Object... args);

    /**
     * Formats the given json content and print it
     */
    void json(@Nullable String json);

    /**
     * Formats the given xml content and print it
     */
    void xml(@Nullable String xml);

    void log(@Nullable int level, int priority, @Nullable String message, @Nullable Throwable throwable);

    void clearLogAdapters();

    void init(Context context);

    void setDislDefaultPath(String path);

    void commit();

    void release();


    void removeLogAdapter(int level);
}
