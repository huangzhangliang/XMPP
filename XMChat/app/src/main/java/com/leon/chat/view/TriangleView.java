package com.leon.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.leon.chat.R;
import com.leon.chat.utils.LogUtils;

/**
 * Created by leon on 16/11/1.
 */

public class TriangleView extends View {

    private int mPaintColor;
    private int mOrientation;

    public TriangleView(Context context) {
        this(context,null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
        mPaintColor = a.getResourceId(R.styleable.TriangleView_paintColor,0);
        mOrientation = a.getInteger(R.styleable.TriangleView_triangleView_orientation,0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        if (mPaintColor == 0){
            p.setColor(Color.WHITE);
        }else {
            p.setColor(getResources().getColor(mPaintColor));
        }
        //实例化路径
        Path path = new Path();
        switch (mOrientation){
            case 1: // 向上
                path.moveTo(getWidth()/2,0);// 此点为多边形的起点
                path.lineTo(getWidth(), getHeight());
                path.lineTo(0, getHeight());
                break;
            case 2: // 向右
                path.moveTo(getWidth(),getHeight()/2);// 此点为多边形的起点
                path.lineTo(0, getHeight());
                path.lineTo(0,0);
                break;
            case 3: // 向下
                path.moveTo(getWidth() / 2,getHeight());// 此点为多边形的起点
                path.lineTo(0,0);
                path.lineTo(getWidth(),0);
                break;
            case 4: // 向左
                path.moveTo(0,getHeight() / 2);// 此点为多边形的起点
                path.lineTo(getWidth(),0);
                path.lineTo(getWidth(),getHeight());
                break;
        }
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);
    }
}
