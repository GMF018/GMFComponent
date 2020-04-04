//package com.gmf.log.decorator;
//
//import android.os.CountDownTimer;
//import android.os.Looper;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.talkfun.liblog.CourseInfo;
//import com.talkfun.liblog.LogConfig;
//import com.talkfun.liblog.LogInfo;
//import com.talkfun.liblog.consts.LogConsts;
//import com.talkfun.liblog.interfaces.ICommitable;
//import com.talkfun.liblog.interfaces.ILifeStyle;
//import com.talkfun.liblog.logger.strategy.LogStrategy;
//import com.talkfun.liblog.logger.strategy.ServerLogStrategy;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * Created by ccy on 2019/11/4/10:21
// */
//public class ServerFormatDecorator extends AbstactFormatDecorator implements ILifeStyle, ICommitable {
//    private List<LogInfo> logList = new CopyOnWriteArrayList<>();
//    private CountDownTimer countDownTimer;
//    private AtomicBoolean isStartTimer = new AtomicBoolean(false);
//    private LogConfig logConfig;
//    private LogStrategy logStrategy;
//
//    public ServerFormatDecorator(Builder builder) {
//        logConfig = builder.logConfig;
//        logStrategy = builder.logStrategy;
//    }
//
//    @Override
//    public void log(int priority, @Nullable CourseInfo info, @NonNull String message) {
//        if (logConfig == null) {
//            return;
//        }
//        LogInfo logInfo = new LogInfo();
//        StackTraceElement stackTraceElement = getStackTraceElement();
//        logInfo.channel = logConfig.channel == 0 ? LogConsts.CHANNEL : logConfig.channel;
//        logInfo.level = priority;
//        logInfo.className = stackTraceElement.getClassName();
//        logInfo.func = stackTraceElement.getMethodName();
//        logInfo.line = stackTraceElement.getLineNumber();
//        logInfo.content = message;
//        logInfo.timestamp = System.currentTimeMillis() / 1000;
//        logInfo.sdkVersion = LogConsts.VERSION;
//        logInfo.appVersion = LogConsts.appVersion;
//        logInfo.uuid = LogConsts.uuid;
//        logInfo.appName = LogConsts.packageName;
//        logInfo.userID = logConfig.user != null ? logConfig.user.uid : "";
//        logInfo.cid = String.valueOf(info == null ? (logConfig.user != null ? logConfig.user.course_id : "") : (info.courseid));
//        logInfo.rid = info == null ? (logConfig.user != null ? logConfig.user.roomid : 0) : (info.roomid);
//        logInfo.pid = info == null ? (logConfig.user != null ? logConfig.user.pid : 0) : (info.pid);
//        logList.add(logInfo);
//        if (logList.size() >= logConfig.handlerConfig.queueSize)
//            commit();
//        else if (logList.size() > 0 && !isStartTimer.get()) {
//            startTimer();
//        }
//    }
//
//    /**
//     * 开始计时
//     */
//    private synchronized void startTimer() {
//        int totalTime = logConfig.handlerConfig.queueTime * 1000;
//        int interval = totalTime;
//
//        if (Looper.myLooper() == null) {
//            Looper.prepare();
//        }
//        if (countDownTimer != null)
//            stopTimer();
//        countDownTimer = new CountDownTimer(totalTime, interval) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                countDownTimer = null;
//                commit();
//                isStartTimer.set(false);
//            }
//        };
//        isStartTimer.set(true);
//        countDownTimer.start();
//    }
//
//    /**
//     * 提交日志
//     */
//    @Override
//    public synchronized void commit() {
//        stopTimer();
//        if (logList.size() == 0)
//            return;
//        Gson gson = new GsonBuilder().create();
//        String jsonStr = null;
//        synchronized (logList) {
//            jsonStr = gson.toJson(logList);
//            logList.clear();
//        }
//        if (!TextUtils.isEmpty(jsonStr))
//            logStrategy.log(0, "", jsonStr);
//    }
//
//    /**
//     * 停止计时
//     */
//    protected synchronized void stopTimer() {
//        if (countDownTimer != null)
//            countDownTimer.cancel();
//        countDownTimer = null;
//        isStartTimer.set(false);
//    }
//    @NonNull
//    public static ServerFormatDecorator.Builder newBuilder() {
//        return new ServerFormatDecorator.Builder();
//    }
//
//    public static final class Builder {
//        LogStrategy logStrategy;
//        LogConfig logConfig;
//
//        private Builder() {
//        }
//
//        @NonNull
//        public Builder logStrategy(@Nullable LogStrategy val) {
//            logStrategy = val;
//            return this;
//        }
//
//        public Builder logConfig(@Nullable LogConfig logConfig) {
//            this.logConfig = logConfig;
//            return this;
//        }
//
//
//        @NonNull
//        public ServerFormatDecorator build() {
//            if (logStrategy == null) {
//                logStrategy = new ServerLogStrategy();
//            }
//            return new ServerFormatDecorator(this);
//        }
//    }
//
//    @Override
//    public void release() {
//        if (logList != null && logList.size() > 0)
//            commit();
//        stopTimer();
//        logList = null;
//        if (logStrategy != null && logStrategy instanceof ServerLogStrategy) {
//            ((ServerLogStrategy) logStrategy).release();
//        }
//    }
//}
