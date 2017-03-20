package com.leon.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.android.xlibrary.util.StatusBarUtil;
import com.leon.chat.R;
import com.leon.chat.base.BaseActivity;
import com.leon.chat.base.BaseApplication;
import com.leon.chat.base.BaseNavigationBar;
import com.leon.chat.databinding.ActivityBaseBinding;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.UIUtils;


/**
 * Created by leon on 16/7/23.
 */
public abstract class NavigationActivity extends BaseActivity {

    public static final String INTENT_FRAGMENT_INDEX_KEY = "fragment_index";
    private static final String TAG_HEADER = "TAG_HEADER";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    private static final String TAG_FOOTER = "TAG_FOOTER";
    private ActivityBaseBinding mDetailsBinding;
    private int mFragmentIndex;
    private BaseNavigationBar mBaseNavigationBar;
    private FragmentManager mFm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        mDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this,R.color.colorOrange);
//        StatusBarUtil.StatusBarLightMode(this,StatusBarUtil.StatusBarLightMode(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDetailsBinding.superContainer.setPadding(0, UIUtils.getStatusBarHeight(), 0, 0);
        }
        mFragmentIndex = getIntent().getIntExtra(INTENT_FRAGMENT_INDEX_KEY, -1);
//        mBaseNavigationBar = new BaseNavigationBar(this);
//        mDetailsBinding.headerContainer.addView(mBaseNavigationBar);
        initFragment();
//        initFooterView(headerView);
    }

    /**
     * 初始化一个头布局
     * @headerView 新创建的头布局
     */
    public void initHeader(View headerView){
        mDetailsBinding.headerContainer.removeAllViews();
        mDetailsBinding.headerContainer.addView(headerView);
    }

    private void initFragment(){
        mFm = getSupportFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();// 开启事务
        // 将帧布局替换成相对应的Fragment
        Fragment fragment = initView(mDetailsBinding.headerContainer,mFragmentIndex,this);
        if (fragment != null){
            LogUtils.sf("将帧布局替换成相对应的Fragment");
            ft.replace(mDetailsBinding.contentContainer.getId(),fragment,TAG_CONTENT);
        }
        ft.commit(); // 提交事务
    }
    public abstract Fragment initView(FrameLayout headerContainer, int fragmentIndex, Context context);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
        Fragment f = mFm.findFragmentByTag(TAG_CONTENT);
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
    }

}
