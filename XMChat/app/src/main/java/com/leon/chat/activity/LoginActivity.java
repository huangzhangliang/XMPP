package com.leon.chat.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.leon.chat.fragment.LoginFragment;

/**
 * Created by leon on 16/10/26.
 */

public class LoginActivity extends NavigationActivity {

    @Override
    public Fragment initView(FrameLayout headerContainer, int fragmentIndex, Context context) {
        return new LoginFragment();
    }
}
