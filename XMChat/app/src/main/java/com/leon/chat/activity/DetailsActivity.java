package com.leon.chat.activity;

import android.content.Context;
import android.widget.FrameLayout;

import com.leon.chat.base.BaseFragment;
import com.leon.chat.fragment.ChatFragment;
import com.leon.chat.fragment.LoginFragment;


/**
 * Created by leon on 16/10/26.
 */

public class DetailsActivity extends NavigationActivity {

    public final static int FRAGMENT_LOGIN = 1; // 登录
    public final static int FRAGMENT_CHAT = 2; // 聊天界面

    @Override
    public BaseFragment initView(FrameLayout headerContainer, int fragmentIndex, Context context) {
        BaseFragment fragment = null;
        switch (fragmentIndex) {
            case FRAGMENT_LOGIN:
                fragment = new LoginFragment();
                break;
            case FRAGMENT_CHAT:
                fragment = new ChatFragment();
                break;
        }
        return fragment;
    }

}

