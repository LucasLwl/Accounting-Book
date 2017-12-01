package com.phone.konka.accountingbook.Utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池管理类
 * <p>
 * Created by 廖伟龙 on 2017/11/30.
 */

public class ThreadPoolManager {

    /**
     * CPU数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();


    /**
     * 核心线程数
     */
    private static final int CORE_COUNT = CPU_COUNT + 1;


    /**
     * 线程池最大线程数
     */
    private static final int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;


    /**
     * 非核心线程空闲存活时间
     */
    private static final int KEEP_ALIVE = 1;


    /**
     * 线程池的私有对象
     */
    private static ThreadPoolManager mInstance;


    /**
     * 线程池
     */
    private ThreadPoolExecutor mExecutor;


    /**
     * 私有构造方法
     */
    private ThreadPoolManager() {

        mExecutor = new ThreadPoolExecutor(CORE_COUNT, MAX_POOL_SIZE, KEEP_ALIVE,
                java.util.concurrent.TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(128));

    }


    /**
     * 获取该私有类的对象
     *
     * @return
     */
    public static ThreadPoolManager getInstance() {
        if (mInstance == null) {
            synchronized (ThreadPoolManager.class) {
                if (mInstance == null)
                    mInstance = new ThreadPoolManager();
            }
        }
        return mInstance;
    }


    /**
     * 执行异步任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }

}
