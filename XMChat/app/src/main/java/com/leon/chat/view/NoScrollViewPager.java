package com.leon.chat.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }


    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // 决定事件是否中断
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; // 不拦截事件，让子ViewPager能获取事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 重写ViewPager事件，改成什么都不做
        return true;
    }
}
