package com.leon.chat.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.FrameLayout;

import com.leon.chat.R;
import com.leon.chat.databinding.LayoutNavigationBinding;

/**
 * Created by leon on 16/10/31.
 */

public class BaseNavigationBar extends FrameLayout {

    private String mNavTitle;
    public LayoutNavigationBinding mNavigationBinding;

    public void setNavTitle(String navTitle) {
        mNavTitle = navTitle;
        mNavigationBinding.tvNavTitle.setText(navTitle);
    }

    public String getNavTitle() {
        return mNavTitle;
    }

    public BaseNavigationBar(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View v = View.inflate(context, R.layout.layout_navigation,null);
        mNavigationBinding = DataBindingUtil.bind(v);
        initLayout(mNavigationBinding,context);
        initListener((BaseActivity)context, mNavigationBinding);
        this.addView(v);
    }

    public void initLayout(LayoutNavigationBinding navigationBinding,Context context){

    };

    public void initListener(final BaseActivity activity , LayoutNavigationBinding navigationBinding){
        navigationBinding.btnNavBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
