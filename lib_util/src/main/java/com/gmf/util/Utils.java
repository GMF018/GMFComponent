package com.gmf.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;


/**
 * @Auther :GMF
 * @datetime :2020/4/4
 * @desc :
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApp;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of UtilsFileProvider.</p>
     *
     * @param app application
     */
    public static void init(final Application app) {
        if (sApp != null) return;
        sApp = app;
        UtilsBridge.init();
    }

    /**
     * Return the Application object.
     *
     * @return the Application object
     */
    public static Application getApp() {
        if (sApp != null) return sApp;
        throw new NullPointerException("UtilsFileProvider load failed.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public abstract static class Task<Result> extends UtilsBridge.Task<Result> {

        private Consumer<Result> mConsumer;

        public Task(final Consumer<Result> consumer) {
            mConsumer = consumer;
        }

        @Override
        public void onSuccess(Result result) {
            if (mConsumer != null) {
                mConsumer.accept(result);
            }
        }
    }
    public interface OnAppStatusChangedListener {
        void onForeground(Activity activity);

        void onBackground(Activity activity);
    }

    public static class ActivityLifecycleCallbacks {

        public void onActivityCreated(@NonNull Activity activity) {/**/}

        public void onActivityStarted(@NonNull Activity activity) {/**/}

        public void onActivityResumed(@NonNull Activity activity) {/**/}

        public void onActivityPaused(@NonNull Activity activity) {/**/}

        public void onActivityStopped(@NonNull Activity activity) {/**/}

        public void onActivityDestroyed(@NonNull Activity activity) {/**/}

        public void onLifecycleChanged(@NonNull Activity activity, Lifecycle.Event event) {/**/}
    }

    public interface OnActivityDestroyedListener {
        void onActivityDestroyed(Activity activity);
    }

    public interface Consumer<T> {
        void accept(T t);
    }

    public interface Supplier<T> {
        T get();
    }

    public interface Func1<Ret, Par> {
        Ret call(Par param);
    }
}
