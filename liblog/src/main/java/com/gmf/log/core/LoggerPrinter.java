package com.gmf.log.core;


import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmf.log.adapter.LogAdapter;
import com.gmf.log.consts.LogConsts;
import com.gmf.log.interfaces.ICommitable;
import com.gmf.log.interfaces.ILifeStyle;
import com.gmf.log.interfaces.Printer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.gmf.log.core.Utils.checkNotNull;


public class LoggerPrinter implements Printer {
    /**
     * 控制台
     */
    public static final int CONSOLE = 0;
    /**
     * 磁盘
     */
    public static final int DISK = 1;
    /**
     * 服务器
     */
    public static final int SERVER = 2;
    /**
     * 磁盘服务器
     */
    public static final int DISK_SERVER = 3;
    //--------------------------------------------------------------------------------------------------------------
    public static final int LOGGER_LEVEL_ALL = 0;
    public static final int LOGGER_LEVEL_DEBUG = 1;
    public static final int LOGGER_LEVEL_INFO = 2;
    public static final int LOGGER_LEVEL_WARN = 3;
    public static final int LOGGER_LEVEL_ERROR = 4;
    public static final int LOGGER_LEVEL_FATAL = 5;
    public static final int LOGGER_LEVEL_VERBOSE = 6;
    public static final int LOGGER_LEVEL_ASSERT = 7;

    public static final int LOGGER_LEVEL_NONE = 10;
    /**
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 2;

    /**
     * Provides one-time used tag for the log message
     */
    private final ThreadLocal<String> localTag = new ThreadLocal<>();

    private final List<LogAdapter> logAdapters = new ArrayList<>();

    @Override
    public void init(Context context) {
        initLogConsts(context);
    }

    @Override
    public void setDislDefaultPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        LogConsts.localDiskPath = path;
    }


    private void initLogConsts(Context context) {
        if (context == null) {
            return;
        }
//        context.getExternalFilesDir().getAbsolutePath()
//        LogConsts.localDiskPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getPackageName() + File.separator + "tflog";
//        LogConsts.uuid = AppInfoUtils.getUuId(context);
//        LogConsts.packageName = AppInfoUtils.getPackageName(context);
//        LogConsts.appVersion = AppInfoUtils.getVerName(context);
    }

    @Override
    public Printer t(String tag) {
        if (tag != null) {
            localTag.set(tag);
        }
        return this;
    }

    @Override
    public void d(@NonNull String message, @Nullable Object... args) {
        d(CONSOLE, message, args);
    }

    @Override
    public void d(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        log(level, LOGGER_LEVEL_DEBUG, null, message, args);
    }

    @Override
    public void d(@Nullable Object object) {
        log(CONSOLE, LOGGER_LEVEL_DEBUG, null, Utils.toString(object));
    }

    @Override
    public void e(@NonNull String message, @Nullable Object... args) {
        e(null, message, args);
    }

    @Override
    public void e(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        log(level, LOGGER_LEVEL_ERROR, null, message, args);
    }

    @Override
    public void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        log(CONSOLE, LOGGER_LEVEL_ERROR, throwable, message, args);
    }


    @Override
    public void w(@NonNull String message, @Nullable Object... args) {
        w(CONSOLE, message, args);
    }

    @Override
    public void w(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        log(level, LOGGER_LEVEL_WARN, null, message, args);
    }

    @Override
    public void i(@NonNull String message, @Nullable Object... args) {
        i(CONSOLE, message, args);
    }

    @Override
    public void i(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        log(level, LOGGER_LEVEL_INFO, null, message, args);
    }

    @Override
    public void v(@NonNull String message, @Nullable Object... args) {
        v(CONSOLE, message, args);
    }

    @Override
    public void v(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        log(level, LOGGER_LEVEL_VERBOSE, null, message, args);
    }

    @Override
    public void a(@NonNull String message, @Nullable Object... args) {
        a(CONSOLE, message, args);
    }

    @Override
    public void a(@NonNull int level, @NonNull String message, @Nullable Object... args) {
        log(level, LOGGER_LEVEL_ASSERT, null, message, args);
    }

    @Override
    public void json(@Nullable String json) {
        if (Utils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
                return;
            }
            e("Invalid Json");
        } catch (JSONException e) {
            e("Invalid Json");
        }
    }

    @Override
    public void xml(@Nullable String xml) {
        if (Utils.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e("Invalid xml");
        }
    }


    @Override
    public void clearLogAdapters() {
        logAdapters.clear();
    }


    @Override
    public void addAdapter(@NonNull LogAdapter adapter) {
        checkNotNull(adapter);
        checkUnique(adapter);
        logAdapters.add(adapter);
    }

    /**
     * 确保每种打印类型有且只有一个,若重复，移除前者
     *
     * @param adapter
     */
    private void checkUnique(LogAdapter adapter) {
        if (logAdapters == null) {
           return;
        }
        Iterator<LogAdapter> iterator = logAdapters.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().tag() == adapter.tag()) {
                iterator.remove();
                break;
            }
        }
    }

    private synchronized void log(int level, int priority,
                                  @Nullable Throwable throwable,
                                  @NonNull String msg,
                                  @Nullable Object... args) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        String message = createMessage(msg, args);
        log(level, priority, message, throwable);
    }

    @Override
    public synchronized void log(@Nullable int level,
                                 int priority,
                                 @Nullable String message,
                                 @Nullable Throwable throwable) {
        if (throwable != null && message != null) {
            message += " : " + Utils.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable);
        }
        if (Utils.isEmpty(message)) {
            return;
        }

        for (LogAdapter adapter : logAdapters) {
            if (adapter.isLoggable(priority, level)) {
                adapter.log(priority, message);
            }
        }
    }

    /**
     * @return the appropriate tag based on local or global
     */
    @Nullable
    private String getTag() {
        String tag = localTag.get();
        if (tag != null) {
            localTag.remove();
            return tag;
        }
        return null;
    }

    @NonNull
    private String createMessage(@NonNull String message, @Nullable Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }


    @Override
    public void commit() {
        if (logAdapters == null) {
            return;
        }
        for (LogAdapter logAdapter : logAdapters) {
            if (logAdapter instanceof ICommitable) {
                ((ICommitable) logAdapter).commit();
                break;
            }
        }
    }

    @Override
    public void release() {
        if (logAdapters == null) {
            return;
        }
        for (LogAdapter logAdapter : logAdapters) {
            if (logAdapter instanceof ILifeStyle) {
                ((ILifeStyle) logAdapter).release();
            }
        }
        logAdapters.clear();
    }

    @Override
    public void removeLogAdapter(int level) {
        if (logAdapters == null) {
            return;
        }
        Iterator<LogAdapter> iterator = logAdapters.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().tag() == level) {
                iterator.remove();
            }
        }
    }


}
