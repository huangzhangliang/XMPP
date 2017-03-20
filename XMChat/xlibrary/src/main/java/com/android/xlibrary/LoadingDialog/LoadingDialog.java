package com.android.xlibrary.LoadingDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.android.xlibrary.R;


/**
 * created by leon
 */
public class LoadingDialog extends ProgressDialog {
    private String tip;
    private ImageView mIvLevel;
    private Drawable mProgressbar;
    private Context mContext;
    private static LoadingDialog loadingDialog;


//    public static LoadingDialog getInstance(Context context) {
//        if(loadingDialog == null) {//双重校验DCL单例模式
//            synchronized(LoadingDialog.class) {//同步代码块
//                if(loadingDialog == null) {
//                    loadingDialog = new LoadingDialog(context);//创建一个新的实例
//                }
//            }
//        }
//        return loadingDialog;//返回一个实例
//    }

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        mContext = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mIvLevel = (ImageView) findViewById(R.id.level_view);
        mProgressbar = mContext.getResources().getDrawable(R.drawable.custom_progressbar_style_small);
        mIvLevel.setImageDrawable(mProgressbar);
        setCancelable(true);
        /**** 透明度设置 ****/
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;// 透明度
        lp.dimAmount = 0.3f;// 黑暗度
        window.setAttributes(lp);
    }

    @Override
    public void setMessage(CharSequence message) {
        tip = message.toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
        rotateAnimation.setDuration(1500); // 时间
        rotateAnimation.setRepeatCount(Integer.MAX_VALUE); // 循环次数
        mIvLevel.startAnimation(rotateAnimation);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


}