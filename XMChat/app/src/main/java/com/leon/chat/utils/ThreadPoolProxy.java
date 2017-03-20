package com.leon.chat.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @des 线程池
 */
public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime;

    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
        mKeepAliveTime = keepAliveTime;
    }

    private ThreadPoolExecutor initThreadPoolExecutor(){
        if(mExecutor == null){
            synchronized (ThreadPoolProxy.class){ // 又重检查加锁
                if(mExecutor == null){
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(); // 无界队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory(); // 线程工厂
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy(); // 异常方案
                    mExecutor = new ThreadPoolExecutor(
                            mCorePoolSize, // 核心线程数
                            mMaximumPoolSize, // 最大线程数
                            mKeepAliveTime, // 保持时间
                            unit, // 保持时间对应的单位
                            workQueue, // 缓存队列/阻塞队列
                            threadFactory, // 线程工厂
                            handler // 异常捕获器
                    );
                }
            }
        }
        return mExecutor;
    }

    // 执行任务
    public void execute(Runnable task){
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }
    // 提交任务
    public Future<?> submit(Runnable task){
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }
    // 移除任务
    public void removeTask(Runnable task){
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }


}
