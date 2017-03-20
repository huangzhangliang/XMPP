package com.leon.chat.utils;


public class ThreadPoolFactory {

    static ThreadPoolProxy mNormalProxy; // 普通线程池
    static ThreadPoolProxy mDownLoadProxy; // 下载线程池

    /* 得到一个普通线程池 */
    public static ThreadPoolProxy getNormalProxy(){
        if(mNormalProxy == null){
            synchronized (ThreadPoolProxy.class){
                if(mNormalProxy == null){
                    mNormalProxy = new ThreadPoolProxy(5,5,3000);
                }
            }
        }
        return mNormalProxy;
    }
    /* 得到一个下载线程池 */
    public static ThreadPoolProxy getDownLoadProxy(){
        if(mDownLoadProxy == null){
            synchronized (ThreadPoolProxy.class){
                if(mDownLoadProxy == null){
                    mDownLoadProxy = new ThreadPoolProxy(3,3,3000);
                }
            }
        }
        return mDownLoadProxy;
    }

}
