package com.leon.chat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.leon.chat.base.BaseApplication;


/**
 * 和UI相关的工具类
 */
public class UIUtils {


    /**  得到上下文 */
    public static Context getContext(){
        return BaseApplication.getContext();
    }

    /** 得到Resources对象 */
    public static Resources getResources(){
        return getContext().getResources();
    }

    /** 得到Drawable对象 */
    public static Drawable getDrawable(int id){
        return ContextCompat.getDrawable(UIUtils.getContext(),id);
    }

    /** 得到String.xml中的字符串 */
    public static String getString(int resId){
        return getResources().getString(resId);
    }

    /** 得到String.xml中的字符串数组 */
    public static String[] getStringArr(int resId){
        return getResources().getStringArray(resId);
    }

    /** 得到Colors.xml中的的颜色 */
    public static int getColor(int colorId){
        return getResources().getColor(colorId);
    }

    /** 得到应用程序包名 */
    public static String getPackageName(){
        return getContext().getPackageName();
    }

    /** 得到主线程ID */
    public static long getMainThreadId(){
        return BaseApplication.getMainThreadId();
    }

    /** 得到主线程Handler */
    public static android.os.Handler getMainThreadHandler(){
        return BaseApplication.getHandler();
    }

    /** 安全的执行一个任务 */
    public static void postTaskSafely(Runnable task){
        int curThreadId = android.os.Process.myTid();
        if(curThreadId == getMainThreadId()){//如果当前线程是主线程
            task.run();
        }else{
            //如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }
    }

    /** 延迟任务 */
    public static void postTaskDelay(Runnable task, int delayMillis){
        getMainThreadHandler().postDelayed(task,delayMillis);
    }

    /** 移除任务 */
    public static void removeTask(Runnable task){
        getMainThreadHandler().removeCallbacks(task);
    }

    /** Dip换Px */
    public static int dip2Px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        int px = (int)(dip * density + .5f);
        return px;
    }

    /** Px换Dip */
    public static int px2Dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        int dip = (int)(px / density + .5f);
        return dip;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2Sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2Px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕密度
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * Toast
     * @param info 消息
     */
    public static void showToast(final String info){
        postTaskSafely(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),info,Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static int getScreensWidth(){
        return getContext().getResources().getDisplayMetrics().widthPixels; // 屏幕宽度（像素）;
    }

    public static int getScreensHeight(){
        return getContext().getResources().getDisplayMetrics().heightPixels;// 屏幕高度（像素）
    }


    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }



}
