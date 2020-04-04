package com.gmf.log.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.adapter.LogAdapter;
import com.gmf.log.interfaces.Printer;

import static com.gmf.log.core.Utils.checkNotNull;


public final class Logger {
    @NonNull
    private static Printer printer = new LoggerPrinter();

    private Logger() {
        //no instance
    }

    public static void init(Context context) {
        printer.init(context);
    }

    public static void setDislDefaultPath(String path) {
        printer.setDislDefaultPath(path);
    }

    public static void printer(@NonNull Printer printer) {
        Logger.printer = checkNotNull(printer);
    }

    public static void addLogAdapter(@NonNull LogAdapter adapter) {
        printer.addAdapter(adapter);
    }

    public static void clearLogAdapters() {
        printer.clearLogAdapters();
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    public static Printer t(@Nullable String tag) {
        return printer.t(tag);
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(@Nullable int level, int priority, @Nullable String message, @Nullable Throwable throwable) {
        printer.log(level, priority,message, throwable);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        printer.d(message, args);
    }

    public static void d(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        printer.d(level, message, args);
    }

    public static void d(@Nullable Object object) {
        printer.d(object);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        printer.e(null, message, args);
    }

    public static void e(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        printer.e(level, message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        printer.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        printer.i(message, args);
    }

    public static void i(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        printer.i(level, message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        printer.v(message, args);
    }

    public static void v(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        printer.v(level, message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        printer.w(message, args);
    }

    public static void w(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        printer.w(level, message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void a(@NonNull String message, @Nullable Object... args) {
        printer.a(message, args);
    }

    public static void a(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        printer.a(level, message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String json) {
        printer.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String xml) {
        printer.xml(xml);
    }

    public static void commit() {
        printer.commit();
    }

    public static void release() {
        printer.release();
    }

    public static void removeLogAdapter(int level) {
        printer.removeLogAdapter(level);
    }
}
