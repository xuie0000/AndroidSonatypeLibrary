package com.xuie0000.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * @author ：ZouWeiLin on 2018.09.04
 */
public class HandlerHelper {

    private final HashMap<String, HandlerThread> mThreadMap = new HashMap<>();
    private static HandlerHelper mHandlerWorker;
    private static Handler mainHandler;
    private final String DEFAULT = "DEFAULT-HANDLER";

    public static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }

        return mainHandler;
    }

    public final Handler createDefaultHandler() {
        return this.createThreadHandler(DEFAULT);
    }

    public static Handler createMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public final Handler createThreadHandler(String handlerThreadName) {
        return new Handler(this.createThread(handlerThreadName).getLooper());
    }

    private HandlerThread createThread(String threadName) {
        synchronized (this.mThreadMap) {
            HandlerThread handlerThread;
            if ((handlerThread = this.mThreadMap.get(threadName)) == null) {
                (handlerThread = new HandlerThread("HT-" + (TextUtils.isEmpty(threadName) ? DEFAULT : threadName))).start();
                this.mThreadMap.put(threadName, handlerThread);
            }

            return handlerThread;
        }
    }

    private HandlerHelper() {
    }

    public static synchronized HandlerHelper newInstance() {
        if (mHandlerWorker == null) {
            mHandlerWorker = new HandlerHelper();
        }
        return mHandlerWorker;
    }
}
