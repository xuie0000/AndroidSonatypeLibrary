package com.xuie0000.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZouWeiLin on 2018.04.17
 */
public class Worker {

    private final int CORE_NUM = Runtime.getRuntime().availableProcessors();
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    /**
     * 多线程池
     */
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(CORE_NUM, CORE_NUM * 2,
            5, TimeUnit.SECONDS,
            workQueue, ThreadFactoryHelper.getThreadFactor("utils multiple " + Worker.class.getSimpleName()));
    /**
     * 单线程池
     */
    private ExecutorService singleThread = new ThreadPoolExecutor(1, 1,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), ThreadFactoryHelper.getThreadFactor("utils single " + Worker.class.getSimpleName()));

    private Worker() {
        //no instance
    }

    public void executeSingle(Runnable runnable) {
        singleThread.execute(runnable);
    }

    /**
     * 使用线程池中的一个线程来执行任务
     *
     * @param runnable 任务
     */
    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return threadPoolExecutor.submit(callable);
    }

    public static Worker newInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final Worker INSTANCE = new Worker();
    }

    /**
     * 创建一个单线程
     *
     * @param threadName 线程名称
     * @param daemon     是否是守护线程
     * @return 线程池
     */
    public static ExecutorService createSingleThread(String threadName, boolean daemon) {
        return new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), ThreadFactoryHelper.getThreadFactor(threadName, daemon));
    }

    public static ScheduledExecutorService createScheduledThread(String name) {
        return new ScheduledThreadPoolExecutor(1, ThreadFactoryHelper.getThreadFactor(name));
    }
}
