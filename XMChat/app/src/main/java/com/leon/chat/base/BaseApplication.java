package com.leon.chat.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;


import okhttp3.OkHttpClient;


/**
 * 定义一个全局的盒子,里面定义的方法和变量都是全局可用的
 */
public class BaseApplication extends Application {

    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadId;
    private static Looper mLooper;
    private static Handler mHandler;
    public static String IP = "192.168.1.164";
    public static String REALM_NAME = "hzlleon.com";
    public static int PORT = 5222;


    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getLooper() {
        return mLooper;
    }


    @Override
    public void onCreate() { // 程序入口
        // 初始化一些常用属性

        // 上下文路径
        mContext = getApplicationContext();

        // 主线程
        mMainThread = Thread.currentThread();

        // 主线程ID
        mMainThreadId = android.os.Process.myTid();

        // 主线程对象
        mLooper = getMainLooper();

        // 定义一个handler
        mHandler = new Handler();




//        Fresco.initialize(mContext);

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);


//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("okdebug",true))
//                .build();
//        OkHttpUtils.initClient(okHttpClient);



        super.onCreate();

    }




}
